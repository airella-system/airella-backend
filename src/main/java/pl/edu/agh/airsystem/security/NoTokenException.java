package pl.edu.agh.airsystem.security;

public class NoTokenException extends RuntimeException {
    public NoTokenException(String text) {
        super(text);
    }
}
