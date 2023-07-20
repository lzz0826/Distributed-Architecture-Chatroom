package org.server.controller;


import static org.server.util.FileUtil.getExtByFileName;
import static org.server.util.FileUtil.supportedComImages;
import static org.server.util.FileUtil.supportedCompZip;

import io.swagger.annotations.Api;
import java.io.IOException;
import java.nio.file.Files;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.UploadImageReq;
import org.server.dao.UserDAO;
import org.server.enums.UploadType;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotFoundUserAvatarPthException;
import org.server.exception.NotFoundUserException;
import org.server.exception.UpdateUserFailException;
import org.server.service.UploadFileService;
import org.server.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(tags = "上傳檔案相關API")
@RestController
@RequestMapping("/upload")
public class UploadFileController extends BaseController {

  @Value("${tmp.upload.file-pth}")
  private String filePth;

  @Resource
  private UploadFileService uploadFileService;
  @Resource
  private UserService userService;

  @PostMapping("/uploadAvatar")
  public BaseResp<String> uploadImage(UploadImageReq req)
      throws MissingParameterErrorException, IOException, UpdateUserFailException {
    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }
    if(req.getFile() == null || req.getFile().isEmpty()){
      throw new MissingParameterErrorException();
    }
    String userId = req.getUserId();
    MultipartFile file = req.getFile();

    String fileName = file.getOriginalFilename();
    String extByFileName = getExtByFileName(fileName);
    if(!supportedComImages.contains(extByFileName)){
      return BaseResp.fail(StatusCode.NonSupportExt);
    }
    UserDAO userDAO = userService.getUserById(userId);
    if(userDAO == null ){
      return BaseResp.fail(StatusCode.NotFoundUser);
    }
    byte[] fileBytes = file.getBytes();
    String fileMd5 = DigestUtils.md5DigestAsHex(fileBytes);
    String result = uploadFileService.updateAvatar(userDAO, fileName, fileMd5, fileBytes);
    return BaseResp.ok(result);
  }


  @PostMapping("/file")
  public BaseResp<String> uploadFile(MultipartFile file) throws IOException {


    if (file == null || file.isEmpty()) {
      return BaseResp.fail(StatusCode.NeedFile);
    }
    String fileName = file.getOriginalFilename();
    String extension = getExtByFileName(fileName);
    if (!supportedCompZip.contains(extension)) {
      return BaseResp.fail(StatusCode.NonSupportExt);
    }
    UserDAO user = getCurUser();
    byte[] fileBytes = file.getBytes();
    String fileMd5 = DigestUtils.md5DigestAsHex(fileBytes);
    String result = uploadFileService.uploadFile(user, fileName, fileMd5, fileBytes, UploadType.File);
    return BaseResp.ok(result);
  }



  @GetMapping("/getAvatar")
  public ResponseEntity<ByteArrayResource> getAvatar(@RequestParam String userId)
      throws IOException, MissingParameterErrorException, NotFoundUserException, NotFoundUserAvatarPthException {

    if(StringUtils.isBlank(userId)){
      throw new MissingParameterErrorException();
    }
    UserDAO userDAO = userService.getUserById(userId);
    if(userDAO == null){
      throw new NotFoundUserException();
    }
    String avatarPth = userDAO.getAvatarPth();
    if(StringUtils.isBlank(avatarPth)){
      throw new NotFoundUserAvatarPthException();
    }


    Path imagePath = Paths.get(avatarPth);

    if (Files.exists(imagePath) && Files.isReadable(imagePath)) {
      byte[] imageBytes = Files.readAllBytes(imagePath);
      ByteArrayResource resource = new ByteArrayResource(imageBytes);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_PNG); // 或者根据图片类型设置 MediaType
      return ResponseEntity.ok()
          .headers(headers)
          .body(resource);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}


