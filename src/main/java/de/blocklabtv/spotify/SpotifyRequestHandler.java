package de.blocklabtv.spotify;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;

import java.io.IOException;

public class SpotifyRequestHandler {

    private final SpotifyCredentials credentials;

    private SpotifyApi spotifyApi;

    public SpotifyRequestHandler(SpotifyCredentials credentials) {
        this.credentials = credentials;
    }

    public SpotifyRequestHandler refreshAccessToken() {
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = this.spotifyApi.authorizationCodeRefresh()
                .build();

        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }

        return this;
    }

    public SpotifyRequestHandler instantiateApi() {
        this.spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.credentials.getClientId())
                .setClientSecret(this.credentials.getClientSecret())
                .setRefreshToken(this.credentials.getRefreshToken())
                .build();
        return this;
    }

    public Paging<PlaylistTrack> receivePlaylist(String playlistId) throws IOException, SpotifyWebApiException {
        final GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi
                .getPlaylistsTracks(playlistId)
                .build();
        return getPlaylistsTracksRequest.execute();
    }

}
