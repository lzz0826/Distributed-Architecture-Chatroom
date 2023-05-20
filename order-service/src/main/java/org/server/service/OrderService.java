package org.server.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.server.common.BaseResp;
import org.server.exception.NotOrderUserException;
import org.server.mapper.OrderMapper;
import org.server.pojo.Order;
import org.server.vo.OrderVO;
import org.server.vo.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {


  @Resource
  private OrderMapper orderMapper;

  @Resource
  private RestTemplate restTemplate;

  public OrderVO getById(String id)
      throws NotOrderUserException, NoSuchFieldException, IllegalAccessException {


    Order order = orderMapper.selectById(id);


    if(order == null){
      throw new NotOrderUserException();
    }

    String userId = order.getUserId();

    String url = "http://127.0.0.1:8080/user/user/"+userId;

    System.out.println(url);

    BaseResp baseResp = restTemplate.getForObject(url, BaseResp.class);

    Field field = baseResp.getClass().getDeclaredField("data");
    field.setAccessible(true);
    Object dataObj = field.get(baseResp);

    String s = JSON.toJSONString(dataObj);
    UserVO userVO = JSON.parseObject(s, UserVO.class);
    OrderVO orderVO = OrderVO
        .builder()
        .id(order.getId())
        .userId(order.getUserId())
        .name(order.getName())
        .price(order.getPrice())
        .num(order.getNum())
        .userVO(userVO)
        .build();


    return orderVO;


  }



}
