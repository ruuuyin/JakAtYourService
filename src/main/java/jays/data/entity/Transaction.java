package jays.data.entity;

public class Transaction {
    private int id;
    private String customer;
    private float amount;
    private float income;
    private String date;
    private String service;

    public Transaction(int id, String customer, float amount, float income, String date, String service) {
        this.id = id;
        this.customer = customer;
        this.amount = amount;
        this.income = income;
        this.date = date;
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
