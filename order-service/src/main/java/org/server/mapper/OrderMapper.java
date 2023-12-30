package org.server.mapper;

import java.math.BigDecimal;
import org.server.dao.OrderDAO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface OrderMapper {

   int insertOrder(OrderDAO orderDAO);

   List<OrderDAO> selectAll();

   OrderDAO selectById(@Param("id") String id);

   int updatePriceByUserId(@Param("userId") String name ,@Param("price") BigDecimal price);

   int updateByOrderId(OrderDAO orderDAO);



}
