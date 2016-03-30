package org.ift2905.musicbrainz.service.musicbrainz;

import java.io.Serializable;

public class ArtistCredit implements Serializable {

    public Artist artist;

    @Override
    public String toString() {
        return artist.toString();
    }

}
