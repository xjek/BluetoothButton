package com.klaks.evgenij.bluetoothbutton.model;


public class Result {
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isError() {
        return result == null || result.isEmpty() || result.equals("error");
    }

    @Override
    public String toString() {
        return "Result{" +
                "result='" + result + '\'' +
                '}';
    }
}
