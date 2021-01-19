package com.example.furniture.Models;

public class Response {

    //Deklarasi data model response untuk response yang akan diparsing nanti

    private int code;
    private String message;

    public Response() {
    }

    public Response(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
