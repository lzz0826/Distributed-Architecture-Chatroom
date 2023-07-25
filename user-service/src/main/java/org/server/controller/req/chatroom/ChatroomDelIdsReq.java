package org.server.controller.req.chatroom;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatroomDelIdsReq {

  @ApiModelProperty(value="id(*必須,List不能含有空字串)")
  List<String> ids ;

}
