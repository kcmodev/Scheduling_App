package models;

import dao.CustomerDAO;

import java.sql.SQLException;

public class Appointment {
    private int appointmentId;
    private int customerId;
    private int addressId;
    private int userId;
    private String type;
    private String startTime;
    private String startDate;
    private String endTime;
    private String name;
    private String address;
    private String phone;


    public Appointment(int customerId, String type, String startTime) throws SQLException {
        System.out.println("in appointment constructor");

        this.customerId = customerId;
        this.type = type;
        this.name = CustomerDAO.getCustomerName(customerId);
        System.out.println("name found: " + this.name);
        this.address = CustomerDAO.getCustomerAddress(customerId);
        System.out.println("address found: " + this.address);
        this.phone = CustomerDAO.getCustomerPhone(customerId);
        System.out.println("phone found: " + this.phone);
        this.startTime = startTime;
        this.startDate = startTime;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStartDate(){
        return startDate;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
