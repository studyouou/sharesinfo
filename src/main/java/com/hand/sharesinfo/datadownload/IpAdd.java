package com.hand.sharesinfo.datadownload;

public class IpAdd {
    private String port;
    private String host;
    private String prof;

    public IpAdd(String port, String host, String prof) {
        this.port = port;
        this.host = host;
        this.prof = prof;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }

    @Override
    public String toString() {
        return "IpAdd{" +
                "port='" + port + '\'' +
                ", host='" + host + '\'' +
                ", prof='" + prof + '\'' +
                '}';
    }
}
