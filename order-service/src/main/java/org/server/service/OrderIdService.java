package org.server.service;

import javax.annotation.Resource;
import org.server.sercice.IdGeneratorService;
import org.springframework.stereotype.Service;

@Service
public class OrderIdService {

  @Resource
  private IdGeneratorService idGeneratorService;


  private static final String  withdrawIdPrefix = "w";



  public String getWithdrawId(){
    String nextId = idGeneratorService.getNextId();
    return withdrawIdPrefix+ nextId;

  }

}
