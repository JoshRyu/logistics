package com.madeg.logistics.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(generator = "product_code_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "product_code_seq", sequenceName = "product_code_seq", allocationSize = 1)
    @Column(name = "product_code", unique = true, nullable = false)
    private String productCode;

    @ManyToOne
    @JoinColumn(name = "category_code", referencedColumnName = "category_code", nullable = false)
    private Category category;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "img")
    private String img;

    @Column(name = "barcode")
    private String barcode;

    @Column(name = "description")
    private String description;

}