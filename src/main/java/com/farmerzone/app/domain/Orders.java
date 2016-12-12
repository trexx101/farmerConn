package com.farmerzone.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.farmerzone.app.domain.enumeration.Status;

/**
 * The Employee entity.                                                        
 * 
 */
@ApiModel(description = "The Employee entity.")
@Entity
@Table(name = "orders")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "order_date")
    private ZonedDateTime orderDate;

    @Column(name = "ship_date")
    private ZonedDateTime shipDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private Status transactionStatus;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "payment_date")
    private ZonedDateTime paymentDate;

    @OneToOne
    @JoinColumn(unique = true)
    private Payment paymentId;

    @OneToMany(mappedBy = "orders")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderData> orders = new HashSet<>();

    @ManyToOne
    private Customers customers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public Orders orderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public ZonedDateTime getShipDate() {
        return shipDate;
    }

    public Orders shipDate(ZonedDateTime shipDate) {
        this.shipDate = shipDate;
        return this;
    }

    public void setShipDate(ZonedDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public Status getTransactionStatus() {
        return transactionStatus;
    }

    public Orders transactionStatus(Status transactionStatus) {
        this.transactionStatus = transactionStatus;
        return this;
    }

    public void setTransactionStatus(Status transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Orders deleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getPaymentDate() {
        return paymentDate;
    }

    public Orders paymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(ZonedDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Payment getPaymentId() {
        return paymentId;
    }

    public Orders paymentId(Payment payment) {
        this.paymentId = payment;
        return this;
    }

    public void setPaymentId(Payment payment) {
        this.paymentId = payment;
    }

    public Set<OrderData> getOrders() {
        return orders;
    }

    public Orders orders(Set<OrderData> orderData) {
        this.orders = orderData;
        return this;
    }

    public Orders addOrder(OrderData orderData) {
        orders.add(orderData);
        orderData.setOrders(this);
        return this;
    }

    public Orders removeOrder(OrderData orderData) {
        orders.remove(orderData);
        orderData.setOrders(null);
        return this;
    }

    public void setOrders(Set<OrderData> orderData) {
        this.orders = orderData;
    }

    public Customers getCustomers() {
        return customers;
    }

    public Orders customers(Customers customers) {
        this.customers = customers;
        return this;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        if(orders.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Orders{" +
            "id=" + id +
            ", orderDate='" + orderDate + "'" +
            ", shipDate='" + shipDate + "'" +
            ", transactionStatus='" + transactionStatus + "'" +
            ", deleted='" + deleted + "'" +
            ", paymentDate='" + paymentDate + "'" +
            '}';
    }
}
