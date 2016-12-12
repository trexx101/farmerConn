package com.farmerzone.app.repository;

import com.farmerzone.app.domain.OrderData;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OrderData entity.
 */
@SuppressWarnings("unused")
public interface OrderDataRepository extends JpaRepository<OrderData,Long> {

}
