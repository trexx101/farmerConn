package com.farmerzone.app.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.farmerzone.app.domain.Supplier;
import com.farmerzone.app.service.SupplierService;
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
 * REST controller for managing Supplier.
 */
@RestController
@RequestMapping("/api")
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);
        
    @Inject
    private SupplierService supplierService;

    /**
     * POST  /suppliers : Create a new supplier.
     *
     * @param supplier the supplier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new supplier, or with status 400 (Bad Request) if the supplier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/suppliers")
    @Timed
    public ResponseEntity<Supplier> createSupplier(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplier);
        if (supplier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("supplier", "idexists", "A new supplier cannot already have an ID")).body(null);
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity.created(new URI("/api/suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("supplier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suppliers : Updates an existing supplier.
     *
     * @param supplier the supplier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated supplier,
     * or with status 400 (Bad Request) if the supplier is not valid,
     * or with status 500 (Internal Server Error) if the supplier couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/suppliers")
    @Timed
    public ResponseEntity<Supplier> updateSupplier(@Valid @RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}", supplier);
        if (supplier.getId() == null) {
            return createSupplier(supplier);
        }
        Supplier result = supplierService.save(supplier);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("supplier", supplier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suppliers : get all the suppliers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of suppliers in body
     */
    @GetMapping("/suppliers")
    @Timed
    public List<Supplier> getAllSuppliers() {
        log.debug("REST request to get all Suppliers");
        return supplierService.findAll();
    }

    /**
     * GET  /suppliers/:id : get the "id" supplier.
     *
     * @param id the id of the supplier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the supplier, or with status 404 (Not Found)
     */
    @GetMapping("/suppliers/{id}")
    @Timed
    public ResponseEntity<Supplier> getSupplier(@PathVariable Long id) {
        log.debug("REST request to get Supplier : {}", id);
        Supplier supplier = supplierService.findOne(id);
        return Optional.ofNullable(supplier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /suppliers/:id : delete the "id" supplier.
     *
     * @param id the id of the supplier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/suppliers/{id}")
    @Timed
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("supplier", id.toString())).build();
    }

}
