package org.server.websocket.demo;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;


public class SocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
    // 打印接收到的消息
    System.out.println("服务端接受到的消息:" + textWebSocketFrame.text());
    // 返回消息给客户端
    channelHandlerContext.writeAndFlush(new TextWebSocketFrame("服务器时间: " + LocalDateTime.now() + "  ： " + textWebSocketFrame.text()));

    ChannelId id = channelHandlerContext.channel().id();
    System.out.println(id);


  }

  /**
   * 客户端连接的时候触发
   * @param ctx
   * @throws Exception
   */
  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    // LongText() 唯一的  ShortText() 不唯一
    System.out.println("handlerAdded：" + ctx.channel().id().asLongText());
    System.out.println("handlerAdded：" + ctx.channel().id().asShortText());
  }

  /**
   * 客户端断开连接的时候触发
   * @param ctx
   * @throws Exception
   */
  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    System.out.println("handlerRemoved：" + ctx.channel().id().asLongText());
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println("异常发生了...");
    ctx.close();
  }
}
