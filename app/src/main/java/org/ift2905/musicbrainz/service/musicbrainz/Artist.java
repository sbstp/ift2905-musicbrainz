package org.ift2905.musicbrainz.service.musicbrainz;

import java.io.Serializable;

public class Artist implements Serializable {

    public String id;

    public String name;

    public String disambiguation;

    @Override
    public String toString() {
        return this.name;
    }
}
