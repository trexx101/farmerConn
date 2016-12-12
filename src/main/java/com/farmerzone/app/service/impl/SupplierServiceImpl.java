package com.farmerzone.app.service.impl;

import com.farmerzone.app.service.SupplierService;
import com.farmerzone.app.domain.Supplier;
import com.farmerzone.app.repository.SupplierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Supplier.
 */
@Service
@Transactional
public class SupplierServiceImpl implements SupplierService{

    private final Logger log = LoggerFactory.getLogger(SupplierServiceImpl.class);
    
    @Inject
    private SupplierRepository supplierRepository;

    /**
     * Save a supplier.
     *
     * @param supplier the entity to save
     * @return the persisted entity
     */
    public Supplier save(Supplier supplier) {
        log.debug("Request to save Supplier : {}", supplier);
        Supplier result = supplierRepository.save(supplier);
        return result;
    }

    /**
     *  Get all the suppliers.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Supplier> findAll() {
        log.debug("Request to get all Suppliers");
        List<Supplier> result = supplierRepository.findAll();

        return result;
    }

    /**
     *  Get one supplier by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Supplier findOne(Long id) {
        log.debug("Request to get Supplier : {}", id);
        Supplier supplier = supplierRepository.findOne(id);
        return supplier;
    }

    /**
     *  Delete the  supplier by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Supplier : {}", id);
        supplierRepository.delete(id);
    }
}
