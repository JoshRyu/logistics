package com.madeg.logistics.entity;

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
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(generator = "category_code_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_code_seq", sequenceName = "category_code_seq", allocationSize = 1, initialValue = 1)
    @Column(name = "category_code", unique = true, nullable = false)
    private String categoryCode;

    @ManyToOne
    @JoinColumn(name = "parent_category_code", referencedColumnName = "category_code")
    private Category parentCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

}
