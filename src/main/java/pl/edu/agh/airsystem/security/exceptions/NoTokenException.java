package pl.edu.agh.airsystem.security.exceptions;

public class NoTokenException extends RuntimeException {
    public NoTokenException(String text) {
        super(text);
    }
}
