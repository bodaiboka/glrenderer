package hu.richardbodai.glrenderer.exceptions;

/**
 * Created by richardbodai on 5/2/16.
 */
public class NoProgramForKeyException extends RuntimeException {

    String message;

    public NoProgramForKeyException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
