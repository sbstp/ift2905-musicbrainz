package org.ift2905.musicbrainz.service.musicmap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MusicMapService {

    private OkHttpClient httpClient;

    public MusicMapService() {
        this.httpClient = new OkHttpClient();
    }

    private HttpUrl createUrl(String artist) {
        String segment = String.format("%s.html", artist.toLowerCase());

        return new HttpUrl.Builder()
                .scheme("http")
                .host("music-map.com")
                .addPathSegment(segment)
                .build();
    }

    public List<String> getSimilarArtists(String artist) throws IOException, MusicMapServiceError {
        HttpUrl url = createUrl(artist);

        Request req = new Request.Builder().url(url).get().build();
        Response res = this.httpClient.newCall(req).execute();
        if (!res.isSuccessful()) {
            throw new MusicMapServiceError();
        }

        List<String> artists = new ArrayList<>();
        Document doc = Jsoup.parse(res.body().byteStream(), null, url.toString());
        Element gnodMap = doc.getElementById("gnodMap");
        Elements items = gnodMap.getElementsByTag("a");

        for (Element item : items.subList(1, items.size())) {
            artists.add(item.text());
        }

        return artists;
    }

}
