package com.event.exception;

import lombok.Getter;

@Getter
public enum MessageType {

    TOKEN_IS_EXPIRED("1001","token süresi dolmuş"),
    NO_RECORD_EXIST("1002", "kayıt bulunamadı"),
    USER_ALREADY_EXISTS("1003", "Kullanıcı zaten mevcut"),
    REGISTER_FAILED("1004", "Kayıt yapılamadı"),
    AUTHENTICATION_FAILED("1005", "Kimlik doğrulama hatası"),
    CREATE_EVENT_FAILED("1006", "Etkinlik oluşturulamadı"),
    USER_NOT_FOUND("1007","Kullanıcı bulunamadı"),
    DUPLICATE_REQUEST("1008", "Aynı kayıt bulundu "),
    CAPACITY_FULL("1009", "Etkinlik kapasitesi dolmuş"),
    INVALID_REQUEST("1010", "Geçersiz giriş"),
    GENERAL_EXCEPTION("9999", "genel bir hata oluştu");
    private String code;
    private String message;

    MessageType(String code, String message){
        this.code= code;
        this.message= message;
    }
}
