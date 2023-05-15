package org.server.controller;


import javax.annotation.Resource;
import org.server.common.BaseResp;
import org.server.service.OrderService;
import org.server.vo.OrderVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {



  @Resource
  private OrderService orderService;


  @GetMapping("/{id}")
  public BaseResp<OrderVO> gitByID(@PathVariable("id") String id) {

    OrderVO byId = orderService.getById(id);

    return BaseResp.ok(byId);


  }


}
