package de.blocklabtv;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import de.blocklabtv.exceptions.PlaylistsStringNotPresentException;
import de.blocklabtv.spotify.SpotifyCredentials;
import de.blocklabtv.spotify.SpotifyRequestHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LambdaRequestStreamHandler implements RequestStreamHandler {

    private final String PLAYLISTS_ENVAR_KEY = "playlists";

    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) {
        final SpotifyCredentials credentials = new SpotifyCredentials(System.getenv("refreshToken"),
                System.getenv("clientId"), System.getenv("clientSecret"));
        final SpotifyRequestHandler requestHandler = new SpotifyRequestHandler(credentials).instantiateApi().refreshAccessToken();
        context.getLogger().log("RequestHandler has been initiated");

        final String playlistString = System.getenv(PLAYLISTS_ENVAR_KEY);
        if (playlistString == null) {
            throw new PlaylistsStringNotPresentException();
        }
        String[] playlistIds = playlistString.split(",");
        for (String playlistId : playlistIds) {
            context.getLogger().log("Parsing playlist: " + playlistId);
            try {
                Paging<PlaylistTrack> tracks = requestHandler.receivePlaylist(playlistId);
                context.getLogger().log("Found a total of " + tracks.getTotal() + " items.");
                for(PlaylistTrack track : tracks.getItems()) {
                    context.getLogger().log("FoundItem: " + track.getTrack().getName());
                }
            } catch (IOException | SpotifyWebApiException e) {
                e.printStackTrace();
            }
        }
    }
}