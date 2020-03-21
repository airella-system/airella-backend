package pl.edu.agh.airsystem.security.exceptions;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException(String text) {
        super(text);
    }
}
