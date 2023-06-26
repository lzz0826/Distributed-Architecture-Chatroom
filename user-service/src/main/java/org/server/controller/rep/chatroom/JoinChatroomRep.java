package org.server.controller.rep.chatroom;


import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JoinChatroomRep {


  private String chatroomId;
  private String userId;
  private String adminUserId;
  private Boolean status;


}
