package org.ift2905.musicbrainz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private int[] titleResourceIds;
        private Fragment[] fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);

            titleResourceIds = new int[] {
                    R.string.main_tab_album_title,
                    R.string.main_tab_artist_title,
                    R.string.main_tab_history_title};
            fragments = new Fragment[]{
                    new AlbumFragment(),
                    new ArtistFragment(),
                    new HistoryFragment()};
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return titleResourceIds.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(titleResourceIds[position]);
        }

    }

    public static class AlbumFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.main_tab_album, container, false);
            EditText ed = (EditText) v.findViewById(R.id.searchBox);
            return v;
        }
    }

    public static class ArtistFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.main_tab_artist, container, false);
            EditText ed = (EditText) v.findViewById(R.id.searchBox);
            return v;

        }
    }

    public static class HistoryFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

}
