package org.server.vo;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatroomOnlineUserVO {

  @ApiModelProperty(value="聊天室id")
  public String chatroomId ;

  @ApiModelProperty(value="在線人員名單")
  public List<String> userIds;

}
