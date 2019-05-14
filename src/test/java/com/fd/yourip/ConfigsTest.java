package com.fd.yourip;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigsTest {
    
    @Test
    public void testLoads() {
        System.setProperty("yourip.configs.bindAddr", "localhost");
        System.setProperty("yourip.configs.bindPort", "8181");
        System.setProperty("yourip.configs.https", "true");
        System.setProperty("yourip.configs.coreSize", "2");
        System.setProperty("yourip.configs.workSize", "3");
        System.setProperty("yourip.configs.sslCrtPath", "crt.crt");
        System.setProperty("yourip.configs.sslKeyPath", "crt.key");
        Configs.INSTANCE.loads();
        assertEquals("localhost", Configs.INSTANCE.bindAddr());
        assertEquals(8181, Configs.INSTANCE.bindPort());
        assertEquals(true, Configs.INSTANCE.https());
        assertEquals(2, Configs.INSTANCE.coreSize());
        assertEquals(3, Configs.INSTANCE.workSize());
        assertEquals("crt.key", Configs.INSTANCE.sslKeyPath());
        assertEquals("crt.crt", Configs.INSTANCE.sslCrtPath());
    }

    @Test
    public void testToString() {
        Configs.INSTANCE.loads();
        System.out.println(Configs.INSTANCE.toString());
    }
}
