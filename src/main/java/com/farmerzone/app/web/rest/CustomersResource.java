package com.farmerzone.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.farmerzone.app.domain.Customers;
import com.farmerzone.app.service.CustomersService;
import com.farmerzone.app.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Customers.
 */
@RestController
@RequestMapping("/api")
public class CustomersResource {

    private final Logger log = LoggerFactory.getLogger(CustomersResource.class);
        
    @Inject
    private CustomersService customersService;

    /**
     * POST  /customers : Create a new customers.
     *
     * @param customers the customers to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customers, or with status 400 (Bad Request) if the customers has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customers")
    @Timed
    public ResponseEntity<Customers> createCustomers(@Valid @RequestBody Customers customers) throws URISyntaxException {
        log.debug("REST request to save Customers : {}", customers);
        if (customers.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("customers", "idexists", "A new customers cannot already have an ID")).body(null);
        }
        Customers result = customersService.save(customers);
        return ResponseEntity.created(new URI("/api/customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("customers", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customers : Updates an existing customers.
     *
     * @param customers the customers to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customers,
     * or with status 400 (Bad Request) if the customers is not valid,
     * or with status 500 (Internal Server Error) if the customers couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customers")
    @Timed
    public ResponseEntity<Customers> updateCustomers(@Valid @RequestBody Customers customers) throws URISyntaxException {
        log.debug("REST request to update Customers : {}", customers);
        if (customers.getId() == null) {
            return createCustomers(customers);
        }
        Customers result = customersService.save(customers);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("customers", customers.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customers : get all the customers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customers in body
     */
    @GetMapping("/customers")
    @Timed
    public List<Customers> getAllCustomers() {
        log.debug("REST request to get all Customers");
        return customersService.findAll();
    }

    /**
     * GET  /customers/:id : get the "id" customers.
     *
     * @param id the id of the customers to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customers, or with status 404 (Not Found)
     */
    @GetMapping("/customers/{id}")
    @Timed
    public ResponseEntity<Customers> getCustomers(@PathVariable Long id) {
        log.debug("REST request to get Customers : {}", id);
        Customers customers = customersService.findOne(id);
        return Optional.ofNullable(customers)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /customers/:id : delete the "id" customers.
     *
     * @param id the id of the customers to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomers(@PathVariable Long id) {
        log.debug("REST request to delete Customers : {}", id);
        customersService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("customers", id.toString())).build();
    }

}
