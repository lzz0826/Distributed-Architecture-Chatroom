package org.server.mapper;

import org.server.pojo.Order;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface OrderMapper {

   int insertOrder(Order order);

   List<Order> selectAll();

   Order selectById(@Param("id") String id);


}
