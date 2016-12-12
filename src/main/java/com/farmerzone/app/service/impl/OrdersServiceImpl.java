package com.farmerzone.app.service.impl;

import com.farmerzone.app.service.OrdersService;
import com.farmerzone.app.domain.Orders;
import com.farmerzone.app.repository.OrdersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Orders.
 */
@Service
@Transactional
public class OrdersServiceImpl implements OrdersService{

    private final Logger log = LoggerFactory.getLogger(OrdersServiceImpl.class);
    
    @Inject
    private OrdersRepository ordersRepository;

    /**
     * Save a orders.
     *
     * @param orders the entity to save
     * @return the persisted entity
     */
    public Orders save(Orders orders) {
        log.debug("Request to save Orders : {}", orders);
        Orders result = ordersRepository.save(orders);
        return result;
    }

    /**
     *  Get all the orders.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Orders> findAll() {
        log.debug("Request to get all Orders");
        List<Orders> result = ordersRepository.findAll();

        return result;
    }

    /**
     *  Get one orders by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Orders findOne(Long id) {
        log.debug("Request to get Orders : {}", id);
        Orders orders = ordersRepository.findOne(id);
        return orders;
    }

    /**
     *  Delete the  orders by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Orders : {}", id);
        ordersRepository.delete(id);
    }
}
