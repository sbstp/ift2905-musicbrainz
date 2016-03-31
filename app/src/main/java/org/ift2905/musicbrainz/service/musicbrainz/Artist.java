package org.ift2905.musicbrainz.service.musicbrainz;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.List;

public class Artist implements Serializable {

    public String id;

    public String name;

    public String disambiguation;

    @Json(name="release-groups")
    public List<ReleaseGroup> releaseGroups;

    @Override
    public String toString() {
        return this.name;
    }
}
