package com.example.csar.kbc_news.modelos.cambio;

import com.example.csar.kbc_news.modelos.noticias.Source;

/**
 * Created by braya on 4/12/2018.
 */

public class Cambio {
    private String updated;
    private String source;
    private String target;
    private double value;
    private double quantity;
    private double amount;

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Cambio{" +
                "updated='" + updated + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", value=" + value +
                ", quantity=" + quantity +
                ", amount=" + amount +
                '}';
    }
}