package org.server.service;

import org.server.enums.UploadType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathService {


  @Value("${tmp.upload.file-pth}")
  public String localUploadFilePath;

  public String getPackLocalUploadFilePath(String userId, String username, String md5,
      UploadType uploadType) {
    return localUploadFilePath + "/" + userId + "_" + username + "/" + uploadType.code + "/" + md5;
  }



}
