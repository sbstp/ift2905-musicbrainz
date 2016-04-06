package org.ift2905.musicbrainz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

import java.io.IOException;
import java.util.List;

public class ArtistFragment extends Fragment implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    private LayoutInflater inflater;
    private EditText searchBox;
    private ListView list;
    private List<Artist> currentArtists;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_artist, container, false);

        this.inflater = inflater;
        this.searchBox = (EditText) v.findViewById(R.id.searchBox);
        this.searchBox.setOnEditorActionListener(this);
        this.list = (ListView) v.findViewById(R.id.list);
        this.list.setOnItemClickListener(this);

        return v;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            // hide keyboard
            InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(searchBox.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            // launch api task
            new Task().execute(searchBox.getText().toString());

            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this.getContext(), DiscographyActivity.class);
        intent.putExtra("artist", currentArtists.get(position));
        intent.putExtra("releaseGroup", (ReleaseGroup) null);
        startActivity(intent);
    }

    private class Task extends AsyncTask<String, Void, List<Artist>> {

        @Override
        protected List<Artist> doInBackground(String... params) {
            MusicBrainzService serv = MusicBrainzService.getInstance();
            try {
                return serv.searchArtist(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            if (artists != null) {
                currentArtists = artists;
                list.setAdapter(new Adapter(artists, inflater));
            } else {
                // TODO show error
            }
        }
    }

    private static class Adapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<Artist> artists;

        public Adapter(List<Artist> artists, LayoutInflater inflater) {
            this.artists = artists;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return artists.size();
        }

        @Override
        public Object getItem(int position) {
            return artists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);

            TextView tv1 = (TextView) v.findViewById(android.R.id.text1);
            TextView tv2 = (TextView) v.findViewById(android.R.id.text2);
            tv1.setText(artists.get(position).name);
            if (artists.get(position).disambiguation != null) {
                tv2.setText(artists.get(position).disambiguation);
            }

            return v;
        }
    }
}