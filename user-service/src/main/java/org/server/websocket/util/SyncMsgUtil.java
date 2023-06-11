package org.server.websocket.util;

import org.server.common.StatusCode;
import org.server.websocket.entity.WsRep;
import org.server.websocket.enums.EMsgType;
import org.server.websocket.enums.EWsMsgType;


public class SyncMsgUtil {

    public static WsRep<Object> getWelcomeMsg() {
        return WsRep.builder()
                .statusCode(StatusCode.Success)
                .eWsMsgType(EWsMsgType.Chatroom)
                .response("Welcome")
                .eMsgType(EMsgType.System)
            .build();
    }

    public static WsRep<Object> getPongMsg() {
        return WsRep.builder()
                .statusCode(StatusCode.Success)
                .eWsMsgType(EWsMsgType.Chatroom)
                .response("pong")
                .eMsgType(EMsgType.HeartBeat)
            .build();
    }

    public static WsRep<Object> getNeedTokenMsg() {
        return WsRep.builder()
                .eWsMsgType(EWsMsgType.Chatroom)
                .statusCode(StatusCode.NeedToken)
                .eMsgType(EMsgType.System)
            .build();
    }
}
