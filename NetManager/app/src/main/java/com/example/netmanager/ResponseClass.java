package com.example.netmanager;

public class ResponseClass {
     private int resultcode;
     private String reason;

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    @Override
    public String toString() {
        return "ResponseClass{" +
                "resultcode=" + resultcode +
                ", reason='" + reason + '\'' +
                '}';
    }
}
