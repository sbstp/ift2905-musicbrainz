package org.ift2905.musicbrainz;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private AlbumFragment albumFragment;
    private ArtistFragment artistFragment;
    private BookmarksFragment bookmarksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumFragment = new AlbumFragment();
        artistFragment = new ArtistFragment();
        bookmarksFragment = new BookmarksFragment();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookmarksFragment.refresh();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private int[] titleResourceIds;
        private Fragment[] fragments;

        public PagerAdapter(FragmentManager fm) {
            super(fm);

            titleResourceIds = new int[] {
                    R.string.main_tab_album_title,
                    R.string.main_tab_artist_title,
                    R.string.main_tab_bookmarks_title,
            };
            fragments = new Fragment[]{
                    albumFragment,
                    artistFragment,
                    bookmarksFragment,
            };
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

}
