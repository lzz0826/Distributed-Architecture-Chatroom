package org.server.controller.rep.chatroomRecord;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.server.controller.rep.BasePageRep;
import org.server.vo.ChatroomRecordVO;

@Builder
@Data
@EqualsAndHashCode(callSuper = true)
public class ListRep extends BasePageRep {

  @ApiModelProperty(value="聊天記錄 List)")
  List<ChatroomRecordVO> chatroomRecords;
}
