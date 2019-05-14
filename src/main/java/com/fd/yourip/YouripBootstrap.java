package com.fd.yourip;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Entrance
 * <p>
 * all configs are set by jvm system properties(-D)
 * </p>
 * all defaults
 * <ul>
 * <li>yourip.configs.bindAddr=0.0.0.0</li>
 * <li>yourip.configs.bindPort=80</li>
 * <li>yourip.configs.https=false</li>
 * <li>yourip.configs.coreSize=1</li>
 * <li>yourip.configs.workSize=1</li>
 * <li>yourip.configs.sslCrtPath=null</li>
 * <li>yourip.configs.sslKeyPath=null</li>
 * </ul>
 * 
 * @author anexplore
 *
 */
public class YouripBootstrap {
    private static final Logger LOG = LoggerFactory.getLogger(YouripBootstrap.class);
    private EventLoopGroup bossGroup;
    private EventLoopGroup workGroup;

    public YouripBootstrap() {}

    public void startup() throws Exception {
        LOG.info("load all confis from properties");
        Configs.INSTANCE.loads();
        LOG.info("service will start with config:\n{}", Configs.INSTANCE.toString());
        Configs config = Configs.INSTANCE;
        SslContext sslContext = null;
        // build ssl context
        if (config.https()) {
            LOG.info("start to prepare https certifactes");
            if (config.sslCrtPath() != null && config.sslKeyPath() != null) {
                LOG.info("use crt: {}, key: {}", config.sslCrtPath(), config.sslCrtPath());
                sslContext =
                    SslContextBuilder.forServer(new File(config.sslCrtPath()), new File(config.sslKeyPath())).build();
            } else {
                LOG.info("use self-signed certiface");
                SelfSignedCertificate ssc = new SelfSignedCertificate();
                sslContext = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            }
        }
        // build server
        bossGroup = new NioEventLoopGroup(config.coreSize());
        workGroup = new NioEventLoopGroup(config.workSize());
        ServerBootstrap server = new ServerBootstrap();
        server.option(ChannelOption.SO_BACKLOG, 128)
            .option(ChannelOption.SO_REUSEADDR, true)
            .group(bossGroup, workGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new SocketChannelInitializer(sslContext))
            .childOption(ChannelOption.TCP_NODELAY, true);
        server.bind(config.bindAddr(), config.bindPort()).sync();
        LOG.info("service starts on {}://{}:{}/", config.https() ? "https" : "http", config.bindAddr(),
            config.bindPort());
    }

    public void shutdown() {
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workGroup != null) {
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        YouripBootstrap bootstrap = new YouripBootstrap();
        bootstrap.startup();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                bootstrap.shutdown();
            }
        });
    }

}
