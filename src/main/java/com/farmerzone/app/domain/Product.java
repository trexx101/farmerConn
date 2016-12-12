package com.farmerzone.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name = "sku")
    private String sku;

    @Column(name = "quantity_per_unit")
    private Integer quantityPerUnit;

    @Column(name = "discount", precision=10, scale=2)
    private BigDecimal discount;

    @Column(name = "min_order")
    private Integer minOrder;

    @Lob
    @Column(name = "picture")
    private byte[] picture;

    @Column(name = "picture_content_type")
    private String pictureContentType;

    @Column(name = "unit_price", precision=10, scale=2)
    private BigDecimal unitPrice;

    @OneToOne
    @JoinColumn(unique = true)
    private Category category;

    @OneToOne
    @JoinColumn(unique = true)
    private Supplier supplier;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public Product productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getSku() {
        return sku;
    }

    public Product sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantityPerUnit() {
        return quantityPerUnit;
    }

    public Product quantityPerUnit(Integer quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
        return this;
    }

    public void setQuantityPerUnit(Integer quantityPerUnit) {
        this.quantityPerUnit = quantityPerUnit;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public Product discount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public Integer getMinOrder() {
        return minOrder;
    }

    public Product minOrder(Integer minOrder) {
        this.minOrder = minOrder;
        return this;
    }

    public void setMinOrder(Integer minOrder) {
        this.minOrder = minOrder;
    }

    public byte[] getPicture() {
        return picture;
    }

    public Product picture(byte[] picture) {
        this.picture = picture;
        return this;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPictureContentType() {
        return pictureContentType;
    }

    public Product pictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
        return this;
    }

    public void setPictureContentType(String pictureContentType) {
        this.pictureContentType = pictureContentType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Product unitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Category getCategory() {
        return category;
    }

    public Product category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Product supplier(Supplier supplier) {
        this.supplier = supplier;
        return this;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        if(product.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", productName='" + productName + "'" +
            ", productDescription='" + productDescription + "'" +
            ", sku='" + sku + "'" +
            ", quantityPerUnit='" + quantityPerUnit + "'" +
            ", discount='" + discount + "'" +
            ", minOrder='" + minOrder + "'" +
            ", picture='" + picture + "'" +
            ", pictureContentType='" + pictureContentType + "'" +
            ", unitPrice='" + unitPrice + "'" +
            '}';
    }
}
