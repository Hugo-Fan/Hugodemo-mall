package com.hugo.hugodemomall.dao.impl;

import com.hugo.hugodemomall.dao.OrderDao;
import com.hugo.hugodemomall.dto.OrderQueryParams;
import com.hugo.hugodemomall.model.Order;
import com.hugo.hugodemomall.model.OrderItem;
import com.hugo.hugodemomall.rowmapper.OrderItemRowMapper;
import com.hugo.hugodemomall.rowmapper.OrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrderDaoImpl implements OrderDao {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Integer countOrder(OrderQueryParams orderQueryParams) {
        String sql = """
                    SELECT count(*)
                    From `order`
                    WHERE 1=1
                """;
        Map<String, Object> map = new HashMap<>();

        sql=addFilteringSql(sql,map,orderQueryParams);

        Integer total = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);


        return total;
    }

    @Override
    public List<Order> getOrders(OrderQueryParams orderQueryParams) {
        String sql = """
                SELECT order_id ,member_id ,total_amount ,created_date,last_modified_date
                FROM `order`
                WHERE 1=1
                """;

        Map<String, Object> map = new HashMap<>();
        // 查詢條件
        sql = addFilteringSql(sql,map,orderQueryParams);

        // 排序
        sql += " ORDER BY created_date DESC";

        // 分頁
        sql += " LIMIT :limit OFFSET :offset";
        map.put("limit",orderQueryParams.getLimit());
        map.put("offset",orderQueryParams.getOffset());

        List<Order> orderList = namedParameterJdbcTemplate.query(sql, map, new OrderRowMapper());

        return orderList;
    }

    @Override
    public Order getOrderById(Integer orderId) {
        String sql = """
                    SELECT order_id,member_id,total_amount,created_date,last_modified_date
                    FROM `order`
                    WHERE order_id = :orderId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("orderId",orderId);

        List<Order> orderList =  namedParameterJdbcTemplate.query(sql,map,new OrderRowMapper());

        if(orderList.size()>0){
            return orderList.get(0);
        }else {
            return null;
        }

    }

    @Override
    public Integer getOrderByTotalPrice(Integer memberId) {
        String sql = """
                SELECT Sum(total_amount)
                FROM `order`
                WHERE member_id = :memberId
                """;
        Map<String,Object> map = new HashMap<>();
        map.put("memberId",memberId);

        Integer priceTotal = namedParameterJdbcTemplate.queryForObject(sql, map, Integer.class);

        if(priceTotal >= 0){
            return priceTotal;
        }else {
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsByOrderId(Integer orderId) {
        String sql = """
                    SELECT oi.order_item_id,oi.order_id,oi.product_id,oi.quantity,oi.amount,p.product_name,p.image_url
                    FROM order_item as oi
                    LEFT JOIN product as p
                    ON oi.product_id = p.product_id
                    WHERE oi.order_id = :orderId
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("orderId",orderId);
        List<OrderItem> orderItemList = namedParameterJdbcTemplate.query(sql,map,new OrderItemRowMapper());

        return orderItemList;
    }

    @Override
    public Integer createOrder(Integer memberId, Integer totalAmount) {
        String sql = """
                    INSERT INTO `order`(member_id,total_amount,created_date,last_modified_date)
                    VALUES(:memberId,:totalAmount,:createdDate,:lastModifiedDate)
                """;
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("totalAmount", totalAmount);

        Date now = new Date();
        map.put("createdDate", now);
        map.put("lastModifiedDate", now);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        int orderId = keyHolder.getKey().intValue();

        return orderId;
    }

    @Override
    public void createrOrderItems(Integer orderId, List<OrderItem> orderItemList) {
        // for loop  一條一條加入 效率較低
//        for (OrderItem orderItem : orderItemList) {
//            String sql = """
//                        INSERT INTO order_item(order_id,product_id,quantity,amount)
//                        VALUES(:orderId,:productId,:quantity,:amount)
//                        """;
//
//            Map<String, Object> map = new HashMap<>();
//            map.put("orderId",orderId);
//            map.put("productId",orderItem.getProductId());
//            map.put("quantity",orderItem.getQuantity());
//            map.put("amount",orderItem.getAmount());
//
//            namedParameterJdbcTemplate.update(sql,map);
//        }
        // 使用batchUpdate 一次性加入數據 效率較高
        String sql = """
                        INSERT INTO order_item(order_id,product_id,quantity,amount)
                        VALUES(:orderId,:productId,:quantity,:amount)
                        """;

        MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[orderItemList.size()];

        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);

            parameterSources[i] = new MapSqlParameterSource();
            parameterSources[i].addValue("orderId",orderId);
            parameterSources[i].addValue("productId",orderItem.getProductId());
            parameterSources[i].addValue("quantity",orderItem.getQuantity());
            parameterSources[i].addValue("amount",orderItem.getAmount());

        }

        namedParameterJdbcTemplate.batchUpdate(sql,parameterSources);
    }

    @Override
    public void deleteOrderById(Integer orderId) {
        String sql = """
                    DELETE FROM `order`
                    WHERE order_id =:orderId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("orderId",orderId);

        namedParameterJdbcTemplate.update(sql,map);
    }

    @Override
    public void deleteOrderItemsByOrderId(Integer orderId) {
        String sql = """
                    DELETE FROM order_item
                    WHERE order_id =:orderId
                """;

        Map<String, Object> map = new HashMap<>();
        map.put("orderId",orderId);

        namedParameterJdbcTemplate.update(sql,map);
    }

    private String addFilteringSql(String sql, Map<String,Object> map, OrderQueryParams orderQueryParams){
        if(orderQueryParams.getMemberId() != null){
            sql += " AND member_id =:memberId";
            map.put("memberId",orderQueryParams.getMemberId());
        }

        return sql;
    }



}
