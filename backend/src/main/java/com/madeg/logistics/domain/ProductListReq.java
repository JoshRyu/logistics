package com.madeg.logistics.domain;

import com.madeg.logistics.enums.ProductSearchType;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductListReq {

    @NotBlank(message = "한 글자 이상 검색하셔야 합니다")
    private String searchKeyWord;

    private ProductSearchType searchType;

}
