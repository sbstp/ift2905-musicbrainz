package org.ift2905.musicbrainz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ift2905.musicbrainz.service.musicbrainz.*;

public class ReleaseGroupFragment extends Fragment implements View.OnClickListener {

    private ReleaseGroup releaseGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_release_group, container, false);
        releaseGroup = (ReleaseGroup) getArguments().getSerializable("releaseGroup");
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(formatTitle(releaseGroup));
        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        iv.setOnClickListener(this);
        Picasso p = Picasso.with(getContext());
        p.setIndicatorsEnabled(true);
        p.load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id))
                .placeholder(R.drawable.release_group_placeholder)
                .into(iv);
        return v;
    }

    private String formatTitle(ReleaseGroup group) {
        if (group.year != null) {
            return String.format("%s (%s)", group.name, group.year);
        } else {
            return group.name;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), Release.class);
        intent.putExtra("releaseGroup", releaseGroup);
        startActivity(intent);
    }
}
