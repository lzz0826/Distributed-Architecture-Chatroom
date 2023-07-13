package org.server.controller.rep.blackList;

import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.server.vo.BlackLisVO;

@Data
@Builder
public class ListRep {

  public List<BlackLisVO> blackLis;

}
