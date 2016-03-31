package org.ift2905.musicbrainz.service.musicbrainz;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class Release implements Serializable {

    public String id;
    public String date;
    public String title;

    @Json(name = "cover-art-archive")
    public ConvertArt covertArt;

    @Json(name = "release-group")
    public ReleaseGroup releaseGroup;

    public List<Media> media;

    public static class ConvertArt implements Serializable {
        public int count;
        public boolean back;
        public boolean artwork;
        public boolean front;
    }

    public static class Media implements Serializable {
        public String title;
        public List<Track> tracks;
    }

    public static class Track implements Serializable {
        public String id;
        public String title;
        public int number;
        public int length;
    }

}
