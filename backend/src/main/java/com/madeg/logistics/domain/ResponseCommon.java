package com.madeg.logistics.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCommon {
    private String timestamp;
    private int status;
    private String message;

    public ResponseCommon(int status, String message) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.message = message;
    }
}
