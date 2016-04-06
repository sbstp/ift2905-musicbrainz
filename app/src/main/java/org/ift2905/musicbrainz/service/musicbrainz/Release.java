package org.ift2905.musicbrainz.service.musicbrainz;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class Release implements Serializable {

    public String id;
    public String name;
    public String year;
    public String month;
    public String day;
    public List<String> areas;
    public List<Recording> recordings;

}
