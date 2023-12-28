package org.server.websocket;

import static org.server.websocket.OnlineWebSocketHandler.checkChannelId;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.server.common.StatusCode;
import org.server.dao.BlackListDAO;
import org.server.dao.ChatSilenceCacheDAO;
import org.server.mq.MsgMqSender;
import org.server.service.BlackListService;
import org.server.service.ChatRecordService;
import org.server.service.ChatSilenceCacheService;
import org.server.utils.SpringUtil;
import org.server.websocket.entity.WsRep;
import org.server.websocket.entity.WsReq;
import org.server.websocket.enums.EMsgType;
import org.server.websocket.enums.EWsMsgType;
import org.server.websocket.mpa.WsChatRoomMap;
import org.server.websocket.mpa.WsChnIdUserIdMap;
import org.server.websocket.mpa.WsUserIdChnIdMap;
import org.server.websocket.util.SyncMsgUtil;

@Log4j2
public class OnlineWebSocketChatBasic extends SimpleChannelInboundHandler<TextWebSocketFrame> {

  private final ChatRecordService chatRecordService = SpringUtil.getBean(ChatRecordService.class);

  private final ChatSilenceCacheService chatSilenceCacheService = SpringUtil.getBean(ChatSilenceCacheService.class);
  private final BlackListService blackListService = SpringUtil.getBean(BlackListService.class);
  private final MsgMqSender msgMqSender = SpringUtil.getBean(MsgMqSender.class);
  @Override
  protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
  }

  /**
   * MQ轉發(確保起多個服務,在不同的服務上都能收到)
   */
  public void setChatroomMq(WsReq<String> wsReq ,ChannelHandlerContext ctx) {
    ChannelId channelId = ctx.channel().id();
    String senderUserId = WsChnIdUserIdMap.get(channelId);
    wsReq.setSenderUserId(senderUserId);
    //MQ
    //TODO EMsgType需要處理空
    if(wsReq.getEWsMsgType() == null){
      System.out.println("EMsgType不能為空");
    }else {
      msgMqSender.send(wsReq);
    }
  }

  /**
   * MQ接收(確保起多個服務,在不同的服務上都能收到)
   */
  public void getChatroomMq(WsReq<String> wsReq) {
    String senderUserId = wsReq.getSenderUserId();
    //TODO 聊天室靜言已完成 私聊尚未
    setChatroom(wsReq, senderUserId);
  }


  /**
   * 供其他service調用
   */
  public void setChatroom(String userId , EWsMsgType eWsMsgType,String chatroomId,String receiverUserId
      ,String finalPath){
    String senderUserId = userId;
    WsReq<String> req = WsReq.<String>builder()
        .receiverUserId(receiverUserId)
        .chatroomId(chatroomId)
        .eWsMsgType(eWsMsgType)
        .eMsgType(EMsgType.System)
        .request(finalPath)
        .build();
    setChatroom(req,senderUserId);
  }

  /**
   * 聊天室主邏輯
   */
  private void setChatroom(WsReq<String> wsReq, String senderUserId) {
    String receiverUserId = wsReq.getReceiverUserId();
    String chatroomId = wsReq.getChatroomId();
    EMsgType eMsgType = wsReq.getEMsgType();
    EWsMsgType eWsMsgType = wsReq.getEWsMsgType();
    String request = wsReq.getRequest();

    System.out.println(wsReq.getReceiverUserId());

    WsRep<Object> wsRep = WsRep
        .builder()
        .receiverUserId(receiverUserId)
        .senderUserId(senderUserId)
        .chatroomId(chatroomId)
        .eMsgType(eMsgType)
        .eWsMsgType(eWsMsgType)
        .statusCode(StatusCode.Success)
        .response(request)
        .build();

    switch (eWsMsgType) {
      case Chatroom:
        System.out.println("聊天室");
        if(isSilenceCacheChatroom(senderUserId,chatroomId)){
          ChannelId setUserChannelId = WsUserIdChnIdMap.get(senderUserId);
          checkChannelId(setUserChannelId, SyncMsgUtil.isSilenceCache());
        }else {
          sendMsgToChatRoom(wsRep);
        }
        chatRecordService.addChatRecord(senderUserId, receiverUserId, chatroomId, request,
            EWsMsgType.Chatroom,EMsgType.App);
        break;
      case PrivateChat:
        System.out.println("私聊");
        sendMsgToUser(wsRep);
        chatRecordService.addChatRecord(senderUserId, receiverUserId, chatroomId, request,
            EWsMsgType.PrivateChat,EMsgType.App);
        break;
      case All:
        System.out.println("公告");
        sendMsgToAll(wsRep);
        chatRecordService.addChatRecord(senderUserId, receiverUserId, chatroomId, request,
            EWsMsgType.All,EMsgType.System);
        break;
      default:
        log.error("無法發送信息，其他錯誤");
        break;
    }
  }

  /**
   * 推送信息(私聊)
   *
   * @param rep
   */
  public void sendMsgToUser(WsRep<?> rep) {

    String receiverUserid = rep.getReceiverUserId();
    if (StringUtils.isBlank(receiverUserid)) {
      log.error("無法傳送信息，receiverUserId為空");
      return;
    }

    String senderUserId = rep.getSenderUserId();


    List<BlackListDAO> blackListDAOS = blackListService.findByBlacklist(senderUserId);
    if(blackListDAOS !=null && !blackListDAOS.isEmpty()){
      for (BlackListDAO blackListDAO : blackListDAOS) {
        String blackedUserId = blackListDAO.getUserId();
        if(blackedUserId.equals(receiverUserid)){
          ChannelId channelId = WsUserIdChnIdMap.get(senderUserId);
          log.error("被對方加入黑名單");
          rep.setEMsgType(EMsgType.System);
          rep.setStatusCode(StatusCode.Blacklisted);
          checkChannelId(channelId, rep);
          return;
        }
      }
    }
    ChannelId channelId = WsUserIdChnIdMap.get(receiverUserid);
    checkChannelId(channelId, rep);
  }


  /**
   * 推送信息(聊天室)
   *
   * @param rep
   */
  public static void sendMsgToChatRoom(WsRep<?> rep) {
    String chatroomId = rep.getChatroomId();
    if (StringUtils.isBlank(chatroomId)) {
      log.error("無法傳送信息，chatroomId為空");
      return;
    }
    Set<String> users = WsChatRoomMap.get(chatroomId);
    if (users != null) {
      for (String user : users) {
        ChannelId channelId = WsUserIdChnIdMap.get(user);
        try {
          checkChannelId(channelId, rep);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    }


  }

  /**
   * 推送信息(公告)
   *
   * @param rep
   */
  public static void sendMsgToAll(WsRep<?> rep) {
    List<ChannelId> channelIds = WsUserIdChnIdMap.getAll();
    if (channelIds.isEmpty() || channelIds.size() == 0) {
      log.error("無法傳送信息，聊天室裡沒人");
      return;
    }
    for (ChannelId channelId : channelIds) {
      checkChannelId(channelId, rep);

    }
  }

  /**
   * 檢查(是否禁言)
   *
   * @param userId
   */
  public Boolean isSilenceCacheChatroom(String userId, String chatroomId) {
    Map<String, ChatSilenceCacheDAO> chatroomIds = chatSilenceCacheService.getSilenceCacheByUserId(userId);
    if (chatroomIds.containsKey(chatroomId)) {
      ChatSilenceCacheDAO dao = chatroomIds.get(chatroomId);
      long timeout = dao.getTimeout();
      long currentTimeMillis = System.currentTimeMillis();
      return timeout > currentTimeMillis;
    }
    return false;
  }


}
