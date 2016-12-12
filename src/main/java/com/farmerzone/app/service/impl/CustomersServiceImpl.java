package com.farmerzone.app.service.impl;

import com.farmerzone.app.service.CustomersService;
import com.farmerzone.app.domain.Customers;
import com.farmerzone.app.repository.CustomersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Customers.
 */
@Service
@Transactional
public class CustomersServiceImpl implements CustomersService{

    private final Logger log = LoggerFactory.getLogger(CustomersServiceImpl.class);
    
    @Inject
    private CustomersRepository customersRepository;

    /**
     * Save a customers.
     *
     * @param customers the entity to save
     * @return the persisted entity
     */
    public Customers save(Customers customers) {
        log.debug("Request to save Customers : {}", customers);
        Customers result = customersRepository.save(customers);
        return result;
    }

    /**
     *  Get all the customers.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Customers> findAll() {
        log.debug("Request to get all Customers");
        List<Customers> result = customersRepository.findAll();

        return result;
    }

    /**
     *  Get one customers by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Customers findOne(Long id) {
        log.debug("Request to get Customers : {}", id);
        Customers customers = customersRepository.findOne(id);
        return customers;
    }

    /**
     *  Delete the  customers by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Customers : {}", id);
        customersRepository.delete(id);
    }
}
