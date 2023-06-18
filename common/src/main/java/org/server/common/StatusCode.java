package org.server.common;

public enum StatusCode {

 /**
 * 系統
 */
  Success(0,"成功"),

  SystemError(-1,"失敗"),

  MissingParameter(10,"缺少必要參數"),

  /**
   * User 1000
   */
  NotFoundUser(1000,"找不到使用者"),
  LoginError(1001,"登入失敗"),

  NeedUserName(1002,"需要UserName"),

  NeedPassword(1003,"需要Password"),

  AddUserFail(1003,"新增失敗"),



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
