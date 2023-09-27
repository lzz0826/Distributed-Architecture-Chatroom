package org.server.websocket;


import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.log4j.Log4j2;

import org.server.entity.CustomUserDetails;
import org.server.service.JwtCacheService;

import org.server.util.FastJsonUtil;
import org.server.util.SpringUtil;
import org.server.websocket.entity.WsRep;
import org.server.websocket.mpa.WsChatRoomMap;
import org.server.websocket.mpa.WsChnIdCtxMap;
import org.server.websocket.mpa.WsChnIdUserIdMap;
import org.server.websocket.mpa.WsUserIdChnIdMap;
import org.server.websocket.util.SyncMsgUtil;


@Log4j2
public class OnlineWebSocketHandler extends OnlineWebSocketChatBasic {


  public OnlineWebSocketHandler() {}


  private final JwtCacheService jwtCacheService = SpringUtil.getBean(JwtCacheService.class);



  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    log.info("與客戶端建立連接，通道開啟！ ctxId: {}", ctx.channel().id());
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) {
    ChannelId chnId = ctx.channel().id();
    String userId = WsChnIdUserIdMap.get(chnId);
    log.info("與客戶端斷開连接，通道關閉！ chnId = {}, userId = {}", chnId, userId);
    WsChnIdUserIdMap.del(chnId);
    if (userId != null) {
      WsUserIdChnIdMap.del(userId);
      WsChatRoomMap.removeUserChatRoomAll(userId);
    }
    WsChnIdCtxMap.del(chnId);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    boolean checkIsLogin = true;
    // 首次連接是FullHttpRequest，把用戶id和對應的channel對象存儲起来
    if (msg instanceof FullHttpRequest) {
      FullHttpRequest request = (FullHttpRequest) msg;
      CustomUserDetails user = getUserDetailsByRequest(request);
      if (user == null) {
        //jwt裡沒有token代表沒登入
        checkIsLogin = false;
      } else {
        String userId = user.getId();
        ChannelId chnId = ctx.channel().id();
        WsUserIdChnIdMap.put(userId, chnId);
        WsChnIdUserIdMap.put(chnId, userId);
        WsChnIdCtxMap.put(chnId, ctx.channel());
      }
    } else if (msg instanceof TextWebSocketFrame) {
      TextWebSocketFrame frame = (TextWebSocketFrame) msg;
      String text = frame.text();
      // 正常的text消息類型
      log.info("客戶端收到服務器數據：{}", text);
      //聊天室
      setChatroomMq(text,ctx);
    }
    super.channelRead(ctx, msg);
    if (msg instanceof FullHttpRequest) {
      if (!checkIsLogin) {
        sendMsgByCtx(ctx, SyncMsgUtil.getNeedTokenMsg());
        ctx.channel().close();
        return;
      }
      sendMsgByCtx(ctx, SyncMsgUtil.getWelcomeMsg());
    } else if (msg instanceof TextWebSocketFrame) {
      sendMsgByCtx(ctx, SyncMsgUtil.getPongMsg());
    }
  }

  public static void checkChannelId(ChannelId channelId, WsRep<?> rep) {
    if (channelId != null) {
      Channel ct = WsChnIdCtxMap.get(channelId);
      if (ct != null) {
        sendMsgByChannel(ct, rep);
      } else {
        log.error("無法傳送信息，找不到Chn, chnId = {} 找不到對應頻道", channelId);
      }
    } else {
      log.error("無法傳送信息，找不到Chn, chnId為空");
    }
  }


  /**
   * 解析url中的參數
   *
   * @return 獲取參數
   */
  private Map<String, String> getLoginInfo(String url) {
    if (!url.contains("?")) {
      return null;
    }
    String query = url.substring(url.indexOf("?") + 1);

    String[] params = query.split("&");
    Map<String, String> map = new HashMap<>();

    for (String param : params) {
      String name = param.split("=")[0];
      String value = param.split("=")[1];
      map.put(name, value);
    }

    return map;
  }

  private CustomUserDetails getUserDetailsByRequest(FullHttpRequest request) {
    String uri = request.uri();

    if (uri.contains("?")) {
      String newUri = uri.substring(0, uri.indexOf("?"));
      request.setUri(newUri);

      Map<String, String> paramMap = getLoginInfo(uri);
      if (paramMap == null || !paramMap.containsKey("token")) {
        return null;
      }

      String jwtToken = paramMap.get("token");
      return jwtCacheService.getUserByJwtToken(jwtToken);
    } else {
      return null;
    }
  }

  private static void sendMsgByChannel(Channel ct, WsRep<?> rep) {
    String msg = JSONObject.toJSONString(rep, FastJsonUtil.getCommonSerializeConfig());
    ct.writeAndFlush(new TextWebSocketFrame(msg));
  }

  private static void sendMsgByCtx(ChannelHandlerContext ctx, WsRep<?> rep) {
    String msg = JSONObject.toJSONString(rep, FastJsonUtil.getCommonSerializeConfig());
    ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
  }

}