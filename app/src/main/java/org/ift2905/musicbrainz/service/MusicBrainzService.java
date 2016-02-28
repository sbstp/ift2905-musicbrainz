package org.ift2905.musicbrainz.service;

import android.util.Log;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicBrainzService {

    private static final int DEFAULT_TIMEOUT = 5000;

    private OkHttpClient httpClient;
    private Moshi moshi;
    private JsonAdapter<SearchArtistResult> artistAdapter;
    private JsonAdapter<ReleaseGroupResult> releaseGroupAdapter;

    public MusicBrainzService() {
        this.httpClient = new OkHttpClient();
        this.moshi = new Moshi.Builder().build();
        this.artistAdapter = this.moshi.adapter(SearchArtistResult.class);
        this.releaseGroupAdapter = this.moshi.adapter(ReleaseGroupResult.class);
    }

    private HttpUrl.Builder createUrl(String type) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host("musicbrainz.org")
                .addPathSegment("ws")
                .addPathSegment("2")
                .addPathSegment(type)
                .addQueryParameter("fmt", "json");
    }

    private Response tryGetResponse(HttpUrl url, int timeout) throws IOException, MusicBrainzServiceTimeout {
        long start = System.currentTimeMillis();
        Request req = new Request.Builder().url(url).get().build();
        Response res;

        while (true) {
            res = this.httpClient.newCall(req).execute();

            if (res.isSuccessful()) {
                break;
            }

            long now = System.currentTimeMillis();
            if (Math.abs(now - start) > timeout) {
                throw new MusicBrainzServiceTimeout();
            }
        }

        return res;
    }

    public List<Artist> searchArtist(String artist) throws IOException, MusicBrainzServiceTimeout {
        return searchArtist(artist, 10, DEFAULT_TIMEOUT);
    }

    public List<Artist> searchArtist(String artist, int limit, int timeout) throws IOException, MusicBrainzServiceTimeout {
        HttpUrl url = createUrl("artist")
                .addQueryParameter("query", artist)
                .addQueryParameter("limit", Integer.toString(limit))
                .build();

        Response res = tryGetResponse(url, timeout);
        SearchArtistResult se = this.artistAdapter.fromJson(res.body().source());
        return se.entries;
    }

    public List<ReleaseGroup> getDiscography(String artistId) throws IOException, MusicBrainzServiceTimeout {
        return getDiscography(artistId, ReleaseGroupType.ALL);
    }

    public List<ReleaseGroup> getDiscography(String artistId, String type) throws IOException, MusicBrainzServiceTimeout {
        return getDiscography(artistId, type, DEFAULT_TIMEOUT);
    }

    public List<ReleaseGroup> getDiscography(String artistId, String type, int timeout) throws IOException, MusicBrainzServiceTimeout {
        int count = 0;
        int max = -1;

        ArrayList<ReleaseGroup> entries = new ArrayList<>();

        do {
            HttpUrl url = createUrl("release-group")
                    .addQueryParameter("artist", artistId)
                    .addQueryParameter("limit", Integer.toString(100))
                    .addQueryParameter("offset", Integer.toString(count))
                    .addQueryParameter("type", type)
                    .build();

            Log.i("url", url.toString());
            Response res = tryGetResponse(url, timeout);
            ReleaseGroupResult group = this.releaseGroupAdapter.fromJson(res.body().source());

            if (max == -1) {
                max = group.count;
            }
            count += group.entries.size();

            entries.addAll(group.entries);
        } while (count < max);

        return entries;
    }

    private static class SearchArtistResult {

        @Json(name = "artists")
        public List<Artist> entries;
    }


    private static class ReleaseGroupResult {

        @Json(name = "release-group-offset")
        public int offset;

        @Json(name = "release-group-count")
        public int count;

        @Json(name = "release-groups")
        public List<ReleaseGroup> entries;

    }


}