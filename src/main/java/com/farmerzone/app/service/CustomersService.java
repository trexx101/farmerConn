package com.farmerzone.app.service;

import com.farmerzone.app.domain.Customers;

import java.util.List;

/**
 * Service Interface for managing Customers.
 */
public interface CustomersService {

    /**
     * Save a customers.
     *
     * @param customers the entity to save
     * @return the persisted entity
     */
    Customers save(Customers customers);

    /**
     *  Get all the customers.
     *  
     *  @return the list of entities
     */
    List<Customers> findAll();

    /**
     *  Get the "id" customers.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Customers findOne(Long id);

    /**
     *  Delete the "id" customers.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
