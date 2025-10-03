package com.order_service.feature.order.repository;

import com.order_service.feature.order.entity.Order;
import com.order_service.feature.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status , Pageable pageable);

}
