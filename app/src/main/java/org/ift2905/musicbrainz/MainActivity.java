package org.ift2905.musicbrainz;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import org.ift2905.musicbrainz.fixjava.OnPageChangeAdapter;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private AlbumFragment albumFragment;
    private ArtistFragment artistFragment;
    private BookmarksFragment bookmarksFragment;
    private Fragment[] fragments;

    private int[] TITLE_RESOURCE_IDS = new int[] {
            R.string.main_tab_album_title,
            R.string.main_tab_artist_title,
            R.string.main_tab_bookmarks_title,
    };

    private int[] ACTIONBAR_TITLE_RESOURCE_IDS = new int[] {
            R.string.main_tab_album_hint,
            R.string.main_tab_artist_hint,
            R.string.main_tab_bookmarks_title,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumFragment = new AlbumFragment();

        artistFragment = new ArtistFragment();
        String search = getIntent().getStringExtra("search");
        Bundle args = new Bundle();
        args.putString("search", search);
        artistFragment.setArguments(args);

        bookmarksFragment = new BookmarksFragment();

        fragments = new Fragment[]{
                albumFragment,
                artistFragment,
                bookmarksFragment,
        };

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new OnPageChangeAdapter() {
            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setTitle(getResources().getString(ACTIONBAR_TITLE_RESOURCE_IDS[position]));
                if (fragments[position] == bookmarksFragment) {
                    bookmarksFragment.hideKeyboard();
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bookmarksFragment.refresh();
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return TITLE_RESOURCE_IDS.length;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(TITLE_RESOURCE_IDS[position]);
        }

    }

}
