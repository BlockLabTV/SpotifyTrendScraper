package de.blocklabtv.exceptions;

public class SpotifyCredentialsMissingException extends RuntimeException {

    public SpotifyCredentialsMissingException(String s) {
        super("Missing the environment variable '" + s + "'");
    }
}
