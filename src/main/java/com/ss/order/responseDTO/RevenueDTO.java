package com.ss.order.responseDTO;

import java.util.Date;

public class RevenueDTO {
    private Date date;
    private double revenue;
    private int numberOfOrders;

    public RevenueDTO(Date date, double revenue, int numberOfOrders) {
        this.date = date;
        this.revenue = revenue;
        this.numberOfOrders = numberOfOrders;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public double getRevenue() {
        return revenue;
    }
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }
    public int getNumberOfOrders() {
        return numberOfOrders;
    }
    public void setNumberOfOrders(int numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }
}
