package org.ift2905.musicbrainz.service.musicbrainz;

public class Artist {

    public String id;

    public String name;

    public String disambiguation;

    @Override
    public String toString() {
        return this.name;
    }
}
