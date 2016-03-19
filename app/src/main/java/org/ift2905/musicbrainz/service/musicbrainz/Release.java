package org.ift2905.musicbrainz.service.musicbrainz;

import com.squareup.moshi.Json;

import java.util.List;

public class Release {

    public String id;
    public String date;
    public String title;

    @Json(name = "cover-art-archive")
    public ConvertArt covertArt;

    public List<Media> media;

    public static class ConvertArt {
        public int count;
        public boolean back;
        public boolean artwork;
        public boolean front;
    }

    public static class Media {
        public String title;
        public List<Track> tracks;
    }

    public static class Track {
        public String id;
        public String title;
        public int number;
        public int length;
    }

}
