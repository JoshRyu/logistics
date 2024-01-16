package com.madeg.logistics.enums;

import java.text.MessageFormat;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS(HttpStatus.OK, "{0}에 성공하였습니다"),
    RETRIEVED(HttpStatus.OK, "{0} 조회에 성공하였습니다."),
    UPDATED(HttpStatus.OK, "{0} 업데이트에 성공하였습니다."),
    CREATED(HttpStatus.CREATED, "{0}이(가) 생성되었습니다."),
    DELETED(HttpStatus.NO_CONTENT, null),

    UNCHANGED(HttpStatus.NO_CONTENT, "{0}은(는) 변경 사항이 없어 업데이트되지 않았습니다."),
    CONFLICT(HttpStatus.CONFLICT, "{0}이(가) 이미 존재합니다."),
    BADREQUEST(HttpStatus.BAD_REQUEST, "{0}."),
    NOTFOUND(HttpStatus.NOT_FOUND, "{0}을(를) 찾을 수 없습니다."),
    INTERNALERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error");

    private final HttpStatus status;
    private final String message;

    private ResponseCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage(String... args) {
        return MessageFormat.format(this.message, (Object[]) args);
    }

    public HttpStatus getStatus() {
        return this.status;
    }

    public int getCode() {
        return status.value();
    }

}
