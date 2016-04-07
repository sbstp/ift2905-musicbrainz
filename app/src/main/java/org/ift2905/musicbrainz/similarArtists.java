package org.ift2905.musicbrainz.service;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import org.ift2905.musicbrainz.R;
import org.ift2905.musicbrainz.service.musicmap.MusicMapService;
import org.ift2905.musicbrainz.service.musicmap.MusicMapServiceError;

import static org.ift2905.musicbrainz.R.id.sim_artist_name;

public class similarArtists extends AppCompatActivity {

    ListView list;
    Adapter myadapter;
    List<String> artists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_similar_artists);

        TextView tv =(TextView) findViewById((sim_artist_name));
        ImageView im = (ImageView)findViewById(R.id.sim_artist_image);

        Intent intent = getIntent();
        String artistessimilaires = intent.getStringExtra("ARTISTESSIMILAIRES");

        list = (ListView) findViewById(R.id.similarArtistsList);
    }

   public class RunSimArtist extends AsyncTask implements AdapterView.OnItemClickListener {

       @Override
       public void onPostExecute(List<MusicMapService>){
           super.onPostExecute();
           myadapter = new MyAdapter();
           list.setAdapter(myadapter);
           list.setOnItemClickListener(this);

       }

       @Override
       protected Object doInBackground(Object[] params) {
           return null;
       }

       @Override
       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           Intent intent = new Intent(this, cd_listing.class);
           intent.putExtra("NOMARTISTE", "Artistes");
           startActivity(intent);

       }
   }

    public class MyAdapter extends BaseAdapter{
        private LayoutInflater inflater;

        public MyAdapter(List<String> artists, LayoutInflater inflater){
            this.inflater = inflater;
            //this.artists = artists;
        }

        @Override
        public int getCount() {
            return artists.size();
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
            View v = inflater.inflate(R.layout.sim_artist_im, parent, false);


            tv.setText("Artistes similaires à: " );
            return v;
        }
    }

}
