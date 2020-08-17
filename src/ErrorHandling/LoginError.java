/**
 * Author: kcmodev
 * Email: *******@wgu.edu
 * Class: WGU C195 Software 2 Performance Assessment
 * Date Submitted: 8/16/2020
 */
package ErrorHandling;

public class LoginError extends Exception{

    /**
     * creates custom login error throwable exception
     * @param errorMessage
     */
    public LoginError(String errorMessage){
        super(errorMessage);
    }
}
