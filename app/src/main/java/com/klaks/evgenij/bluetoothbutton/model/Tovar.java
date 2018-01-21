package com.klaks.evgenij.bluetoothbutton.model;

import java.util.Locale;

public class Tovar {
    private int id = 0;
    private String name = "";
    private String image = "";
    private double price = 0.0;
    private String description = "";
    private int count = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getStringPrice() {
        return getFormatPrice(price);
    }

    public String getStringCount() {
        return getFormatCount(count);
    }

    public static String getFormatPrice(double price) {
        return String.format(Locale.getDefault(),"%1.2f р.", price);
    }

    public static String getFormatCount(int count) {
        return String.format(Locale.getDefault(), "%1d", count);
    }

    @Override
    public String toString() {
        return "Tovar{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", count=" + count +
                '}';
    }
}
