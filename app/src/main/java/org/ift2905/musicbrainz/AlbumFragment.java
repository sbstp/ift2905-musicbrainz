package org.ift2905.musicbrainz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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

import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

import java.io.IOException;
import java.util.List;

public class AlbumFragment extends Fragment implements TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    private LayoutInflater inflater;
    private EditText searchBox;
    private ListView list;
    private List<ReleaseGroup> currentReleaseGroups;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_album, container, false);

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
                event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

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
        intent.putExtra("artist", currentReleaseGroups.get(position).credits.get(0));
        intent.putExtra("releaseGroup", currentReleaseGroups.get(position));
        startActivity(intent);
    }

    private class Task extends AsyncTask<String, Void, List<ReleaseGroup>> {

        @Override
        protected List<ReleaseGroup> doInBackground(String... params) {
            MusicBrainzService serv = MusicBrainzService.getInstance();
            try {
                return serv.searchReleaseGroup(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ReleaseGroup> releaseGroups) {
            currentReleaseGroups = releaseGroups;
            list.setAdapter(new Adapter(releaseGroups, inflater));
        }
    }

    private static class Adapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<ReleaseGroup> albums;

        public Adapter(List<ReleaseGroup> albums, LayoutInflater inflater) {
            this.albums = albums;
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return albums.size();
        }

        @Override
        public Object getItem(int position) {
            return albums.get(position);
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

            tv1.setText(albums.get(position).name);
            tv2.setText(TextUtils.join(", ", albums.get(position).credits));

            return v;
        }
    }
}

