package org.ift2905.musicbrainz.service.musicbrainz;

import android.util.Log;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicBrainzService {

    private static final int DEFAULT_TIMEOUT = 5000;

    private OkHttpClient httpClient;
    private JsonAdapter<ArtistResult> artistAdapter;
    private JsonAdapter<ReleaseGroupResult> releaseGroupAdapter;
    private JsonAdapter<ReleaseResult> releaseAdapter;

    private MusicBrainzService() {
        this.httpClient = new OkHttpClient();

        Moshi moshi = new Moshi.Builder().add(new DateAdapter()).build();
        this.artistAdapter = moshi.adapter(ArtistResult.class);
        this.releaseGroupAdapter = moshi.adapter(ReleaseGroupResult.class);
        this.releaseAdapter = moshi.adapter(ReleaseResult.class);
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

            res.body().close();

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

        Log.i("url", url.toString());

        Response res = tryGetResponse(url, timeout);
        ArtistResult se = this.artistAdapter.fromJson(res.body().source());
        res.body().close();

        return se.entries;
    }

    public List<ReleaseGroup> searchReleaseGroup(String releaseGroup) throws IOException, MusicBrainzServiceTimeout {
        return searchReleaseGroup(releaseGroup, 10, DEFAULT_TIMEOUT);
    }

    public List<ReleaseGroup> searchReleaseGroup(String releaseGroup, int limit, int timeout) throws IOException, MusicBrainzServiceTimeout {
        HttpUrl url = createUrl("release-group")
                .addQueryParameter("query", releaseGroup)
                .addQueryParameter("limit", Integer.toString(limit))
                .build();

        Log.i("url", url.toString());

        Response res = tryGetResponse(url, timeout);
        ReleaseGroupResult se = this.releaseGroupAdapter.fromJson(res.body().source());
        res.body().close();

        return se.entries;
    }

    public List<ReleaseGroup> getReleaseGroups(String artistId) throws IOException, MusicBrainzServiceTimeout {
        return getReleaseGroups(artistId, DEFAULT_TIMEOUT);
    }

    public List<ReleaseGroup> getReleaseGroups(String artistId, int timeout) throws IOException, MusicBrainzServiceTimeout {
        int count = 0;
        int max = -1;

        List<Release> entries = new ArrayList<>();

        do {
            HttpUrl url = createUrl("release")
                    .addQueryParameter("artist", artistId)
                    .addQueryParameter("limit", "100")
                    .addQueryParameter("offset", Integer.toString(count))
                    .addQueryParameter("type", "album|single|ep")
                    .addQueryParameter("inc", "release-groups")
                    .addQueryParameter("status", "official")
                    .build();

            Log.i("url", url.toString());

            Response res = tryGetResponse(url, timeout);
            ReleaseResult group = this.releaseAdapter.fromJson(res.body().source());
            res.body().close();

            if (max == -1) {
                max = group.count;
            }
            count += group.entries.size();

            entries.addAll(group.entries);
        } while (count < max);

        return releasesToReleaseGroups(entries);
    }

    private List<ReleaseGroup> releasesToReleaseGroups(List<Release> releases) {
        HashMap<String, ReleaseGroup> groups = new HashMap<>();
        ReleaseGroup group;

        for (Release r : releases) {
            group = groups.get(r.releaseGroup.id);
            if (group == null) {
                group = r.releaseGroup;
                groups.put(group.id, group);
            }
            if (group.releases == null) {
                group.releases = new ArrayList<>();
            }
            group.releases.add(r);
        }

        return new ArrayList<>(groups.values());
    }

    public List<Release> getReleases(String releaseGroupId) throws IOException, MusicBrainzServiceTimeout {
        return getReleases(releaseGroupId, DEFAULT_TIMEOUT);
    }

    public List<Release> getReleases(String releaseGroupId, int timeout) throws IOException, MusicBrainzServiceTimeout {
        HttpUrl url = createUrl("release")
                .addQueryParameter("release-group", releaseGroupId)
                .addQueryParameter("limit", "25")
                .addQueryParameter("inc", "recordings")
                .build();

        Response res = tryGetResponse(url, timeout);
        ReleaseResult rel = this.releaseAdapter.fromJson(res.body().source());
        res.body().close();

        return rel.entries;
    }

    private static class ArtistResult {

        @Json(name = "artists")
        public List<Artist> entries;
    }

    private static class ReleaseGroupResult {

        @Json(name = "release-group-count")
        public int count;

        @Json(name = "release-groups")
        public List<ReleaseGroup> entries;

    }

    private static class ReleaseResult {

        @Json(name = "release-count")
        public int count;

        @Json(name = "releases")
        public List<Release> entries;
    }


}