package org.ift2905.musicbrainz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

public class DiscographyActivity extends AppCompatActivity {

    private Artist artist;
    private ReleaseGroup releaseGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discography);
        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");
        releaseGroup = (ReleaseGroup) intent.getSerializableExtra("releaseGroup");

        ((TextView) findViewById(R.id.textView)).setText(artist.name);
        if (releaseGroup != null) {
            ((TextView) findViewById(R.id.textView2)).setText(releaseGroup.title);
        }
    }
}
