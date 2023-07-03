package org.server.controller.req.chatroomRecord;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelSilenceCacheReq {

  private String userId;

}
