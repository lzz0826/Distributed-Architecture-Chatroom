package org.server.common;

public enum StatusCode {

 /**
 * 系統
 */
  Success(0,"成功"),

  SystemError(-1,"失敗"),

  MissingParameter(10,"缺少必要參數"),

  AddFail(11,"新增失敗"),

  NeedPage(12,"需要頁碼"),

  NeedPageSize(13,"需要頁碼大小"),



  /**
   * User 1000
   */
  NotFoundUser(1000,"找不到使用者"),
  LoginError(1001,"登入失敗"),

  NeedUserName(1002,"需要UserName"),

  NeedPassword(1003,"需要Password"),

  NeedLogin(1004,"需要登入"),



  /**
   * Order 2000
   */

  NotFoundOrder(2000,"找不到訂單"),



  /**
   * 權限 3000
   */
  AccessDenied(3000, "沒有權限"),


  /**
   * 聊天室 3000
   */

  NeedChatroomName(3000,"需要聊天室名稱"),

  NeedChatroomId(3001,"需要聊天室id"),

  NotFoundChatroom(3002,"找不到聊天室"),

  ChatroomNotOpen(3003,"聊天室沒開放"),


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
