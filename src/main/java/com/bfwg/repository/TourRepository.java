package com.bfwg.repository;

import com.bfwg.model.Car;
import com.bfwg.model.Tour;
import com.bfwg.model.TourType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour, Long> {
    Page<Tour> findByTourTypeId(long tourType, Pageable pageable);
    Optional<Tour> findByIdAndTourType(Long id, Long tourType);
}
