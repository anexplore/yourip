package com.fd.yourip;

public enum Configs {
    INSTANCE;
    
    private final String PRO_PREFIX = "yourip.configs.";
    
    private boolean https;
    private String bindAddr = "0.0.0.0";
    private int bindPort = 80;
    private int coreSize = 1;
    private int workSize = 1;
    
    /*ssl crt and key*/
    private String sslCrtPath;
    private String sslKeyPath;
    
    
    public synchronized void loads() {
        https = Boolean.parseBoolean(System.getProperty(PRO_PREFIX + "https", "false"));
        bindAddr = System.getProperty(PRO_PREFIX + "bindAddr", "0.0.0.0");
        bindPort = Integer.parseInt(System.getProperty(PRO_PREFIX + "bindPort", "80"));
        coreSize = Integer.parseInt(System.getProperty(PRO_PREFIX + "coreSize", "1"));
        workSize = Integer.parseInt(System.getProperty(PRO_PREFIX + "workSize", "1"));
        sslCrtPath = System.getProperty(PRO_PREFIX + "sslCrtPath");
        sslKeyPath = System.getProperty(PRO_PREFIX + "sslKeyPath");
    }
    
    public synchronized boolean https() {
        return https;
    }
    
    public synchronized String bindAddr() {
        return bindAddr;
    }
    
    public synchronized int bindPort() {
        return bindPort;
    }
    
    public synchronized int coreSize() {
        return coreSize;
    }
    
    public synchronized int workSize() {
        return workSize;
    }
    
    public synchronized String sslCrtPath() {
        return sslCrtPath;
    } 
    
    public synchronized String sslKeyPath() {
        return sslKeyPath;
    }
    
    public synchronized String toString() {
        String builder = "bindAddr:" + bindAddr;
        builder += "\nbindPort:" + bindPort;
        builder += "\nhttps:" + https;
        builder += "\ncoreSize:" + coreSize;
        builder += "\nworkSize:" + workSize;
        builder += "\nsslCrtPath:" + sslCrtPath;
        builder += "\nsslKeyPath:" + sslKeyPath;
        return builder;
    }
}
