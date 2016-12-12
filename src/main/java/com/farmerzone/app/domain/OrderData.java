package com.farmerzone.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A OrderData.
 */
@Entity
@Table(name = "order_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class OrderData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "price", precision=10, scale=2)
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "ship_date")
    private ZonedDateTime shipDate;

    @Column(name = "bill_date")
    private ZonedDateTime billDate;

    @ManyToOne
    private Orders orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public OrderData price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public OrderData quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ZonedDateTime getShipDate() {
        return shipDate;
    }

    public OrderData shipDate(ZonedDateTime shipDate) {
        this.shipDate = shipDate;
        return this;
    }

    public void setShipDate(ZonedDateTime shipDate) {
        this.shipDate = shipDate;
    }

    public ZonedDateTime getBillDate() {
        return billDate;
    }

    public OrderData billDate(ZonedDateTime billDate) {
        this.billDate = billDate;
        return this;
    }

    public void setBillDate(ZonedDateTime billDate) {
        this.billDate = billDate;
    }

    public Orders getOrders() {
        return orders;
    }

    public OrderData orders(Orders orders) {
        this.orders = orders;
        return this;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderData orderData = (OrderData) o;
        if(orderData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, orderData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "OrderData{" +
            "id=" + id +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            ", shipDate='" + shipDate + "'" +
            ", billDate='" + billDate + "'" +
            '}';
    }
}
