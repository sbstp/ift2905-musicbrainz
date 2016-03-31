package org.ift2905.musicbrainz.service.musicbrainz;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

public class ReleaseGroup implements Serializable {

    public String id;

    public String title;

    @Json(name="primary-type")
    public String primaryType;

    @Json(name="first-release-date")
    public GregorianCalendar releaseDate;

    public List<Release> releases;

    @Json(name="secondary-types")
    public List<String> secondaryTypes;

    @Json(name="artist-credit")
    public List<ArtistCredit> artistCredits;

    public boolean hasPrimaryType(String primaryType) {
        return this.primaryType.equalsIgnoreCase(primaryType);
    }

    public boolean hasSecondaryType(String secondaryType) {
        for (String type : this.secondaryTypes) {
            if (type.equalsIgnoreCase(secondaryType)) {
                return true;
            }
        }
        return false;
    }

}
