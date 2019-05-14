package com.fd.yourip;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    
    private final SslContext sslContext;
    
    public SocketChannelInitializer(final SslContext sslContext) {
        this.sslContext = sslContext;
    }
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (sslContext != null) {
            ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
        }
        pipeline.addLast(new ReadTimeoutHandler(2, TimeUnit.SECONDS));
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new IpAddressHandler());
    }
}
