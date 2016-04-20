package org.ift2905.musicbrainz;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.Recording;
import org.ift2905.musicbrainz.service.musicbrainz.Release;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReleaseActivity extends AppCompatActivity {

    private Artist artist;
    private ReleaseGroup releaseGroup;

    private LayoutInflater inflater;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);

        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");
        releaseGroup = (ReleaseGroup) intent.getSerializableExtra("releaseGroup");

        getSupportActionBar().setTitle(releaseGroup.name);

        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        listView = (ListView) findViewById(R.id.list);

        new Task().execute();
    }

    public class Task extends AsyncTask<Void, Void, List<ListItem>> {

        @Override
        protected List<ListItem> doInBackground(Void... params) {
            List<Release> releases;

            try {
                releases = MusicBrainzService.getInstance().getReleases(releaseGroup);
            } catch (IOException e) {
                return null;
            }

            return flattenExtraReleases(diffReleases(releases));
        }

        @Override
        protected void onPostExecute(List<ListItem> items) {
            if (items != null) {
                listView.setAdapter(new Adapter(items));
            } else {
                Util.ioErrorDialog(ReleaseActivity.this, true).show();
            }
        }

    }

    // TODO: complexity is like O(n^2)
    private List<Release> diffReleases(List<Release> releases) {
        List<Release> extraReleases = new ArrayList<>();
        List<Recording> marked = new ArrayList<>();

        for (Release rel : releases) {
            List<Recording> extraRecordings = new ArrayList<>();
            for (Recording rec : rel.recordings) {
                if (!marked.contains(rec)) {
                    extraRecordings.add(rec);
                    marked.add(rec);
                }
            }

            if (extraRecordings.size() > 0) {
                Release newRel = rel.cloneWithoutRecordings();
                newRel.recordings = extraRecordings;
                extraReleases.add(newRel);
            }
        }

        return extraReleases;
    }

    private List<ListItem> flattenExtraReleases(List<Release> extraReleases) {
        List<ListItem> list = new ArrayList<>();
        for (Release rel : extraReleases) {
            list.add(new ListItemRelease(rel));
            int position = 0;
            for (Recording rec : rel.recordings) {
                list.add(new ListItemRecording(position++, rec));
            }
        }
        return list;
    }

    private interface ListItem {

        View getView(ViewGroup parent);

    }

    private class ListItemRelease implements ListItem {

        private Release release;

        public ListItemRelease(Release release) {
            this.release = release;
        }

        @Override
        public View getView(ViewGroup parent) {
            View v = inflater.inflate(R.layout.list_release_release, parent, false);

            TextView name = (TextView) v.findViewById(R.id.name);
            TextView date = (TextView) v.findViewById(R.id.date);
            TextView areas = (TextView) v.findViewById(R.id.areas);

            name.setText(release.name);
            date.setText(release.year);
            areas.setText(TextUtils.join(", ", release.areas).replaceAll("(\\[|\\])", ""));

            return v;
        }
    }

    private class ListItemRecording implements ListItem {

        private int position;
        private Recording recording;

        public ListItemRecording(int position, Recording recording) {
            this.position = position;
            this.recording = recording;
        }

        @Override
        public View getView(ViewGroup parent) {
            View v = inflater.inflate(R.layout.list_release_recording, parent, false);

            TextView number = (TextView) v.findViewById(R.id.number);
            TextView title = (TextView) v.findViewById(R.id.title);
            TextView duration = (TextView) v.findViewById(R.id.duration);

            Util.interweaveListViewBgColor(position, v);

            number.setText(String.format("%02d", position + 1));
            title.setText(recording.name);
            if (recording.length != null && !recording.length.isEmpty()) {
                duration.setText(Util.secondsToText(Integer.parseInt(recording.length)));
            } else {
                duration.setText("");
            }

            return v;
        }
    }

    private class Adapter extends BaseAdapter {

        private List<ListItem> items;

        public Adapter(List<ListItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
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
            return items.get(position).getView(parent);
        }

    }
}

