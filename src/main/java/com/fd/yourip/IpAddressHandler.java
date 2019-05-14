package com.fd.yourip;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class IpAddressHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final Logger LOG = LoggerFactory.getLogger(IpAddressHandler.class);
    
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        SocketAddress address = ctx.channel().remoteAddress();
        if (msg instanceof HttpRequest) {
            LOG.info("received {} request", address);
            String ip = "";
            int port = 0;
            if (address instanceof InetSocketAddress) {
                InetSocketAddress inet = InetSocketAddress.class.cast(address);
                ip  = inet.getHostString();
                port = inet.getPort();
            }
            ctx.write(buildResponse(ip, port)).addListener(ChannelFutureListener.CLOSE);
        }
        // ignore any content
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOG.error("channel[{}] occurs error", ctx.channel().remoteAddress(), cause);
        ctx.close();
    }
    
    private HttpResponse buildResponse(String ip, int port) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, 
            HttpResponseStatus.OK,
            Unpooled.wrappedBuffer(ip.getBytes()));
        response.headers().add(HttpHeaderNames.CONNECTION, "close")
            .add(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=utf-8");
        return response;
    }
}
