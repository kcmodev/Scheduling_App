/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package ErrorHandling;


public class AppointmentTimeWarning extends Exception{

    /**
     * creates customer error for appointment time warnings
     * @param message
     */
    public AppointmentTimeWarning (String message) {
        super(message);
    }
}
