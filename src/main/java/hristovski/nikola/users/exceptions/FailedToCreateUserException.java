package hristovski.nikola.users.exceptions;

public class FailedToCreateUserException extends RuntimeException {

    public FailedToCreateUserException(){
        super();
    }

    public FailedToCreateUserException(String message){
        super(message);
    }
}
