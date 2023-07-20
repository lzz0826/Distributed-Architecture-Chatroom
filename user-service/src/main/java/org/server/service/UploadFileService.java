package org.server.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import javax.annotation.Resource;
import org.server.dao.UserDAO;
import org.server.enums.UploadType;
import org.server.exception.UpdateUserFailException;
import org.springframework.stereotype.Service;

@Service
public class UploadFileService {



  @Resource
  private PathService pathService;
  @Resource
  private UserService userService;

  public String uploadFile(UserDAO user,String fileName,String fileMd5,byte[] fileBytes ,
      UploadType uploadType) throws IOException {
    String userId = user.getId();
    String username = user.getUsername();
    String packLocalUploadPath = pathService.getPackLocalUploadFilePath(userId,username,fileMd5,uploadType);
    File filePath = new File(packLocalUploadPath);
    File file = new File(filePath, fileName);
    if (!filePath.exists()) {
      Files.createDirectories(filePath.toPath());
    }
    FileOutputStream fileOutputStream = null;
    try {
      fileOutputStream = new FileOutputStream(file);
      fileOutputStream.write(fileBytes);
    }finally {
      if(fileOutputStream == null){
        fileOutputStream.close();
      }
    }
    return file.getPath();
  }

  public String updateAvatar(UserDAO user,String fileName,String fileMd5,byte[] fileBytes)
      throws IOException, UpdateUserFailException {
    String finalPath = uploadFile(user,fileName,fileMd5,fileBytes,UploadType.Avatar);

    UserDAO dao = UserDAO
        .builder()
        .id(user.getId())
        .avatarPth(finalPath)
        .updateTime(new Date())
        .build();


    int i = userService.updateUser(dao);
    if(i == 0){
      throw new UpdateUserFailException();
    }
    return finalPath;
  }



}
