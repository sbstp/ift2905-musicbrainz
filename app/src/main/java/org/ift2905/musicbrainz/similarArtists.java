package org.ift2905.musicbrainz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicmap.MusicMapService;
import org.ift2905.musicbrainz.service.musicmap.MusicMapServiceError;

import static org.ift2905.musicbrainz.R.layout.activity_similar_artists;

public class similarArtists extends AppCompatActivity{

    private ListView list;
    private MyAdapter adapter;
    private LayoutInflater inflater;
    private Artist artist;
    private List<String> similarArtistList;
    private ImageView imageArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_similar_artists);
        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");
    }

    public class RunSimArtist extends AsyncTask<String, Void, List<String>> {

       @Override
       protected void onPostExecute(List<String> strings) {
           super.onPostExecute(strings);
           adapter = new MyAdapter((List<Artist>) artist, inflater);//manque nom artiste(s)? en paramètre
           list.setAdapter(adapter);
       }

       @Override
       protected List<String> doInBackground(String... params) {
           String artistToSearch = artist.toString();
           MusicMapService musicMap = new MusicMapService();

           try {
               return similarArtistList = musicMap.getSimilarArtists(artistToSearch);
           } catch (IOException e) {
               return null;
           } catch (MusicMapServiceError musicMapServiceError) {
               return null;
           }
       }
   }

    public class MyAdapter extends BaseAdapter implements View.OnClickListener {
        private LayoutInflater inflater;
        private List<Artist> artistsA;

        public MyAdapter(List<Artist> artistsA, LayoutInflater inflater){
            inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            this.artistsA = artistsA;
        }

        @Override
        public int getCount() {
            return artistsA.size();
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
            if (v==null) {
                v = inflater.inflate(android.R.layout.simple_expandable_list_item_1, parent, false);
            }
            TextView tv = (TextView)v.findViewById(android.R.id.text1);
            tv.setOnClickListener(this);

            ImageView im = (ImageView)v.findViewById(R.id.imageArtist);
            Picasso.with(getApplicationContext()).
                    load(artist.image)
                    .placeholder(R.drawable.release_group_placeholder)
                    .into(im);
            String similaire = similarArtistList.get(position);
            tv.setText(similaire);
            return  v;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("artist", artist);
            startActivity(intent);
        }
    }

}
