/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package models;

import dao.CustomerDAO;

import java.sql.SQLException;

public class Appointment {
    private static final CustomerDAO customerData = new CustomerDAO();

    private int appointmentId;
    private int customerId;
    private int userId;
    private String type;
    private String startTime;
    private String startDate;
    private String name;
    private String address;
    private String phone;

    /**
     * constructor to instantiate appointment objects
     * @param appointmentId
     * @param customerId
     * @param type
     * @param startDate
     * @param startTime
     * @throws SQLException
     */
    public Appointment(int appointmentId, int customerId, String type, String startDate, String startTime) throws SQLException {
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.type = type;
        this.name = customerData.getCustomerName(customerId);
        this.address = customerData.getCustomerAddress(customerId);
        this.phone = customerData.getCustomerPhone(customerId);
        this.startDate = startDate;
        this.startTime = startTime;
    }

    /**
     * getters and setters
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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
