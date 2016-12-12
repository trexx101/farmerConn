package com.farmerzone.app.repository;

import com.farmerzone.app.domain.Customers;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Customers entity.
 */
@SuppressWarnings("unused")
public interface CustomersRepository extends JpaRepository<Customers,Long> {

}
