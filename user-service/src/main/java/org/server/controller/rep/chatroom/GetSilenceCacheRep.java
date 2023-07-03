package org.server.controller.rep.chatroom;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSilenceCacheRep {

  private String userId;

  private String chatroomId;



}
