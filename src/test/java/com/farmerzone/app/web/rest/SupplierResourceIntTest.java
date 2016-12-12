package com.farmerzone.app.web.rest;

import com.farmerzone.app.FarmerConnApp;

import com.farmerzone.app.domain.Supplier;
import com.farmerzone.app.repository.SupplierRepository;
import com.farmerzone.app.service.SupplierService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SupplierResource REST controller.
 *
 * @see SupplierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FarmerConnApp.class)
public class SupplierResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BUSINESS_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BUSINESS_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_1 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_1 = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_STATE_PROVINCE = "AAAAAAAAAA";
    private static final String UPDATED_STATE_PROVINCE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_ENTERED = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ENTERED = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_ENTERED_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_DATE_ENTERED);

    @Inject
    private SupplierRepository supplierRepository;

    @Inject
    private SupplierService supplierService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSupplierMockMvc;

    private Supplier supplier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupplierResource supplierResource = new SupplierResource();
        ReflectionTestUtils.setField(supplierResource, "supplierService", supplierService);
        this.restSupplierMockMvc = MockMvcBuilders.standaloneSetup(supplierResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Supplier createEntity(EntityManager em) {
        Supplier supplier = new Supplier()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .businessName(DEFAULT_BUSINESS_NAME)
                .login(DEFAULT_LOGIN)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .address1(DEFAULT_ADDRESS_1)
                .address2(DEFAULT_ADDRESS_2)
                .postalCode(DEFAULT_POSTAL_CODE)
                .city(DEFAULT_CITY)
                .stateProvince(DEFAULT_STATE_PROVINCE)
                .dateEntered(DEFAULT_DATE_ENTERED);
        return supplier;
    }

    @Before
    public void initTest() {
        supplier = createEntity(em);
    }

    @Test
    @Transactional
    public void createSupplier() throws Exception {
        int databaseSizeBeforeCreate = supplierRepository.findAll().size();

        // Create the Supplier

        restSupplierMockMvc.perform(post("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isCreated());

        // Validate the Supplier in the database
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeCreate + 1);
        Supplier testSupplier = suppliers.get(suppliers.size() - 1);
        assertThat(testSupplier.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testSupplier.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testSupplier.getBusinessName()).isEqualTo(DEFAULT_BUSINESS_NAME);
        assertThat(testSupplier.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testSupplier.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSupplier.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSupplier.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testSupplier.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testSupplier.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testSupplier.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testSupplier.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testSupplier.getDateEntered()).isEqualTo(DEFAULT_DATE_ENTERED);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = supplierRepository.findAll().size();
        // set the field null
        supplier.setLogin(null);

        // Create the Supplier, which fails.

        restSupplierMockMvc.perform(post("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplier)))
                .andExpect(status().isBadRequest());

        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSuppliers() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get all the suppliers
        restSupplierMockMvc.perform(get("/api/suppliers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supplier.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].businessName").value(hasItem(DEFAULT_BUSINESS_NAME.toString())))
                .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].address1").value(hasItem(DEFAULT_ADDRESS_1.toString())))
                .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2.toString())))
                .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE.toString())))
                .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY.toString())))
                .andExpect(jsonPath("$.[*].stateProvince").value(hasItem(DEFAULT_STATE_PROVINCE.toString())))
                .andExpect(jsonPath("$.[*].dateEntered").value(hasItem(DEFAULT_DATE_ENTERED_STR)));
    }

    @Test
    @Transactional
    public void getSupplier() throws Exception {
        // Initialize the database
        supplierRepository.saveAndFlush(supplier);

        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", supplier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(supplier.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.businessName").value(DEFAULT_BUSINESS_NAME.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.address1").value(DEFAULT_ADDRESS_1.toString()))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2.toString()))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE.toString()))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY.toString()))
            .andExpect(jsonPath("$.stateProvince").value(DEFAULT_STATE_PROVINCE.toString()))
            .andExpect(jsonPath("$.dateEntered").value(DEFAULT_DATE_ENTERED_STR));
    }

    @Test
    @Transactional
    public void getNonExistingSupplier() throws Exception {
        // Get the supplier
        restSupplierMockMvc.perform(get("/api/suppliers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplier() throws Exception {
        // Initialize the database
        supplierService.save(supplier);

        int databaseSizeBeforeUpdate = supplierRepository.findAll().size();

        // Update the supplier
        Supplier updatedSupplier = supplierRepository.findOne(supplier.getId());
        updatedSupplier
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .businessName(UPDATED_BUSINESS_NAME)
                .login(UPDATED_LOGIN)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE)
                .address1(UPDATED_ADDRESS_1)
                .address2(UPDATED_ADDRESS_2)
                .postalCode(UPDATED_POSTAL_CODE)
                .city(UPDATED_CITY)
                .stateProvince(UPDATED_STATE_PROVINCE)
                .dateEntered(UPDATED_DATE_ENTERED);

        restSupplierMockMvc.perform(put("/api/suppliers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSupplier)))
                .andExpect(status().isOk());

        // Validate the Supplier in the database
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeUpdate);
        Supplier testSupplier = suppliers.get(suppliers.size() - 1);
        assertThat(testSupplier.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testSupplier.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testSupplier.getBusinessName()).isEqualTo(UPDATED_BUSINESS_NAME);
        assertThat(testSupplier.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testSupplier.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSupplier.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSupplier.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testSupplier.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testSupplier.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testSupplier.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testSupplier.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testSupplier.getDateEntered()).isEqualTo(UPDATED_DATE_ENTERED);
    }

    @Test
    @Transactional
    public void deleteSupplier() throws Exception {
        // Initialize the database
        supplierService.save(supplier);

        int databaseSizeBeforeDelete = supplierRepository.findAll().size();

        // Get the supplier
        restSupplierMockMvc.perform(delete("/api/suppliers/{id}", supplier.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Supplier> suppliers = supplierRepository.findAll();
        assertThat(suppliers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
