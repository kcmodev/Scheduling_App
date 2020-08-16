/**
 * Author: Steven Christensen
 * Email: schr206@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */
package ErrorHandling;

public class InvalidInput extends Exception{

    /**
     * creates custom error for invalid input warnings
     * @param message
     */
    public InvalidInput(String message){
        super(message);
    }
}
