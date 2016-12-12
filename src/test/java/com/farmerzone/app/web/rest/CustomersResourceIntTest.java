package com.farmerzone.app.web.rest;

import com.farmerzone.app.FarmerConnApp;

import com.farmerzone.app.domain.Customers;
import com.farmerzone.app.repository.CustomersRepository;
import com.farmerzone.app.service.CustomersService;

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
 * Test class for the CustomersResource REST controller.
 *
 * @see CustomersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FarmerConnApp.class)
public class CustomersResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

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
    private CustomersRepository customersRepository;

    @Inject
    private CustomersService customersService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCustomersMockMvc;

    private Customers customers;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CustomersResource customersResource = new CustomersResource();
        ReflectionTestUtils.setField(customersResource, "customersService", customersService);
        this.restCustomersMockMvc = MockMvcBuilders.standaloneSetup(customersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Customers createEntity(EntityManager em) {
        Customers customers = new Customers()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .login(DEFAULT_LOGIN)
                .email(DEFAULT_EMAIL)
                .phone(DEFAULT_PHONE)
                .address1(DEFAULT_ADDRESS_1)
                .address2(DEFAULT_ADDRESS_2)
                .postalCode(DEFAULT_POSTAL_CODE)
                .city(DEFAULT_CITY)
                .stateProvince(DEFAULT_STATE_PROVINCE)
                .dateEntered(DEFAULT_DATE_ENTERED);
        return customers;
    }

    @Before
    public void initTest() {
        customers = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomers() throws Exception {
        int databaseSizeBeforeCreate = customersRepository.findAll().size();

        // Create the Customers

        restCustomersMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customers)))
                .andExpect(status().isCreated());

        // Validate the Customers in the database
        List<Customers> customers = customersRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeCreate + 1);
        Customers testCustomers = customers.get(customers.size() - 1);
        assertThat(testCustomers.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCustomers.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCustomers.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testCustomers.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCustomers.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCustomers.getAddress1()).isEqualTo(DEFAULT_ADDRESS_1);
        assertThat(testCustomers.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testCustomers.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testCustomers.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testCustomers.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
        assertThat(testCustomers.getDateEntered()).isEqualTo(DEFAULT_DATE_ENTERED);
    }

    @Test
    @Transactional
    public void checkLoginIsRequired() throws Exception {
        int databaseSizeBeforeTest = customersRepository.findAll().size();
        // set the field null
        customers.setLogin(null);

        // Create the Customers, which fails.

        restCustomersMockMvc.perform(post("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(customers)))
                .andExpect(status().isBadRequest());

        List<Customers> customers = customersRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get all the customers
        restCustomersMockMvc.perform(get("/api/customers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(customers.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
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
    public void getCustomers() throws Exception {
        // Initialize the database
        customersRepository.saveAndFlush(customers);

        // Get the customers
        restCustomersMockMvc.perform(get("/api/customers/{id}", customers.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customers.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
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
    public void getNonExistingCustomers() throws Exception {
        // Get the customers
        restCustomersMockMvc.perform(get("/api/customers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomers() throws Exception {
        // Initialize the database
        customersService.save(customers);

        int databaseSizeBeforeUpdate = customersRepository.findAll().size();

        // Update the customers
        Customers updatedCustomers = customersRepository.findOne(customers.getId());
        updatedCustomers
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .login(UPDATED_LOGIN)
                .email(UPDATED_EMAIL)
                .phone(UPDATED_PHONE)
                .address1(UPDATED_ADDRESS_1)
                .address2(UPDATED_ADDRESS_2)
                .postalCode(UPDATED_POSTAL_CODE)
                .city(UPDATED_CITY)
                .stateProvince(UPDATED_STATE_PROVINCE)
                .dateEntered(UPDATED_DATE_ENTERED);

        restCustomersMockMvc.perform(put("/api/customers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCustomers)))
                .andExpect(status().isOk());

        // Validate the Customers in the database
        List<Customers> customers = customersRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeUpdate);
        Customers testCustomers = customers.get(customers.size() - 1);
        assertThat(testCustomers.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCustomers.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCustomers.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testCustomers.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCustomers.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCustomers.getAddress1()).isEqualTo(UPDATED_ADDRESS_1);
        assertThat(testCustomers.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testCustomers.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testCustomers.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testCustomers.getStateProvince()).isEqualTo(UPDATED_STATE_PROVINCE);
        assertThat(testCustomers.getDateEntered()).isEqualTo(UPDATED_DATE_ENTERED);
    }

    @Test
    @Transactional
    public void deleteCustomers() throws Exception {
        // Initialize the database
        customersService.save(customers);

        int databaseSizeBeforeDelete = customersRepository.findAll().size();

        // Get the customers
        restCustomersMockMvc.perform(delete("/api/customers/{id}", customers.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Customers> customers = customersRepository.findAll();
        assertThat(customers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
