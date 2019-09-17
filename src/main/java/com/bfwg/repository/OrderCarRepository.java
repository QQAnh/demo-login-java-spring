package com.bfwg.repository;

import com.bfwg.model.OrderCar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderCarRepository extends JpaRepository<OrderCar, Long> {
}
