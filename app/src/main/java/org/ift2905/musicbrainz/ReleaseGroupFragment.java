package org.ift2905.musicbrainz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ift2905.musicbrainz.service.musicbrainz.ReleaseGroup;

public class ReleaseGroupFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_release_group, container, false);
        ReleaseGroup releaseGroup = (ReleaseGroup) getArguments().getSerializable("releaseGroup");
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(releaseGroup.title);
        return v;
    }
}
