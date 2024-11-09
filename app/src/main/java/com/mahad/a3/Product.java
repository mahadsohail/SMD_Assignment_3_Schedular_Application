package com.mahad.a3;

public class Product {
    private int id;
    private String name;
    private String date;
    private int price;
    private String status;  // Add status for the product

    public Product(int id, String name, String date, int price, String status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.price = price;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Date: " + date + ", Price: $" + price;
    }
}
