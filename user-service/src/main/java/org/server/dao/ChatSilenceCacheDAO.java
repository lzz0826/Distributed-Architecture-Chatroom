package org.server.dao;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatSilenceCacheDAO {

  private String userId;

  private String chatroomId ;

  private long timeout;
}
