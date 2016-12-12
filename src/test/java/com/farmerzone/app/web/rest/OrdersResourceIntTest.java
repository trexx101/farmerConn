package com.farmerzone.app.web.rest;

import com.farmerzone.app.FarmerConnApp;

import com.farmerzone.app.domain.Orders;
import com.farmerzone.app.repository.OrdersRepository;
import com.farmerzone.app.service.OrdersService;

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

import com.farmerzone.app.domain.enumeration.Status;
/**
 * Test class for the OrdersResource REST controller.
 *
 * @see OrdersResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FarmerConnApp.class)
public class OrdersResourceIntTest {

    private static final ZonedDateTime DEFAULT_ORDER_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ORDER_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_ORDER_DATE_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_ORDER_DATE);

    private static final ZonedDateTime DEFAULT_SHIP_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SHIP_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SHIP_DATE_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_SHIP_DATE);

    private static final Status DEFAULT_TRANSACTION_STATUS = Status.NEW;
    private static final Status UPDATED_TRANSACTION_STATUS = Status.SEALED;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final ZonedDateTime DEFAULT_PAYMENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_PAYMENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PAYMENT_DATE_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_PAYMENT_DATE);

    @Inject
    private OrdersRepository ordersRepository;

    @Inject
    private OrdersService ordersService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrdersMockMvc;

    private Orders orders;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrdersResource ordersResource = new OrdersResource();
        ReflectionTestUtils.setField(ordersResource, "ordersService", ordersService);
        this.restOrdersMockMvc = MockMvcBuilders.standaloneSetup(ordersResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orders createEntity(EntityManager em) {
        Orders orders = new Orders()
                .orderDate(DEFAULT_ORDER_DATE)
                .shipDate(DEFAULT_SHIP_DATE)
                .transactionStatus(DEFAULT_TRANSACTION_STATUS)
                .deleted(DEFAULT_DELETED)
                .paymentDate(DEFAULT_PAYMENT_DATE);
        return orders;
    }

    @Before
    public void initTest() {
        orders = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrders() throws Exception {
        int databaseSizeBeforeCreate = ordersRepository.findAll().size();

        // Create the Orders

        restOrdersMockMvc.perform(post("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orders)))
                .andExpect(status().isCreated());

        // Validate the Orders in the database
        List<Orders> orders = ordersRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeCreate + 1);
        Orders testOrders = orders.get(orders.size() - 1);
        assertThat(testOrders.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testOrders.getShipDate()).isEqualTo(DEFAULT_SHIP_DATE);
        assertThat(testOrders.getTransactionStatus()).isEqualTo(DEFAULT_TRANSACTION_STATUS);
        assertThat(testOrders.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testOrders.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get all the orders
        restOrdersMockMvc.perform(get("/api/orders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orders.getId().intValue())))
                .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE_STR)))
                .andExpect(jsonPath("$.[*].shipDate").value(hasItem(DEFAULT_SHIP_DATE_STR)))
                .andExpect(jsonPath("$.[*].transactionStatus").value(hasItem(DEFAULT_TRANSACTION_STATUS.toString())))
                .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
                .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getOrders() throws Exception {
        // Initialize the database
        ordersRepository.saveAndFlush(orders);

        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", orders.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orders.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE_STR))
            .andExpect(jsonPath("$.shipDate").value(DEFAULT_SHIP_DATE_STR))
            .andExpect(jsonPath("$.transactionStatus").value(DEFAULT_TRANSACTION_STATUS.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingOrders() throws Exception {
        // Get the orders
        restOrdersMockMvc.perform(get("/api/orders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrders() throws Exception {
        // Initialize the database
        ordersService.save(orders);

        int databaseSizeBeforeUpdate = ordersRepository.findAll().size();

        // Update the orders
        Orders updatedOrders = ordersRepository.findOne(orders.getId());
        updatedOrders
                .orderDate(UPDATED_ORDER_DATE)
                .shipDate(UPDATED_SHIP_DATE)
                .transactionStatus(UPDATED_TRANSACTION_STATUS)
                .deleted(UPDATED_DELETED)
                .paymentDate(UPDATED_PAYMENT_DATE);

        restOrdersMockMvc.perform(put("/api/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrders)))
                .andExpect(status().isOk());

        // Validate the Orders in the database
        List<Orders> orders = ordersRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeUpdate);
        Orders testOrders = orders.get(orders.size() - 1);
        assertThat(testOrders.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testOrders.getShipDate()).isEqualTo(UPDATED_SHIP_DATE);
        assertThat(testOrders.getTransactionStatus()).isEqualTo(UPDATED_TRANSACTION_STATUS);
        assertThat(testOrders.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testOrders.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
    }

    @Test
    @Transactional
    public void deleteOrders() throws Exception {
        // Initialize the database
        ordersService.save(orders);

        int databaseSizeBeforeDelete = ordersRepository.findAll().size();

        // Get the orders
        restOrdersMockMvc.perform(delete("/api/orders/{id}", orders.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Orders> orders = ordersRepository.findAll();
        assertThat(orders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
