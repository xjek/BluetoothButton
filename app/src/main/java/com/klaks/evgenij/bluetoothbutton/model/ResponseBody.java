package com.klaks.evgenij.bluetoothbutton.model;

import java.util.List;

public class ResponseBody {
    private int status = 0;
    private Button button;
    private Organization organization;
    private List<Tovar> tovar;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public List<Tovar> getTovars() {
        return tovar;
    }

    public void setTovars(List<Tovar> tovars) {
        this.tovar = tovars;
    }

    @Override
    public String toString() {
        return "ResponseBody{" +
                "status=" + status +
                ", button=" + button +
                ", organization=" + organization +
                ", tovars=" + tovar +
                '}';
    }
}
