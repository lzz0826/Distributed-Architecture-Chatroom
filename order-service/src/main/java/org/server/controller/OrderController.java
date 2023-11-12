package org.server.controller;


import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.server.common.BaseResp;
import org.server.controller.rep.Results;
import org.server.controller.req.OrderReq;
import org.server.exception.NotOrderUserException;
import org.server.service.OrderService;
import org.server.vo.OrderVO;
import org.server.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {



  @Resource
  private OrderService orderService;


  @GetMapping("/{id}")
  public BaseResp<OrderVO> gitByID(@PathVariable("id") String id)
      throws NotOrderUserException, NoSuchFieldException, IllegalAccessException {
    OrderVO byId = orderService.getById(id);
    return BaseResp.ok(byId);

  }


  /**
   * get请求
   */
  @GetMapping("/test/server")
  public String getMsg() {
    System.out.println("/test/server 收到");
    return "这是OkHttp的请求";
  }

  /**
   * form表单
   */
  @PostMapping("/test/form")
  public String getFormMsg(OrderReq req) {
    System.out.println("/test/form 收到");
    System.out.println(req.toString());
    req.setId("9999");
    req.setUserId("8888");
    req.setPrice(100+req.getPrice());
    log.info(JSON.toJSONString(req));
    return JSON.toJSONString(req);
  }

  /**
   * post请求
   */
  @PostMapping("/test/order")
  public OrderVO getOder(@RequestBody OrderReq req) {
    System.out.println("/test/order 收到");

    UserVO userVO = UserVO.builder()
        .id("123")
        .username("test name")
        .build();

    OrderVO orderVO = OrderVO.builder().userVO(userVO).build();

    BeanUtils.copyProperties(req, orderVO);
    log.info(JSON.toJSONString(orderVO));
    return orderVO;
  }

  /**
   * post请求
   */
  @PostMapping("/test/result")
  public Results getResult(@RequestBody OrderReq req) {

    System.out.println("/test/result 收到");
    System.out.println(req.toString());

    UserVO userVO = UserVO.builder()
        .id("666")
        .username("test name666")
        .build();
    OrderVO orderVO = OrderVO.builder().userVO(userVO).build();

    BeanUtils.copyProperties(req, orderVO);

    System.out.println(orderVO.getUserVO().toString());
    Results<Object> results = Results.builder().build();
    results.setCode(200);
    results.setStatus("OK");
    results.setData(orderVO);
    return results;
  }

  /**
   * 返回照片
   */
  @PostMapping("/test/img")
  public void getResult(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      File file = new File("/Users/yexuerui/Downloads/二维码池改造.png");
      FileInputStream inputStream = new FileInputStream(file);
      byte[] data = new byte[(int) file.length()];
      //信息从文件输入到JVM的数组中
      inputStream.read(data);
      inputStream.close();
      response.setContentType("image/png");
      ServletOutputStream outputStream = response.getOutputStream();
      //信息从JVM的数组输出到
      outputStream.write(data);
      outputStream.close();
    } catch (FileNotFoundException e) {
      log.error("", e);
    } catch (IOException e) {
      log.error("", e);
    }
  }
}


