/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package models;

public class Address {
    private int addressId;
    private String address;
    private String cityId;
    private int postalCode;
    private int phone;

    /**
     * constructor to instantiate address objects
     * @param addressId
     * @param address
     * @param cityId
     * @param postalCode
     * @param phone
     */
    public Address(int addressId, String address, String cityId, int postalCode, int phone) {
        this.addressId = addressId;
        this.address = address;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
    }
}
