package com.madeg.logistics.domain;

import org.springframework.data.jpa.domain.Specification;
import com.madeg.logistics.entity.Product;
import com.madeg.logistics.enums.CompareType;
import com.madeg.logistics.enums.ProductType;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {

    public static Specification<Product> withDynamicQuery(ProductType type, ProductListReq productListReq) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null) {
                predicates.add(builder.equal(root.get("type"), type));
            }

            if (productListReq != null && productListReq.getSearchKeyWord() != null
                    && !productListReq.getSearchKeyWord().isEmpty()) {
                switch (productListReq.getSearchType()) {
                    case PRODUCT_NAME:
                        predicates.add(builder.like(root.get("name"), "%" + productListReq.getSearchKeyWord() + "%"));
                        break;
                    case CATEGORY_NAME:
                        predicates.add(builder.like(root.get("category").get("name"),
                                "%" + productListReq.getSearchKeyWord() + "%"));
                        break;
                    case STOCK:
                        addComparablePredicate(predicates, builder, root, "stock",
                                Double.parseDouble(productListReq.getSearchKeyWord()), productListReq.getCompareType());
                        break;
                    case PRICE:
                        addComparablePredicate(predicates, builder, root, "price",
                                new BigDecimal(productListReq.getSearchKeyWord()), productListReq.getCompareType());
                        break;
                    case DESCRIPTION:
                        predicates.add(
                                builder.like(root.get("description"), "%" + productListReq.getSearchKeyWord() + "%"));
                        break;
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static <T extends Comparable<T>> void addComparablePredicate(List<Predicate> predicates,
            CriteriaBuilder builder, Root<Product> root, String fieldName, T value, CompareType compareType) {
        switch (compareType) {
            case E:
                predicates.add(builder.equal(root.get(fieldName), value));
                break;
            case GT:
                predicates.add(builder.greaterThan(root.get(fieldName), value));
                break;
            case LT:
                predicates.add(builder.lessThan(root.get(fieldName), value));
                break;
            case GTE:
                predicates.add(builder.greaterThanOrEqualTo(root.get(fieldName), value));
                break;
            case LTE:
                predicates.add(builder.lessThanOrEqualTo(root.get(fieldName), value));
                break;
        }
    }
}
