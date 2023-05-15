package org.server.service;

import javax.annotation.Resource;
import org.server.mapper.OrderMapper;
import org.server.pojo.Order;
import org.server.vo.OrderVO;
import org.springframework.stereotype.Service;

@Service
public class OrderService {


  @Resource
  private OrderMapper orderMapper;

  public OrderVO getById(String id){

    Order order = orderMapper.selectById(id);

    OrderVO build = OrderVO
        .builder()
        .id(order.getId())
        .userId(order.getUserId())
        .name(order.getName())
        .price(order.getPrice())
        .num(order.getNum())
        .build();


    return build;


  }



}
