package com.madeg.logistics.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "custom_sequence")
    @GenericGenerator(name = "custom_sequence", strategy = "com.madeg.logistics.entity.CustomSequenceGenerator", parameters = {
            @Parameter(name = "prefix", value = "category_code_")
    })
    @Column(name = "category_code", unique = true, nullable = false)
    private String categoryCode;

    @ManyToOne
    @JoinColumn(name = "parent_category_code", referencedColumnName = "category_code")
    private Category parentCategory;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

}
