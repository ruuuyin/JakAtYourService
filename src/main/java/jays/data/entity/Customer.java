package jays.data.entity;

public class Customer {
    private int customer_id;
    private String customer_first;
    private String customer_middle;
    private String customer_last;
    private String customer_phone;

    public Customer(int customer_id, String customer_first, String customer_middle, String customer_last, String customer_phone) {
        this.customer_id = customer_id;
        this.customer_first = customer_first;
        this.customer_middle = customer_middle;
        this.customer_last = customer_last;
        this.customer_phone = customer_phone;
    }

    public String getCustomer_first() {
        return customer_first;
    }

    public void setCustomer_first(String customer_first) {
        this.customer_first = customer_first;
    }

    public String getCustomer_middle() {
        return customer_middle;
    }

    public void setCustomer_middle(String customer_middle) {
        this.customer_middle = customer_middle;
    }

    public String getCustomer_last() {
        return customer_last;
    }

    public void setCustomer_last(String customer_last) {
        this.customer_last = customer_last;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    @Override
    public String toString() {
        return  customer_id +" - " + customer_first + " " + customer_middle +" "+ customer_last;
    }
}
