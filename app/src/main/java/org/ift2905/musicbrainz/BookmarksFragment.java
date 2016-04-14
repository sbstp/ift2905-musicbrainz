package org.ift2905.musicbrainz;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.bookmarks.BookmarksService;
import org.ift2905.musicbrainz.service.musicbrainz.Artist;

import java.util.List;

public class BookmarksFragment extends Fragment {

    private BookmarksService bookmarksService;
    private LayoutInflater inflater;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        bookmarksService = new BookmarksService(getContext());
        this.inflater = inflater;
        listView = (ListView) v.findViewById(R.id.listView);
        refresh();

        return v;
    }

    public void refresh() {
        if (getContext() != null) {
            listView.setAdapter(new Adapter(bookmarksService.getBookmarks()));
        }
    }

    private class Adapter extends BaseAdapter {

        private List<Artist> artists;

        public Adapter(List<Artist> artists) {
            this.artists = artists;
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
            View v = inflater.inflate(R.layout.list_bookmarks_entry, parent, false);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            ImageButton ib = (ImageButton) v.findViewById(R.id.imageButton);

            final Artist artist = artists.get(position);
            tv.setText(artist.name);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DiscographyActivity.class);
                    intent.putExtra("artist", artist);
                    startActivity(intent);
                }
            });

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bookmarksService.removeBookmark(artist);
                    refresh();
                }
            });

            return v;
        }
    }
}