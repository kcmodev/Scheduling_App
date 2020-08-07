package models;

public class Address {
    private int addressId;
    private String address;
    private String cityId;
    private int postalCode;
    private int phone;

    public Address(int addressId, String address, String cityId, int postalCode, int phone) {
        this.addressId = addressId;
        this.address = address;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
    }


}
