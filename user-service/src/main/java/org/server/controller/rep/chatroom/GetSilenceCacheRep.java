package org.server.controller.rep.chatroom;


import io.swagger.annotations.ApiModelProperty;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.server.dao.ChatSilenceCacheDAO;
import org.server.vo.ChatSilenceCacheVO;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSilenceCacheRep {


  @ApiModelProperty(value="靜言聊天室詳細訊息 Key 聊天室id")
  private Map<String, ChatSilenceCacheVO> silenceChatroomDatas;



}
