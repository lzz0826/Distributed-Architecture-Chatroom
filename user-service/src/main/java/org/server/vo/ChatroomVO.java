package org.server.vo;


import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ChatroomVO {

  @ApiModelProperty(value="聊天室id")
  private String id;

  @ApiModelProperty(value="聊天室名稱")
  private String name;

  @ApiModelProperty(value="聊天室管理員Id")
  private String adminUserId;

  @ApiModelProperty(value="聊天室狀態")
  private Boolean status;

  @ApiModelProperty(value="最後更新時間")
  private Date updateTime;

  @ApiModelProperty(value="創建時間")
  private Date createTime;



}
