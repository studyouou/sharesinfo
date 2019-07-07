package com.hand.sharesinfo.datadownload;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class DiaoYongApp {
    static List<IpAdd> ipAdds = new ArrayList<IpAdd>();
    //储存行业以及其股票数量
    static Map<String,Integer> hangyePage = new HashMap<String,Integer>();

    //行业列表
    static List<String> hangye = new ArrayList<String>();

    static BufferedWriter fw = null;

    static ThreadLocal<Statement> threadLocal = new ThreadLocal<Statement>();

    static int count = 0 ;

    public static void main(String[] args) throws IOException, SQLException {
        fw = new BufferedWriter(new FileWriter("d:\\handdata\\shares.txt"));
        //获取代理ip
//        ipAdds = new ProxyIP().getIpAddrs(5);
        //获取所有行业的coede
        List<String> url_list = getId("http://money.finance.sina.com.cn/q/view/newFLJK.php?param=industry");
        String sql = "create table if not exists shares(code varchar(20) primary key,hangyecode varchar(20)" +
                ",name varchar(40) not null,nowPrice double not null,index index_code(code))engine=InnoDB CHARACTER SET UTF8";
        createTable(sql,"shares");
        createTables(url_list);
        //获取到每个行业的股票code
        getCodeListAndDown(url_list);

        fw.close();
    }

    private static void createTables(List<String> url_list) throws SQLException {
        for (String code : url_list){
            for (int i=2019;i>=2010;i--) {
                String sql = "create table if not exists " + code+"(id varchar(40) primary key,time varchar(20),code varchar(20) " +
                        "not null,name varchar(40) not null,price double,zhangfu double,month tinyint not null,day tinyint not null,index index_id(code),index index_day(day)" +
                        ",index index_month(month)" +
                        ",index index_code(code))engine=MyISAM CHARACTER SET UTF8";
                createTable(sql,(code));
            }
        }

    }

    private static void createTable(String createTableSql,String tableName) throws SQLException {
        java.sql.Connection connection = ConnectionUtil.getConnection();
        Statement statement = connection.createStatement();
        statement.execute(createTableSql);
        System.out.println("数据库 "+tableName+"已创建好");
        statement.close();
        connection.close();
    }

    private static List getId(String url) throws IOException {
        //请求所有行业的json数据
        List<String> url_list = new ArrayList<String>();
        Map<String,String> header = new HashMap<String,String>();
        header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        Document document = getDocument(url,header);
        String body = document.body().text();
        body = body.substring(body.indexOf("{"));
        JSONObject jsonObject = JSON.parseObject(body);
        Map<String,Object> map= jsonObject.getInnerMap();
        map.remove("");

        Set<String> entry_set = map.keySet();
        for (String primary: entry_set){
            //将所有行业的code储存到url_list中
            primary= primary.substring(primary.lastIndexOf('#')+1,primary.length());
            url_list.add(primary);
            //获取每个行业里面的页数，方便取出里面的股票code
            getPage(primary);
//            break;//测试break
        }
        return url_list;
    }

    private static void getPage(String primary) throws IOException {
        //请求json数据获得该行业内股票总数，然后根据每页大小算出多好页。
        String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeStockCount?node="+primary;
        int te = getPageNum(url,false);
        if (te==0){
            return;
        }
        //将行业code以及它含有页数储存进map中。
        hangyePage.put(primary,te);
    }

    private static void getCodeListAndDown(List<String> url_list) throws IOException, SQLException {
        //根据fiddler抓取知道获取每个code请求的接口为http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/
        // Market_Center.getHQNodeData?page=1&num=40&sort=symbol&asc=1&node=hangye_ZF21&symbol=&_s_r_a=init所以对url进行重新
        //拼装然后请求获取json对象解析，每个行业可能含有几页股票，所以对page参数需要拼装
        Map<String,String> codeNameMap = new HashMap<String,String>();
        String qianzhui = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=";
        String zhongjian = "&num=40&sort=symbol&asc=1&node=";
        String houzhui ="&symbol=&_s_r_a=init";
        for (String hang_url :url_list){
            //根据每个行业和每页来获取代码code
            int page = hangyePage.get(hang_url);
            if (hangyePage.get(hang_url)==0){
                break;
            }
            else {
                getCode(qianzhui,zhongjian,houzhui,page,hang_url,codeNameMap);
            }
            downLoadData(codeNameMap,hang_url);
            codeNameMap.clear();
        }
    }

    private static void getCode(String qianzhui, String zhongjian, String houzhui, int page, String hang_url, Map<String,String> codeNameMap) throws IOException {
        for (int i=1;i<=page;i++){
            //拼装地址
            String hangye = new StringBuilder().append(qianzhui).append(String.valueOf(i)).append(zhongjian).append(hang_url).append(houzhui).toString();
            Map<String,String> header = new HashMap<String,String>();
            header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            Document document = getDocument(hangye,header);
            //解析json获取股票code，并将code传入list中
            JSONArray array = getJsonArray(document);
            if (array==null){
                continue;
            }
            for (Object o:array){
                JSONObject jsonO = (JSONObject) o;
                String code = (String) jsonO.get("symbol");
                String name = (String) jsonO.get("name");
                codeNameMap.put(code,name);
            }
        }

    }

    private static void insertData(String tableName,Map<String,Object> paramaterMap,Boolean batch) throws SQLException {
        if (batch){
            if (threadLocal.get()==null){
                java.sql.Connection connection = ConnectionUtil.getConnection();
                threadLocal.set(connection.createStatement());
            }
            Statement statement = threadLocal.get();
            if (paramaterMap == null){
                statement.executeBatch();
                System.out.println("批量执行sql");
                statement.clearBatch();
                statement.getConnection().close();
                threadLocal.remove();
            }else {
                String sql = sqlPingZhuang(tableName,paramaterMap);
                System.out.println("向statement里放入的语句"+sql);
                statement.addBatch(sql);
                //记录插入量
                count++;
            }
        }else {
            java.sql.Connection connection = ConnectionUtil.getConnection();
            Statement statement = connection.createStatement();
            String sql = sqlPingZhuang(tableName,paramaterMap);
            try {
                statement.execute(sql);
                count++;
            }finally {
                connection.close();
            }
            System.out.println(sql);
        }



    }

    private static String sqlPingZhuang(String tableName, Map<String, Object> paramaterMap) {
        Set<String> key_set = paramaterMap.keySet();
        int size = paramaterMap.size();
        int jishuqi = 1;
        StringBuilder sqlBuild = new StringBuilder().append("insert into ").append(tableName).append("(");
        StringBuilder valus = new StringBuilder();
        for (String key : key_set){
            if (jishuqi == size){
                sqlBuild.append(key).append(") values(");
                valus.append("'").append(paramaterMap.get(key)).append("'").append(")");
            }else {
                sqlBuild.append(key).append(",");
                valus.append("'").append(paramaterMap.get(key)).append("'").append(",");
            }
            jishuqi++;
        }
        return sqlBuild.append(valus).toString();
    }

    private static JSONArray getJsonArray(Document document) {
        Element jsons = document.body();
        String s = jsons.text();
        System.out.println(s);
        return JSON.parseArray(s);
    }

    private static void downLoadData(Map<String, String> codeNameMap, String hang_url) throws IOException, SQLException {
        Set<String> keys = codeNameMap.keySet();
        for (String code : keys){
            //获取页数
            String pageUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssc_qsfx_lscjfb?daima="+code;
            int pageNum = getPageNum(pageUrl,true);
            if (pageNum==0) continue;
            double onetrade = 0;
            String name = codeNameMap.get(code);
            for (int i=1;i<=pageNum;i++){
                String qianzui = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/MoneyFlow.ssl_qsfx_lscjfb?page=";
                String houzhui = "&num=2500&sort=opendate&asc=0&daima="+code;
                //根据股票code获取相应的数据
                String uu =new StringBuilder().append(qianzui).append(i).append(houzhui).toString();
                Map<String,String> header = new HashMap<String,String>();
                header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
                Document document = getDocument(uu,header);
                JSONArray array = getJsonArray(document);
                if (array==null){
                    continue;
                }
                //获取最新一天的数据
                JSONObject dayone = array.getJSONObject(0);
                onetrade = Double.parseDouble(((String)dayone.get("trade")).substring(0,4));
                //返回的json处理获取股票日期，当日封盘价格和当日涨幅
                for (Object o:array){
                    JSONObject jsonO = (JSONObject) o;
                    String data = (String) jsonO.get("opendate");
                    String s = (String)jsonO.get("changeratio");
                    double zhangfu = 0;
                    if (s.length()>6){
                        zhangfu =  Double.parseDouble(s.substring(0,6));
                    }else {
                        zhangfu =  Double.parseDouble(s);
                    }

                    double trade = Double.parseDouble(((String)jsonO.get("trade")).substring(0,4));
                    String[] tem = data.split("-");
//                    String sql = "insert into "+hang_url+"_"+tem[0]+"(time,month,code,price,name,zhangfu)" +
//                            " values('"+data+"','"+code+"','"+trade+"','"+tem[1]+"','"+name+"','"+zhangfu+"')";
                    String tableName = hang_url;
                    Map<String, Object> parameterMap = new HashMap<String,Object>();
                    parameterMap.put("id",code+tem[0]+tem[1]+tem[2]);
                    parameterMap.put("time",data);
                    parameterMap.put("month",tem[1]);
                    parameterMap.put("code",code);
                    parameterMap.put("price",trade);
                    parameterMap.put("zhangfu",zhangfu);
                    parameterMap.put("name",name);
                    parameterMap.put("day",tem[2]);
                    insertData(tableName,parameterMap,true);
                    //记录插入数量
                    StringBuilder writeToFile = new StringBuilder();
                    writeToFile.append("日期:").append(data).append("               ").
                            append("收盘价:").append(trade).append("               ").
                            append(name);

                    fw.write(writeToFile.toString(),0,writeToFile.toString().length());
                    //有利于清理掉json这个大对象
                    jsonO=null;
                    fw.newLine();
                    fw.flush();
                }
            }
            insertData(null,null,true);
            //向share表插入每个股票数据
            Map<String, Object> parameterMap = new HashMap<String,Object>();
            parameterMap.put("nowPrice",onetrade);
            parameterMap.put("code",code);
            parameterMap.put("hangyecode",hang_url);
            parameterMap.put("name",name);
            insertData("shares",parameterMap,false);
            System.out.println("插入了"+String.valueOf(count)+"条数据");
        }
    }

    private static int getPageNum(String url,boolean isHistory) {
        if (isHistory){
            Map<String,String> header = new HashMap<String,String>();
            header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            Document document = getDocument(url,header);
            String count = document.body().text();
            System.out.println(count);
            if ("null".equals(count)||"".equals(count)||count==null) {
                return 0;
            };
            //放回数据为的格式是  new String("22")，所以需要截取中间数字
            count = count.substring(count.indexOf("\"")+1,count.lastIndexOf("\""));
            int yu = Integer.parseInt(count)%2500;
            int pageNum = Integer.parseInt(count)/2500;
            return pageNum = yu == 0? pageNum: pageNum+1;
        }else {
            Map<String,String> header = new HashMap<String,String>();
            header.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            Document document = getDocument(url,header);
            String count = document.body().text();
            System.out.println(count);
            if ("null".equals(count)||"".equals(count)||count==null) {
                return 0;
            };
            //放回数据为的格式是  new String("22")，所以需要截取中间数字
            count = count.substring(count.indexOf("\"")+1,count.lastIndexOf("\""));
            int yu = Integer.parseInt(count)%40;
            int pageNum = Integer.parseInt(count)/40;
            return pageNum = yu == 0? pageNum: pageNum+1;
        }

    }

    private static Document getDocument(String url, Map<String,String> header) {
//        setIpProxy();
        boolean b = false;
        Connection connect = Jsoup.connect(url).timeout(2000);
        connect.headers(header);
        Document document = null;
        while (!b) {
            try {
                document = connect.get();
                b = true;
            } catch (IOException e) {
//                setIpProxy();
                System.out.println("重新连接中接口"+url);
            }
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("访问成功"+url);
        return document;
    }

    private static void setIpProxy() {
        IpAdd ip = ipAdds.get(new Random().nextInt(10));
        System.getProperties().setProperty("proxySet", "true");
        System.setProperty("http.proxyHost",ip.getHost());
        System.setProperty("http.proxyPort",ip.getPort());
    }
}
