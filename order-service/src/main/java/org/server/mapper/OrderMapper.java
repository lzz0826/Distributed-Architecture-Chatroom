package org.server.mapper;

import org.server.dao.OrderDAO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface OrderMapper {

   int insertOrder(OrderDAO order);

   List<OrderDAO> selectAll();

   OrderDAO selectById(@Param("id") String id);

   int updateByName(@Param("name") String name ,@Param("price") int price);


}
