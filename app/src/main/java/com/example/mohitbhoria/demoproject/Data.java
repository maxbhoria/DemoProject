package com.example.mohitbhoria.demoproject;

import java.util.*;


public class Data {
    String error_string;
    int error_code;
    String result;
    ArrayList<Details> data;

    public Data(String error_string, int error_code, String result, ArrayList<Details> data) {
        this.error_string = error_string;
        this.error_code = error_code;
        this.result = result;
        this.data = data;
    }

    public String getError_string() {
        return error_string;
    }

    public void setError_string(String error_string) {
        this.error_string = error_string;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<Details> getData() {
        return data;
    }

    public void setData(ArrayList<Details> data) {
        this.data = data;
    }
}