package com.farmerzone.app.web.rest;

import com.farmerzone.app.FarmerConnApp;

import com.farmerzone.app.domain.OrderData;
import com.farmerzone.app.repository.OrderDataRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderDataResource REST controller.
 *
 * @see OrderDataResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FarmerConnApp.class)
public class OrderDataResourceIntTest {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final ZonedDateTime DEFAULT_SHIP_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SHIP_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SHIP_DATE_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_SHIP_DATE);

    private static final ZonedDateTime DEFAULT_BILL_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_BILL_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_BILL_DATE_STR = DateTimeFormatter.ISO_INSTANT.format(DEFAULT_BILL_DATE);

    @Inject
    private OrderDataRepository orderDataRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restOrderDataMockMvc;

    private OrderData orderData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderDataResource orderDataResource = new OrderDataResource();
        ReflectionTestUtils.setField(orderDataResource, "orderDataRepository", orderDataRepository);
        this.restOrderDataMockMvc = MockMvcBuilders.standaloneSetup(orderDataResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrderData createEntity(EntityManager em) {
        OrderData orderData = new OrderData()
                .price(DEFAULT_PRICE)
                .quantity(DEFAULT_QUANTITY)
                .shipDate(DEFAULT_SHIP_DATE)
                .billDate(DEFAULT_BILL_DATE);
        return orderData;
    }

    @Before
    public void initTest() {
        orderData = createEntity(em);
    }

    @Test
    @Transactional
    public void createOrderData() throws Exception {
        int databaseSizeBeforeCreate = orderDataRepository.findAll().size();

        // Create the OrderData

        restOrderDataMockMvc.perform(post("/api/order-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(orderData)))
                .andExpect(status().isCreated());

        // Validate the OrderData in the database
        List<OrderData> orderData = orderDataRepository.findAll();
        assertThat(orderData).hasSize(databaseSizeBeforeCreate + 1);
        OrderData testOrderData = orderData.get(orderData.size() - 1);
        assertThat(testOrderData.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testOrderData.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrderData.getShipDate()).isEqualTo(DEFAULT_SHIP_DATE);
        assertThat(testOrderData.getBillDate()).isEqualTo(DEFAULT_BILL_DATE);
    }

    @Test
    @Transactional
    public void getAllOrderData() throws Exception {
        // Initialize the database
        orderDataRepository.saveAndFlush(orderData);

        // Get all the orderData
        restOrderDataMockMvc.perform(get("/api/order-data?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(orderData.getId().intValue())))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
                .andExpect(jsonPath("$.[*].shipDate").value(hasItem(DEFAULT_SHIP_DATE_STR)))
                .andExpect(jsonPath("$.[*].billDate").value(hasItem(DEFAULT_BILL_DATE_STR)));
    }

    @Test
    @Transactional
    public void getOrderData() throws Exception {
        // Initialize the database
        orderDataRepository.saveAndFlush(orderData);

        // Get the orderData
        restOrderDataMockMvc.perform(get("/api/order-data/{id}", orderData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(orderData.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.shipDate").value(DEFAULT_SHIP_DATE_STR))
            .andExpect(jsonPath("$.billDate").value(DEFAULT_BILL_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingOrderData() throws Exception {
        // Get the orderData
        restOrderDataMockMvc.perform(get("/api/order-data/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrderData() throws Exception {
        // Initialize the database
        orderDataRepository.saveAndFlush(orderData);
        int databaseSizeBeforeUpdate = orderDataRepository.findAll().size();

        // Update the orderData
        OrderData updatedOrderData = orderDataRepository.findOne(orderData.getId());
        updatedOrderData
                .price(UPDATED_PRICE)
                .quantity(UPDATED_QUANTITY)
                .shipDate(UPDATED_SHIP_DATE)
                .billDate(UPDATED_BILL_DATE);

        restOrderDataMockMvc.perform(put("/api/order-data")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedOrderData)))
                .andExpect(status().isOk());

        // Validate the OrderData in the database
        List<OrderData> orderData = orderDataRepository.findAll();
        assertThat(orderData).hasSize(databaseSizeBeforeUpdate);
        OrderData testOrderData = orderData.get(orderData.size() - 1);
        assertThat(testOrderData.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testOrderData.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrderData.getShipDate()).isEqualTo(UPDATED_SHIP_DATE);
        assertThat(testOrderData.getBillDate()).isEqualTo(UPDATED_BILL_DATE);
    }

    @Test
    @Transactional
    public void deleteOrderData() throws Exception {
        // Initialize the database
        orderDataRepository.saveAndFlush(orderData);
        int databaseSizeBeforeDelete = orderDataRepository.findAll().size();

        // Get the orderData
        restOrderDataMockMvc.perform(delete("/api/order-data/{id}", orderData.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<OrderData> orderData = orderDataRepository.findAll();
        assertThat(orderData).hasSize(databaseSizeBeforeDelete - 1);
    }
}
