package com.madeg.logistics.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRefreshRes extends CommonRes {

    private String accessToken;

    public UserRefreshRes(int status, String message, String accessToken) {
        super(status, message);
        this.accessToken = accessToken;
    }
}
