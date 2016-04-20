package org.ift2905.musicbrainz.service.musicbrainz;

import java.io.Serializable;

public class Recording implements Serializable {

    public String id;
    public String name;
    public String length;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof  Recording)) return false;
        return name.equals(((Recording) o).name);
    }
}
