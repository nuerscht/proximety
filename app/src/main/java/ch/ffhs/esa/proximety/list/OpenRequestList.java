package ch.ffhs.esa.proximety.list;

import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import ch.ffhs.esa.proximety.R;

/**
 * Created by Patrick Bösch.
 */
public class OpenRequestList extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final String[] friends;
    private final Bitmap[] images;

    public OpenRequestList(LayoutInflater inflater, String[] friends, Bitmap[] images) {
        super(inflater.getContext(), R.layout.list_friend, friends);
        this.inflater = inflater;
        this.friends = friends;
        this.images = images;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView= inflater.inflate(R.layout.list_openrequest, null, true);
        TextView friendNameView = (TextView) rowView.findViewById(R.id.openrequest_name);
        ImageView friendImageView = (ImageView) rowView.findViewById(R.id.openrequest_image);
        ImageButton acceptButton = (ImageButton)rowView.findViewById(R.id.openrequest_accept);
        ImageButton cancelButton = (ImageButton)rowView.findViewById(R.id.openrequest_cancel);
        acceptButton.setTag(position);
        cancelButton.setTag(position);
        friendNameView.setText(friends[position]);
        friendImageView.setImageBitmap(images[position]);
        return rowView;
    }

    public void refresh() {
        notifyDataSetChanged();
    }
}
