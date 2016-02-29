package org.ift2905.musicbrainz;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.Artist;
import org.ift2905.musicbrainz.service.MusicBrainzServiceTimeout;
import org.ift2905.musicbrainz.service.Release;
import org.ift2905.musicbrainz.service.ReleaseGroup;
import org.ift2905.musicbrainz.service.MusicBrainzService;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        TextView tv = (TextView) findViewById(R.id.editText);
        tv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_NULL && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    new ApiTask().execute(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private class ApiTask extends AsyncTask<String, Void, List<ReleaseGroup>> {

        @Override
        protected List<ReleaseGroup> doInBackground(String ... params) {
            MusicBrainzService serv = new MusicBrainzService();

            try {
                List<Artist> artists = serv.searchArtist(params[0]);
                List<ReleaseGroup> entries = serv.getReleaseGroups(artists.get(0).id);
                //List<Release> releases = serv.getReleases(entries.get(0).id);
                return entries;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MusicBrainzServiceTimeout e) {
                Log.i("MusicBrainzService", "Request timed out");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<ReleaseGroup> releaseGroups) {
            super.onPostExecute(releaseGroups);
            listView.setAdapter(new ReleaseGroupAdapter(releaseGroups));
        }
    }

    private class ReleaseGroupAdapter extends BaseAdapter {

        private List<ReleaseGroup> releaseGroups;

        public ReleaseGroupAdapter(List<ReleaseGroup> releaseGroups) {
            this.releaseGroups = releaseGroups;
            Collections.sort(this.releaseGroups, new Comparator<ReleaseGroup>() {
                @Override
                public int compare(ReleaseGroup lhs, ReleaseGroup rhs) {
                    if (lhs.releaseDate == null || rhs.releaseDate == null) {
                        return 0;
                    }
                    return lhs.releaseDate.compareTo(rhs.releaseDate);
                }
            });
        }

        @Override
        public int getCount() {
            return this.releaseGroups.size();
        }

        @Override
        public Object getItem(int position) {
            return this.releaseGroups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);
            }
            TextView tv1 = (TextView) convertView.findViewById(android.R.id.text1);
            TextView tv2 = (TextView) convertView.findViewById(android.R.id.text2);

            tv1.setText(this.releaseGroups.get(position).title);
            tv2.setText(this.releaseGroups.get(position).primaryType);

            return convertView;
        }
    }
}
