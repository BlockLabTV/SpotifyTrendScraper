package de.blocklabtv.spotify;

import de.blocklabtv.exceptions.SpotifyCredentialsMissingException;

public class SpotifyCredentials {

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    private final String refreshToken, clientId, clientSecret;


    public SpotifyCredentials(String refreshToken, String clientId, String clientSecret) {
        if (refreshToken == null) {
            throw new SpotifyCredentialsMissingException("refreshToken");
        }
        if (clientId == null) {
            throw new SpotifyCredentialsMissingException("clientId");
        }
        if (clientSecret == null) {
            throw new SpotifyCredentialsMissingException("clientSecret");
        }
        this.refreshToken = refreshToken;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }
}
