package com.bfwg.repository;

import com.bfwg.model.Flight;
import com.bfwg.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Page<Hotel> findByName(String name, Pageable pageable);

}
