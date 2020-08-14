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
