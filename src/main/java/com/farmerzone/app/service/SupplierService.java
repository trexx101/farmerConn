package com.farmerzone.app.service;

import com.farmerzone.app.domain.Supplier;

import java.util.List;

/**
 * Service Interface for managing Supplier.
 */
public interface SupplierService {

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save
     * @return the persisted entity
     */
    Supplier save(Supplier supplier);

    /**
     *  Get all the suppliers.
     *  
     *  @return the list of entities
     */
    List<Supplier> findAll();

    /**
     *  Get the "id" supplier.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    Supplier findOne(Long id);

    /**
     *  Delete the "id" supplier.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
