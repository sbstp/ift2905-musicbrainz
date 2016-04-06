package org.ift2905.musicbrainz.service.musicbrainz;

import android.util.Log;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicBrainzService {

    private OkHttpClient httpClient;
    private JsonAdapter<List<Artist>> artistAdapter;
    private JsonAdapter<List<ReleaseGroup>> releaseGroupAdapter;
    private JsonAdapter<List<Release>> releaseAdapter;

    private MusicBrainzService() {
        this.httpClient = new OkHttpClient();

        Moshi moshi = new Moshi.Builder().build();
        this.artistAdapter = moshi.adapter(Types.newParameterizedType(List.class, Artist.class));
        this.releaseGroupAdapter = moshi.adapter(Types.newParameterizedType(List.class, ReleaseGroup.class));
        this.releaseAdapter = moshi.adapter(Types.newParameterizedType(List.class, Release.class));
    }

    // singleton
    private static MusicBrainzService service;

    public static MusicBrainzService getInstance() {
        if (service == null) {
            service = new MusicBrainzService();
        }
        return service;
    }

    private HttpUrl.Builder createUrl(String type) {
        return new HttpUrl.Builder()
                .scheme("http")
                .host("musicbrainz.sbstp.me")
                .addPathSegment(type);
    }

    public List<Artist> searchArtist(String artist) throws IOException {
        HttpUrl url = createUrl("search-artist.php")
                .addQueryParameter("query", artist)
                .build();

        Log.i("url", url.toString());

        Request req = new Request.Builder().url(url).get().build();
        Response res = this.httpClient.newCall(req).execute();
        List<Artist> artists = this.artistAdapter.fromJson(res.body().source());
        res.body().close();

        return artists;
    }

    public List<ReleaseGroup> searchReleaseGroup(String releaseGroup) throws IOException {
        HttpUrl url = createUrl("search-release-group.php")
                .addQueryParameter("query", releaseGroup)
                .build();

        Log.i("url", url.toString());

        Request req = new Request.Builder().url(url).get().build();
        Response res = this.httpClient.newCall(req).execute();
        List<ReleaseGroup> releaseGroups = this.releaseGroupAdapter.fromJson(res.body().source());
        res.body().close();

        return releaseGroups;
    }

    public List<ReleaseGroup> getReleaseGroups(Artist artist) throws IOException {
        HttpUrl url = createUrl("release-groups.php")
                .addQueryParameter("artist", artist.id)
                .build();

        Log.i("url", url.toString());

        Request req = new Request.Builder().url(url).get().build();
        Response res = this.httpClient.newCall(req).execute();
        List<ReleaseGroup> releaseGroups = this.releaseGroupAdapter.fromJson(res.body().source());
        res.body().close();

        return releaseGroups;
    }
    public List<Release> getReleases(ReleaseGroup releaseGroup) throws IOException {
        HttpUrl url = createUrl("releases.php")
                .addQueryParameter("release-group", releaseGroup.id)
                .build();

        Log.i("url", url.toString());

        Request req = new Request.Builder().url(url).get().build();
        Response res = this.httpClient.newCall(req).execute();
        List<Release> releases = this.releaseAdapter.fromJson(res.body().source());
        res.body().close();

        return releases;
    }


}