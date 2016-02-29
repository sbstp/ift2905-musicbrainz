package org.ift2905.musicbrainz.service;

import com.squareup.moshi.Json;

import java.util.GregorianCalendar;
import java.util.List;

public class ReleaseGroup {

    public String id;

    public String title;

    @Json(name="primary-type")
    public String primaryType;

    @Json(name="first-release-date")
    public GregorianCalendar releaseDate;

    @Json(name="secondary-types")
    public List<String> secondaryTypes;

    public boolean isAlbum() {
        return this.primaryType.equalsIgnoreCase(ReleaseGroupType.ALBUM);
    }

    public boolean isEP() {
        return this.primaryType.equalsIgnoreCase(ReleaseGroupType.EP);
    }

    public boolean isSingle() {
        return this.primaryType.equalsIgnoreCase(ReleaseGroupType.SINGLE);
    }

    private boolean hasSecondaryType(String secondaryType) {
        for (String type : this.secondaryTypes) {
            if (type.equalsIgnoreCase(secondaryType)) {
                return true;
            }
        }
        return false;
    }

    public boolean isCompilation() {
        return hasSecondaryType(ReleaseGroupType.COMPILATION);
    }

    public boolean isLive() {
        return hasSecondaryType(ReleaseGroupType.LIVE);
    }

    public boolean isRemix() {
        return hasSecondaryType(ReleaseGroupType.REMIX);
    }

    public boolean isSoundtrack() {
        return hasSecondaryType(ReleaseGroupType.SOUNDTRACK);
    }
}
