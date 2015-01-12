package ch.ffhs.esa.proximety.list;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ch.ffhs.esa.proximety.R;

/**
 * @author Patrick Bösch.
 */
public class FriendList extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final String[] friends;
    private final String[] places;
    private final String[] distance;
    private final Bitmap[] images;

    public FriendList(LayoutInflater inflater, String[] friends, String[] places, String[] distance, Bitmap[] images) {
        super(inflater.getContext(), R.layout.list_friend, friends);
        this.inflater = inflater;
        this.friends = friends;
        this.places = places;
        this.images = images;
        this.distance = distance;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView= inflater.inflate(R.layout.list_friend, null, true);
        TextView friendNameView = (TextView) rowView.findViewById(R.id.friend_name);
        TextView friendPlaceView = (TextView) rowView.findViewById(R.id.friend_place);
        ImageView friendImageView = (ImageView) rowView.findViewById(R.id.friend_image);
        TextView friendDistanceView = (TextView) rowView.findViewById(R.id.friend_distance);
        friendNameView.setText(friends[position]);
        friendPlaceView.setText(places[position]);
        friendImageView.setImageBitmap(images[position]);
        friendDistanceView.setText(distance[position]);
        return rowView;
    }

    public void refresh() {
        notifyDataSetChanged();
    }
}
