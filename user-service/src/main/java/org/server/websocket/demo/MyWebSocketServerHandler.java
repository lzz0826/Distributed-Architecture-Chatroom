package org.server.websocket.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

public class MyWebSocketServerHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    // 在建立连接时调用，可以执行一些初始化操作
    System.out.println("WebSocket连接已建立");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    // 在连接断开时调用，可以执行资源释放等清理操作
    System.out.println("WebSocket连接已断开");
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
    if (frame instanceof TextWebSocketFrame) {
      // 处理收到的文本帧
      String request = ((TextWebSocketFrame) frame).text();
      System.out.println("收到消息: " + request);

      // 做一些业务逻辑处理...

      // 发送响应
      String response = "收到: " + request;
      ctx.channel().writeAndFlush(new TextWebSocketFrame(response));
    } else {
      // 处理其他类型的帧
      System.out.println("收到其他类型的帧: " + frame.getClass().getSimpleName());
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // 异常处理，可以记录日志或进行其他操作
    System.err.println("WebSocket处理发生异常: " + cause.getMessage());
    ctx.close();
  }
}
