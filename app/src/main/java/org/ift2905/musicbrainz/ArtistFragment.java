package org.ift2905.musicbrainz;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzServiceTimeout;

import java.io.IOException;
import java.util.List;

public class ArtistFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_tab_artist, container, false);
        final EditText searchBox = (EditText) v.findViewById(R.id.searchBox);
        final ListView list = (ListView) v.findViewById(R.id.list);

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    // hide keyboard
                    InputMethodManager inputManager =
                            (InputMethodManager) getContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            searchBox.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);

                    // launch search
                    new AsyncTask<String, Void, List<Artist>>() {
                        @Override
                        protected List<Artist> doInBackground(String... params) {
                            MusicBrainzService serv = new MusicBrainzService();
                            try {
                                return serv.searchArtist(params[0]);
                            } catch (IOException e) {
                                e.printStackTrace();
                                // TODO
                            } catch (MusicBrainzServiceTimeout e) {
                                // TODO
                                e.printStackTrace();
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(List<Artist> artists) {
                            list.setAdapter(new Adapter(artists, inflater));
                        }
                    }.execute(searchBox.getText().toString());


                    return true;
                }
                return false;
            }
        });

        return v;
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