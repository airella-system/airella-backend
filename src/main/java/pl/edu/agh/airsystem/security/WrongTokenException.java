package pl.edu.agh.airsystem.security;

public class WrongTokenException extends RuntimeException {
    public WrongTokenException(String text) {
        super(text);
    }
}
