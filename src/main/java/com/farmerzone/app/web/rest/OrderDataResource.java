package com.farmerzone.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.farmerzone.app.domain.OrderData;

import com.farmerzone.app.repository.OrderDataRepository;
import com.farmerzone.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing OrderData.
 */
@RestController
@RequestMapping("/api")
public class OrderDataResource {

    private final Logger log = LoggerFactory.getLogger(OrderDataResource.class);
        
    @Inject
    private OrderDataRepository orderDataRepository;

    /**
     * POST  /order-data : Create a new orderData.
     *
     * @param orderData the orderData to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderData, or with status 400 (Bad Request) if the orderData has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-data")
    @Timed
    public ResponseEntity<OrderData> createOrderData(@RequestBody OrderData orderData) throws URISyntaxException {
        log.debug("REST request to save OrderData : {}", orderData);
        if (orderData.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("orderData", "idexists", "A new orderData cannot already have an ID")).body(null);
        }
        OrderData result = orderDataRepository.save(orderData);
        return ResponseEntity.created(new URI("/api/order-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("orderData", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-data : Updates an existing orderData.
     *
     * @param orderData the orderData to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderData,
     * or with status 400 (Bad Request) if the orderData is not valid,
     * or with status 500 (Internal Server Error) if the orderData couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-data")
    @Timed
    public ResponseEntity<OrderData> updateOrderData(@RequestBody OrderData orderData) throws URISyntaxException {
        log.debug("REST request to update OrderData : {}", orderData);
        if (orderData.getId() == null) {
            return createOrderData(orderData);
        }
        OrderData result = orderDataRepository.save(orderData);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("orderData", orderData.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-data : get all the orderData.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of orderData in body
     */
    @GetMapping("/order-data")
    @Timed
    public List<OrderData> getAllOrderData() {
        log.debug("REST request to get all OrderData");
        List<OrderData> orderData = orderDataRepository.findAll();
        return orderData;
    }

    /**
     * GET  /order-data/:id : get the "id" orderData.
     *
     * @param id the id of the orderData to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderData, or with status 404 (Not Found)
     */
    @GetMapping("/order-data/{id}")
    @Timed
    public ResponseEntity<OrderData> getOrderData(@PathVariable Long id) {
        log.debug("REST request to get OrderData : {}", id);
        OrderData orderData = orderDataRepository.findOne(id);
        return Optional.ofNullable(orderData)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /order-data/:id : delete the "id" orderData.
     *
     * @param id the id of the orderData to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-data/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderData(@PathVariable Long id) {
        log.debug("REST request to delete OrderData : {}", id);
        orderDataRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("orderData", id.toString())).build();
    }

}
