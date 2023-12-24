package org.server.aop;


import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Log4j2
public class LogAop {

  @Before(value = "execution(* org.server.controller.*Controller.*(..))")
  public void requestLog(JoinPoint joinPoint) {
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("[Request {}] 方法名:{} 傳入參數:{} ",className,methodName,args);
  }


  @AfterReturning(value = "execution(* org.server.controller.*Controller.*(..))",returning = "result")
  public void responseLog(JoinPoint joinPoint,Object result) {
    String className = joinPoint.getTarget().getClass().getName();
    String methodName = joinPoint.getSignature().getName();
    log.info("[Response {}] 方法名:{} 傳出參數:{} ",className,methodName,result);
  }

}
