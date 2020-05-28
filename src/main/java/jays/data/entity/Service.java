package jays.data.entity;

public class Service {
    private int service_id;
    private String service_name;
    private boolean service_status;
    private float service_price;
    private float service_profit;
    private String service_category;

    public Service(int service_id, String service_name, boolean service_status, float service_price, float service_profit, String service_category) {
        this.service_id = service_id;
        this.service_name = service_name;
        this.service_status = service_status;
        this.service_price = service_price;
        this.service_profit = service_profit;
        this.service_category = service_category;
    }

    public int getService_id() {
        return service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public boolean isService_status() {
        return service_status;
    }

    public void setService_status(boolean service_status) {
        this.service_status = service_status;
    }

    public float getService_price() {
        return service_price;
    }

    public void setService_price(float service_price) {
        this.service_price = service_price;
    }

    public float getService_profit() {
        return service_profit;
    }

    public void setService_profit(float service_profit) {
        this.service_profit = service_profit;
    }

    public String getService_category() {
        return service_category;
    }

    public void setService_category(String service_category) {
        this.service_category = service_category;
    }
}
