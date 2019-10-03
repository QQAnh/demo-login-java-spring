package com.bfwg.repository;

import com.bfwg.model.OrderTour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderTourRepository extends JpaRepository<OrderTour, Long>, JpaSpecificationExecutor<OrderTour> {
}
