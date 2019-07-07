package com.hand.sharesinfo.datadownload;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyIP {
    public List<IpAdd> getIpAddrs(int num)throws IOException {
        List<IpAdd> ipAdds = new ArrayList<IpAdd>();
        String url = "https://www.xicidaili.com/nn/";
        int page = 1;
        while (ipAdds.size()<100){
            System.out.println(page);
            String newurl = url+String.valueOf(page);
            getProxyIpPool(ipAdds,newurl);
            page++;
        }
        return ipAdds;
    }

    private void getProxyIpPool(List<IpAdd> ipAdds,String url){
        boolean contimeout = false;
        Connection connection = Jsoup.connect(url).timeout(3000);
        Document document = null;
        Map<String,String> head_map = new HashMap<String,String>();
        head_map.put("Cache-Control","max-age=0");
        head_map.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        head_map.put("Accept-Encoding","gzip, deflate, br");
        head_map.put("Accept-Language","zh-CN,zh;q=0.9");
        head_map.put("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        connection.headers(head_map);
        while (!contimeout){
            try {
                document = connection.get();
                contimeout = true;
            } catch (IOException e) {
                System.out.println("重新连接获取代理网站");
            }
        }

        List<Node> nodes = document.getElementById("ip_list").child(0).childNodes();
        boolean flag = false;
        int  i = 0;
        for (Node node:nodes){
            if (!flag){
                flag=true;
                continue;
            }
            i++;
            if (i%2==1){
                continue;
            }
            String hh = ((Element)node.childNode(11)).text();

            if (hh.equalsIgnoreCase("http")) {
                if (ipAdds.size()==100){
                    break;
                }
                IpAdd ii = new IpAdd(((Element)node.childNode(5)).text(), ((Element)node.childNode(3)).text(), hh);
                boolean b = checkIp(ii);
                if (b){
                    System.out.println(ii.getHost()+":"+ii.getPort()+"可用");
                    ipAdds.add(ii);
                }
            }
        }
    }

    private boolean checkIp(IpAdd ii) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress(ii.getHost(),Integer.parseInt(ii.getPort())));
        HttpURLConnection connection = null;
        Document document = null;
        try {
            connection = (HttpURLConnection)new URL("http://200019.ip138.com/").openConnection(proxy);
            connection.setConnectTimeout(500);
            //有点问题  要解决  暂时不知道
            connection.connect();
            document = Jsoup.parse(connection.getInputStream(),connection.getContentEncoding(),connection.getURL().getHost());
        } catch (IOException e) {
            System.out.println(ii.getHost()+":"+ii.getPort()+" 不可用");
            return false;
        }
        System.out.println(document.body().text());
        return true;
    }
}
