package org.server.controller.req;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UploadAvatarReq {

  @ApiModelProperty(value="userId(*必須)")
  private String userId;

  @ApiModelProperty(value="圖片jpg.png(*必須)")
  private MultipartFile file;




}
