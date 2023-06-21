package org.server.controller.rep.chatroom;


import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.server.controller.rep.BasePageRep;
import org.server.vo.ChatroomVO;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class ListRep extends BasePageRep {

  List<ChatroomVO> chatrooms;


}
