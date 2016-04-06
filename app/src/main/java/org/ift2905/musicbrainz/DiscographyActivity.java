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
import android.util.Log;

import org.ift2905.musicbrainz.fixjava.OnTabSelectedAdapter;
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
            R.string.discovery_filter_all,
            R.string.discovery_filter_album,
            R.string.discovery_filter_ep,
            R.string.discovery_filter_single,
    };

    private static String[] PRIMARY_FILTER_VALUES = new String[] {
            null,
            ReleaseGroupType.ALBUM,
            ReleaseGroupType.EP,
            ReleaseGroupType.SINGLE,
    };

    private static int[] SECONDARY_FILTER_TITLES = new int[] {
            R.string.discovery_filter_all,
            R.string.discovery_filter_studio,
            R.string.discovery_filter_compilation,
            R.string.discovery_filter_live,
            R.string.discovery_filter_remix,
            R.string.discovery_filter_soundtrack,
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
        Log.i("rg", String.format("cut %d to %d", currentReleaseGroups.size(), filtered.size()));
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getFragments() != null) {
            fm.getFragments().clear();
        }
        pager.setAdapter(new Adapter(fm, filtered));
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

    private static class Adapter extends FragmentPagerAdapter {

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
            args.putSerializable("releaseGroup", albums.get(position));
            f.setArguments(args);
            return f;
        }
    }
}
