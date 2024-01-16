package com.madeg.logistics.domain;

import com.madeg.logistics.enums.ProductSearchType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductListReq {

    private String searchKeyWord;

    private ProductSearchType searchType;

}
