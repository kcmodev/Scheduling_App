/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */

package ErrorHandling;

public class InvalidCustomerData extends Exception{

    /**
     * creates custom invalid customer throwable exception
     * @param message
     */
    public InvalidCustomerData(String message){ super(message);}
}
