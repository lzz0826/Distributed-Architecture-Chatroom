package org.server.withdraw.been;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.server.mapper.BankCodeMapper;
import org.server.withdraw.WithdrawBank;
import org.server.withdraw.exampleBank.ExampleBank;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class WithdrawChannelPool implements InitializingBean, ApplicationContextAware {

  private ApplicationContext applicationContext;

  private List<WithdrawBank> withdrawBankList = new ArrayList<>();


  // ApplicationContextAware接口方法的實作，用於保存應用上下文的引用
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * 將WithdrawBank實現類都放進list
   */
  @Override
  public void afterPropertiesSet() throws Exception {

    // 從應用上下文中獲取所有WithdrawBank bean 並加入列表中
    withdrawBankList = new ArrayList<>(applicationContext.getBeansOfType(WithdrawBank.class).values());
    for (WithdrawBank withdrawBank : withdrawBankList) {
      log.info("Add WithdrawBank: {} " , withdrawBank.getClass().getSimpleName());
    }
  }

  private WithdrawBank getWithdrawBank(String catchId){

    log.info("print withdrawMethodList size={}, catchId: {}", withdrawBankList.size(), catchId);
    for (WithdrawBank withdrawBank : withdrawBankList) {
      if(withdrawBank.canSupport(catchId)){
        log.info("print withdrawMethodList content={}", withdrawBank);
        return withdrawBank;
      }
    }
    return null;
  }
}
