package org.ift2905.musicbrainz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ift2905.musicbrainz.service.musicbrainz.*;

public class ReleaseGroupFragment extends Fragment implements View.OnClickListener {

    private Artist artist;
    private ReleaseGroup releaseGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_release_group, container, false);

        artist = (Artist) getArguments().getSerializable("artist");
        releaseGroup = (ReleaseGroup) getArguments().getSerializable("releaseGroup");

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView year = (TextView) v.findViewById(R.id.year);
        TextView primaryType = (TextView) v.findViewById(R.id.primaryType);
        TextView secondaryTypes = (TextView) v.findViewById(R.id.secondaryTypes);

        name.setText(releaseGroup.name);
        year.setText(releaseGroup.year);
        // TODO: i18n types
        primaryType.setText(releaseGroup.primaryType);
        secondaryTypes.setText(TextUtils.join(" + ", releaseGroup.secondaryTypes));

        ImageView iv = (ImageView) v.findViewById(R.id.imageView);
        iv.setOnClickListener(this);
        Picasso.with(getContext())
                .load(String.format("http://coverartarchive.org/release-group/%s/front", releaseGroup.id))
                .fit()
                .centerCrop()
                .error(R.drawable.empty_album)
                .placeholder(R.drawable.empty_album)
                .into(iv);
        return v;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), ReleaseActivity.class);
        intent.putExtra("artist", artist);
        intent.putExtra("releaseGroup", releaseGroup);
        startActivity(intent);
    }
}
