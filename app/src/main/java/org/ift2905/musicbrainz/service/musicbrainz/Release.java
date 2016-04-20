package org.ift2905.musicbrainz.service.musicbrainz;

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

    public Release cloneWithoutRecordings() {
        Release r = new Release();

        r.id = id;
        r.name = name;
        r.year = year;
        r.month = month;
        r.day = day;
        r.areas = areas;
        r.recordings = null;

        return r;
    }

}
