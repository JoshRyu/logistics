package com.madeg.logistics.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Store")
@RestController
@RequestMapping(path = "/api/v1")
public class StoreController {

  @GetMapping(path = "/store/list")
  public List<String> getStoreList() {
    List<String> dummyStores = new ArrayList<>();

    dummyStores.add("펄블리샵");
    dummyStores.add("어떤어떤샵");
    dummyStores.add("강남대점샵");
    dummyStores.add("어메이징샵");
    return dummyStores;
  }
}
