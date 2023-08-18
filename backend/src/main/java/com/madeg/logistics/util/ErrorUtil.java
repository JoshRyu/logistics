package com.madeg.logistics.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class ErrorUtil {

  public Map<String, String> getErrorMessages(Errors errors) {
    Map<String, String> errorMap = new HashMap<>();

    errors
      .getFieldErrors()
      .forEach(err -> errorMap.put(err.getField(), err.getDefaultMessage()));

    return errorMap;
  }
}
