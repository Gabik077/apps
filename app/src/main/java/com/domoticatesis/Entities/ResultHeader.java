package com.domoticatesis.Entities;

import java.util.List;

/**
 * Created by gabriel_cabrera on 18/4/18.
 */

public class ResultHeader {

    private int status;
    private String msg;
    private int pid;
    private TemperatureModel temperature;
    private HumidityModel humidity;
    private List<ExpenseModel> expense_list;
    private double totalExpense;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TemperatureModel getTemperature() {
        return temperature;
    }

    public void setTemperature(TemperatureModel temperature) {
        this.temperature = temperature;
    }

    public HumidityModel getHumidity() {
        return humidity;
    }

    public void setHumidity(HumidityModel humidity) {
        this.humidity = humidity;
    }

    public List<ExpenseModel> getExpense_list() {
        return expense_list;
    }

    public void setExpense_list(List<ExpenseModel> expense_list) {
        this.expense_list = expense_list;
    }

    public double getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(double totalExpense) {
        this.totalExpense = totalExpense;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }
}
