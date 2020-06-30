package io.rackshift.model;

public class RSException extends RuntimeException {

    public RSException(String message) {
        super(message);
    }

    public static RSException throwExceptions(String msg) {
        throw new RSException(msg);
    }
}
