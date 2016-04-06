package org.ift2905.musicbrainz.service.musicbrainz;

import com.squareup.moshi.Json;

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.List;

public class ReleaseGroup implements Serializable {

    public String id;
    public String name;
    public String year;
    public String month;
    public String day;
    public List<Artist> credits;

    @Json(name="primary_type")
    public String primaryType;

    @Json(name="secondary_types")
    public List<String> secondaryTypes;

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
