//package org.server.websocket;
//
//import com.alibaba.fastjson.JSONObject;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelId;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.handler.codec.http.FullHttpRequest;
//import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
//import java.util.HashMap;
//import java.util.Map;
//import lombok.extern.log4j.Log4j2;
//import org.server.component.SpringUtil;
//import org.server.domain.admin.AdminDAO;
//import org.server.entity.auth.AdminDetails;
//import org.server.entity.ws.WsRep;
//import org.server.service.JwtCacheService;
//import org.server.service.SyncMsgUtil;
//import org.server.service.admin.AdminService;
//import org.server.util.FastJsonUtil;
//import org.server.websocket.map.WsAdminChnIdMap;
//import org.server.websocket.map.WsChnIdAdminMap;
//import org.server.websocket.map.WsChnIdCtxMap;
//
//
//@Log4j2
//public class OnlineWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
//
//    private final AdminService adminService = SpringUtil.getBean(AdminService.class);
//
//    private final JwtCacheService jwtCacheService = SpringUtil.getBean(JwtCacheService.class);
//
//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        log.info("與客戶端建立連接，通道開啟！ ctxId: {}", ctx.channel().id());
//    }
//
//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) {
//        ChannelId chnId = ctx.channel().id();
//        String adminId = WsChnIdAdminMap.get(chnId);
//
//        log.info("與客戶端斷開连接，通道關閉！ chnId = {}, adminId = {}", chnId, adminId);
//
//        WsChnIdAdminMap.del(chnId);
//
//        if (adminId != null) {
//            WsAdminChnIdMap.del(adminId);
//        }
//
//        WsChnIdCtxMap.del(chnId);
//    }
//
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        boolean checkIsLogin = true;
//
//        // 首次連接是FullHttpRequest，把用戶id和對應的channel對象存儲起来
//        if (msg instanceof FullHttpRequest request) {
//            AdminDetails adminDetails = getUserDetailsByRequest(request);
//            if (adminDetails == null) {
//                checkIsLogin = false;
//            } else {
//                AdminDAO admin = adminService.getByUsernameChnId(adminDetails.getUsername(), adminDetails.getChannelId());
//                String adminId = admin.getId();
//                ChannelId chnId = ctx.channel().id();
//
//                WsAdminChnIdMap.put(adminId, chnId);
//                WsChnIdAdminMap.put(chnId, adminId);
//                WsChnIdCtxMap.put(chnId, ctx.channel());
//            }
//        } else if (msg instanceof TextWebSocketFrame frame) {
//            // 正常的text消息類型
//            log.info("客戶端收到服務器數據：{}", frame.text());
//        }
//
//        super.channelRead(ctx, msg);
//
//        if (msg instanceof FullHttpRequest) {
//            if (!checkIsLogin) {
//                sendMsgByCtx(ctx, SyncMsgUtil.getNeedTokenMsg());
//                ctx.channel().close();
//
//                return;
//            }
//            sendMsgByCtx(ctx, SyncMsgUtil.getWelcomeMsg());
//        } if (msg instanceof TextWebSocketFrame) {
//            sendMsgByCtx(ctx, SyncMsgUtil.getPongMsg());
//        }
//    }
//
//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
//    }
//
//    /**
//     * 推送信息
//     * @param rep
//     */
//    public static void sendMsgToAdmin(WsRep<?> rep) {
//        String adminId = rep.getAdminId();
//        if (adminId == null) {
//            log.error("無法傳送信息，admin為空");
//            return;
//        }
//        ChannelId channelId = WsAdminChnIdMap.get(rep.getAdminId());
//        if (channelId != null) {
//            Channel ct = WsChnIdCtxMap.get(channelId);
//            if (ct != null) {
//                sendMsgByChannel(ct, rep);
//            } else {
//                log.error("無法傳送信息，找不到Chn, chnId = {} 找不到對應頻道", channelId);
//            }
//        } else {
//            log.error("無法傳送信息，找不到Chn, chnId為空");
//        }
//    }
//
//    /**
//     * 解析url中的參數
//     * @return 獲取參數
//     */
//    private Map<String, String> getLoginInfo(String url) {
//        if (!url.contains("?")) {
//            return null;
//        }
//        String query = url.substring(url.indexOf("?") + 1);
//
//        String[] params = query.split("&");
//        Map<String, String> map = new HashMap<>();
//
//        for (String param : params) {
//            String name = param.split("=")[0];
//            String value = param.split("=")[1];
//            map.put(name, value);
//        }
//
//        return map;
//    }
//
//    private AdminDetails getUserDetailsByRequest(FullHttpRequest request) {
//        String uri = request.uri();
//
//        if (uri.contains("?")) {
//            String newUri = uri.substring(0, uri.indexOf("?"));
//            request.setUri(newUri);
//
//            Map<String, String> paramMap = getLoginInfo(uri);
//            if (paramMap == null || !paramMap.containsKey("token")) {
//                return null;
//            }
//
//            String jwtToken = paramMap.get("token");
//            return jwtCacheService.getAdminDetailsByJwtToken(jwtToken);
//        } else {
//            return null;
//        }
//    }
//
//    private static void sendMsgByChannel(Channel ct, WsRep<?> rep) {
//        String msg = JSONObject.toJSONString(rep, FastJsonUtil.getCommonSerializeConfig());
//        ct.writeAndFlush(new TextWebSocketFrame(msg));
//    }
//
//    private static void sendMsgByCtx(ChannelHandlerContext ctx, WsRep<?> rep) {
//        String msg = JSONObject.toJSONString(rep, FastJsonUtil.getCommonSerializeConfig());
//        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
//    }
//}