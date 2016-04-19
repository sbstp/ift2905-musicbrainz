package org.ift2905.musicbrainz;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.ift2905.musicbrainz.fixjava.OnTabSelectedAdapter;
import org.ift2905.musicbrainz.service.bookmarks.BookmarksService;
import org.ift2905.musicbrainz.service.musicbrainz.Artist;
import org.ift2905.musicbrainz.service.musicbrainz.MusicBrainzService;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;
import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroupType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscographyActivity extends AppCompatActivity {

    private Artist artist;
    private ReleaseGroup releaseGroup;
    private List<ReleaseGroup> currentReleaseGroups;
    private String primaryFilter;
    private String secondaryFilter;

    private TabLayout tabLayout1;
    private TabLayout tabLayout2;
    private ViewPager pager;

    private static int[] PRIMARY_FILTER_TITLES = new int[] {
            R.string.discography_filter_all,
            R.string.discography_filter_album,
            R.string.discography_filter_ep,
            R.string.discography_filter_single,
    };

    private static String[] PRIMARY_FILTER_VALUES = new String[] {
            null,
            ReleaseGroupType.ALBUM,
            ReleaseGroupType.EP,
            ReleaseGroupType.SINGLE,
    };

    private static int[] SECONDARY_FILTER_TITLES = new int[] {
            R.string.discography_filter_all,
            R.string.discography_filter_studio,
            R.string.discography_filter_compilation,
            R.string.discography_filter_live,
            R.string.discography_filter_remix,
            R.string.discography_filter_soundtrack,
    };

    private static String[] SECONDARY_FILTER_VALUES = new String[] {
            null,
            ReleaseGroupType.STUDIO,
            ReleaseGroupType.COMPILATION,
            ReleaseGroupType.LIVE,
            ReleaseGroupType.REMIX,
            ReleaseGroupType.SOUNDTRACK,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discography);

        Intent intent = getIntent();
        artist = (Artist) intent.getSerializableExtra("artist");
        releaseGroup = (ReleaseGroup) intent.getSerializableExtra("releaseGroup");

        getSupportActionBar().setTitle(artist.name);

        tabLayout1 = (TabLayout) findViewById(R.id.tabLayout1);
        buildTabLayout(tabLayout1, PRIMARY_FILTER_TITLES, PRIMARY_FILTER_VALUES);
        tabLayout1.setOnTabSelectedListener(new OnTabSelectedAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                primaryFilter = (String) tab.getTag();
                updateFilter();
            }
        });

        tabLayout2 = (TabLayout) findViewById(R.id.tabLayout2);
        buildTabLayout(tabLayout2, SECONDARY_FILTER_TITLES, SECONDARY_FILTER_VALUES);
        tabLayout2.setOnTabSelectedListener(new OnTabSelectedAdapter() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                secondaryFilter = (String) tab.getTag();
                updateFilter();
            }
        });


        pager = (ViewPager) findViewById(R.id.viewPager);
        new Task().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.discography_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem addBookmark = menu.findItem(R.id.add_bookmark);
        MenuItem rmBookmark = menu.findItem(R.id.rm_bookmark);

        boolean bookmarked = new BookmarksService(getApplicationContext()).isBookmarked(artist);
        addBookmark.setEnabled(!bookmarked);
        rmBookmark.setEnabled(bookmarked);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.similar:
                Intent intent = new Intent(getApplicationContext(), SimilarArtistsActivity.class);
                intent.putExtra("artist", artist);
                startActivity(intent);
                return true;
            case R.id.add_bookmark:
                new BookmarksService(getApplicationContext()).addBookmark(artist);
                invalidateOptionsMenu();
                return true;
            case R.id.rm_bookmark:
                new BookmarksService(getApplicationContext()).removeBookmark(artist);
                invalidateOptionsMenu();
                return true;
        }
        return false;
    }

    private void buildTabLayout(TabLayout tabLayout, int[] titles, String[] values) {
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout
                            .newTab()
                            .setText(getResources().getString(titles[i]))
                            .setTag(values[i])
            );
        }
    }

    private void updateFilter() {
        if (currentReleaseGroups == null) return;

        List<ReleaseGroup> filtered = new ArrayList<>();
        for (ReleaseGroup rg : currentReleaseGroups) {
            if (primaryFilter != null) {
                if (!rg.hasPrimaryType(primaryFilter)) {
                    continue;
                }
            }
            if (secondaryFilter != null) {
                if (secondaryFilter.equals(ReleaseGroupType.STUDIO)) {
                    if (rg.secondaryTypes.size() > 0) {
                        continue;
                    }
                } else if (!rg.hasSecondaryType(secondaryFilter)) {
                    continue;
                }
            }
            filtered.add(rg);
        }

        FragmentManager fm = getSupportFragmentManager();
        if (fm.getFragments() != null) {
            fm.getFragments().clear();
        }
        pager.setAdapter(new Adapter(fm, filtered));

        // If the activty was launched by an album search,
        // we must select the release group from the list.
        // Once this is done, we consume the release group
        // so that it doesn't select the release group on
        // the next filter.
        if (releaseGroup != null) {
            int position = filtered.indexOf(releaseGroup);
            if (position != -1) {
                pager.setCurrentItem(position);
            }
            releaseGroup = null;
        }
    }

    private class Task extends AsyncTask<Void, Void, List<ReleaseGroup>> {

        @Override
        protected List<ReleaseGroup> doInBackground(Void... params) {
            try {
                return MusicBrainzService.getInstance().getReleaseGroups(artist);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<ReleaseGroup> releaseGroups) {
            currentReleaseGroups = releaseGroups;
            updateFilter();
        }
    }

    private class Adapter extends FragmentPagerAdapter {

        private List<ReleaseGroup> albums;

        public Adapter(FragmentManager fm, List<ReleaseGroup> albums) {
            super(fm);
            this.albums = albums;
        }

        @Override
        public int getCount() {
            return albums.size();
        }

        @Override
        public Fragment getItem(int position) {
            ReleaseGroupFragment f = new ReleaseGroupFragment();
            Bundle args = new Bundle();
            args.putSerializable("artist", artist);
            args.putSerializable("releaseGroup", albums.get(position));
            f.setArguments(args);
            return f;
        }
    }
}
