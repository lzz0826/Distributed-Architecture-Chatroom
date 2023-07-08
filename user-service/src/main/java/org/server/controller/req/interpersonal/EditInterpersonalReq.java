package org.server.controller.req.interpersonal;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EditInterpersonalReq {


  private String userId;

  private List<String> blacklist;

  private List<String> blacklisted;

  private List<String> banChatRoom;

}
