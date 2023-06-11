package org.server.common;

public enum StatusCode {

 /**
 * 系統
 */
  Success(0,"成功"),

  SystemError(-1,"失敗"),

  /**
   * User 1000
   */
  NotFoundUser(1000,"找不到使用者"),

  LoginError(10001,"登入失敗"),


  /**
   * Order 2000
   */

  NotFoundOrder(2000,"找不到訂單"),


  /**
   * WS 9000
   */

  NeedToken(9000,"請填入token或token錯誤"),




  ;

  public final int code;

  public final String msg;


  StatusCode(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static StatusCode getByCode(int code) {
    for (StatusCode e : StatusCode.values()) {
      if (e.code == code) {
        return e;
      }
    }

    return null;
  }




}
