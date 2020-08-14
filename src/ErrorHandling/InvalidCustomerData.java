package ErrorHandling;

public class InvalidCustomerData extends Exception{

    /**
     * creates custom invalid customer throwable exception
     * @param message
     */
    public InvalidCustomerData(String message){ super(message);}
}
