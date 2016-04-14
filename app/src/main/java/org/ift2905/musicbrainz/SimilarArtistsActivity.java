package org.ift2905.musicbrainz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicmap.MusicMapService;
import org.ift2905.musicbrainz.service.musicmap.MusicMapServiceError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.ift2905.musicbrainz.R.layout.activity_similar_artists;

public class SimilarArtistsActivity extends AppCompatActivity {

    private Artist artist;
    private LayoutInflater inflater;
    private TextView header;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_similar_artists);

        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        header = (TextView) findViewById(R.id.header);
        list = (ListView) findViewById(R.id.list);

        header.setText(getResources().getString(R.string.similar_artists_header, artist.name));

        new Task().execute();
    }

    private class Task extends AsyncTask<String, Void, List<String>> {

        @Override
        protected void onPostExecute(List<String> similarArtists) {
            list.setAdapter(new Adapter(similarArtists));
        }

        @Override
        protected List<String> doInBackground(String... params) {
            MusicMapService musicMap = new MusicMapService();
            try {
                return musicMap.getSimilarArtists(artist.name);
            } catch (IOException e) {
                return null;
            } catch (MusicMapServiceError e) {
                return new ArrayList<>();
            }
        }
    }

    public class Adapter extends BaseAdapter implements View.OnClickListener {

        private List<String> similarArtists;

        public Adapter(List<String> similarArtists) {
            this.similarArtists = similarArtists;
        }

        @Override
        public int getCount() {
            return similarArtists.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                v = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }

            Stylist.interweaveListViewBgColor(position, v);

            TextView tv = (TextView) v.findViewById(android.R.id.text1);
            tv.setText(similarArtists.get(position));
            
            return v;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("artist", artist);
            startActivity(intent);
        }
    }

}