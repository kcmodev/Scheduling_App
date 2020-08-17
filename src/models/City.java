/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package models;

public class City {
    private int cityId;
    private String cityName;
    private int countryId;

    /**
     * constructor to instantiate city objects
     * @param cityId
     * @param cityName
     * @param countryId
     */
    public City(int cityId, String cityName, int countryId) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryId = countryId;
    }

//    public int getCityId() {
//        return cityId;
//    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

//    public String getCityName() {
//        return cityName;
//    }
//
//    public void setCityName(String cityName) {
//        this.cityName = cityName;
//    }
//
//    public int getCountryId() {
//        return countryId;
//    }
//
//    public void setCountryId(int countryId) {
//        this.countryId = countryId;
//    }
}
