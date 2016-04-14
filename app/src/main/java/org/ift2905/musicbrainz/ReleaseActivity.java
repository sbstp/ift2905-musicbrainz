package org.ift2905.musicbrainz;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.Recording;
import org.ift2905.musicbrainz.service.musicbrainz.Release;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

import java.io.IOException;
import java.util.List;

public class ReleaseActivity extends AppCompatActivity {

    private Artist artist;
    private ReleaseGroup releaseGroup;

    private TextView artiste;
    private TextView album;
    private ListView listView;
    private ImageView imageView;
    private MusicBrainzService service;
    private Release release;
    private List<Release> releases;
    private List<Recording> recordings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");
        releaseGroup = (ReleaseGroup) intent.getSerializableExtra("releaseGroup");

        artiste = (TextView) findViewById(R.id.artiste);
        album = (TextView) findViewById(R.id.album);
        listView = (ListView) findViewById(R.id.listView);
        imageView = (ImageView) findViewById(R.id.imageView);


        artiste.setText(artist.name);
        album.setText(releaseGroup.name);

        Picasso.with(getApplicationContext())
                .load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id))
                .placeholder(R.drawable.release_group_placeholder)
                .into(imageView);

        RunAPI run = new RunAPI();
        run.execute();
    }

    public class RunAPI extends AsyncTask<String, Object, Release> {
        @Override
        protected void onPostExecute(Release release) {
            super.onPostExecute(release);
            listView.setAdapter(new Adapter());
        }

        @Override
        protected Release doInBackground(String... params) {
            service = MusicBrainzService.getInstance();
            releases = null;

            try {
                releases = service.getReleases(releaseGroup);
            } catch (IOException e) {
                e.printStackTrace();
            }
            release = releases.get(0);

            recordings = release.recordings;

            return release;
        }
    }

    private class Adapter extends BaseAdapter {

        private LayoutInflater inflateur;

        public Adapter() {
            inflateur = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return recordings.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent) {
            if (v == null) {
                v = inflateur.inflate(R.layout.list_release_track, parent, false);
            }

            TextView number = (TextView) v.findViewById(R.id.number);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView duration = (TextView) v.findViewById(R.id.duration);

            Recording rec = recordings.get(position);

            if (position % 2 == 0) {
                v.setBackgroundColor(Color.WHITE);
            } else {
                v.setBackgroundColor(Color.LTGRAY);
            }

            number.setText(String.format("%02d", position + 1));
            title.setText(rec.name);
            duration.setText(secondsToText(Integer.parseInt(rec.length)));

            return v;
        }

        private String secondsToText(int secs) {
            secs /= 1000;
            int minutes = secs / 60;
            int seconds = secs % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }

    }
}

