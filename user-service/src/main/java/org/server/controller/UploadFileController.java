package org.server.controller;


import static org.server.utils.FileUtil.getExtByFileName;
import static org.server.utils.FileUtil.supportedComImages;
import static org.server.utils.FileUtil.supportedCompZip;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.nio.file.Files;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.server.common.BaseResp;
import org.server.common.StatusCode;
import org.server.controller.req.UploadAvatarReq;
import org.server.controller.req.chatroomRecord.UploadImageReq;
import org.server.dao.UserDAO;
import org.server.enums.UploadType;
import org.server.exception.MissingParameterErrorException;
import org.server.exception.NotFoundUserAvatarPthException;
import org.server.exception.NotFoundUserException;
import org.server.exception.UpdateUserFailException;
import org.server.service.UploadFileService;
import org.server.service.UserService;
import org.server.websocket.enums.EWsMsgType;
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
  @ApiOperation("上傳頭像")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> uploadAvatar(@ApiParam("上傳頭像請求") UploadAvatarReq req)
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
  @ApiOperation("上傳檔案")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> uploadFile(@ApiParam("File zip(*必須)") MultipartFile file) throws IOException {

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
  @ApiOperation("取得頭像")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public ResponseEntity<ByteArrayResource> getAvatar(@RequestParam @ApiParam("userId(*必須)") String userId)
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



  @PostMapping("/uploadImage")
  @ApiOperation("上傳聊天圖片")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public BaseResp<String> uploadImage(@ApiParam("上傳聊天圖片請求")UploadImageReq req)
      throws MissingParameterErrorException, IOException {
    if(StringUtils.isBlank(req.getUserId())){
      throw new MissingParameterErrorException();
    }
    if(req.getFile() == null || req.getFile().isEmpty()){
      throw new MissingParameterErrorException();
    }
    String userId = req.getUserId();
    MultipartFile file = req.getFile();

    if(req.getEWsMsgType() == null){
      throw new MissingParameterErrorException();
    }
    String chatroomId = null;
    String receiverUserId = null;
    EWsMsgType eWsMsgType = req.getEWsMsgType();
    if(eWsMsgType.code.equals("chatroom")){
      if(StringUtils.isBlank(req.getChatroomId())){
        throw new MissingParameterErrorException();
      }
      chatroomId = req.getChatroomId();
    }
    if(eWsMsgType.code.equals("privateChat")){
      if(StringUtils.isBlank(req.getReceiverUserId())){
        throw new MissingParameterErrorException();
      }
      receiverUserId = req.getReceiverUserId();
    }

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
    String result = uploadFileService.uploadImage(userDAO, fileName, fileMd5, fileBytes, eWsMsgType
    , chatroomId, receiverUserId);
    return BaseResp.ok(result);
  }



  @GetMapping("/getImage")
  @ApiOperation("取得聊天室貼圖")
  @ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true,
      allowEmptyValue = false, paramType = "header", dataTypeClass = String.class)
  public ResponseEntity<ByteArrayResource> getImage(@RequestParam @ApiParam("圖片路徑") String imagePth)
      throws IOException, MissingParameterErrorException {

    if(StringUtils.isBlank(imagePth)){
      throw new MissingParameterErrorException();
    }
    Path imagePath = Paths.get(imagePth);

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


