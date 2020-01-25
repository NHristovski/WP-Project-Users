package hristovski.nikola.users.exceptions;

public class NoUserFoundException extends RuntimeException {

    private static final String MESSAGE = "Failed to find user";
    public NoUserFoundException(){
        super(MESSAGE);
    }

    public NoUserFoundException(String usernmae){
        super(MESSAGE + " with username " + usernmae);
    }
}
