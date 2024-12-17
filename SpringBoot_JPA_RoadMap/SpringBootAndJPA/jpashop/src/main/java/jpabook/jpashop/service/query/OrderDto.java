package jpabook.jpashop.service.query;

import jpabook.jpashop.api.OrderApiController;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {

        orderId = order.getId();
        name = order.getMember().getName(); // LAZY 초기화
        orderDate = order.getOrderDate();
        orderStatus = order.getStatus();
//            order.getOrderItems().stream()
//                    .forEach(o -> o.getItem().getName());
        // orderitem 이 entity 그 자체로 들어감
        address = order.getDelivery().getAddress(); // LAZY 초기화
        orderItems = order.getOrderItems().stream()
                .map(OrderItemDto::new)
                .toList();
    }
}
