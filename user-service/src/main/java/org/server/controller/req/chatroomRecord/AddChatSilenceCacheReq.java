package org.server.controller.req.chatroomRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddChatSilenceCacheReq {

  private String userId;

  private String chatroomId ;

  private Integer timeout;

}
