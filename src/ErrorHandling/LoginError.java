package ErrorHandling;

public class LoginError extends Exception{

    public LoginError(String errorMessage){
        super(errorMessage);
    }
}
