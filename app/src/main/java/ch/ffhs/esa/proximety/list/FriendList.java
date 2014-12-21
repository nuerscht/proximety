package ch.ffhs.esa.proximety.list;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ch.ffhs.esa.proximety.R;

/**
 * Created by boe on 21.12.2014.
 */
public class FriendList extends ArrayAdapter<String> {
    private final LayoutInflater inflater;
    private final String[] friends;
    private final String[] places;
    public FriendList(LayoutInflater inflater, String[] friends, String[] places) {
        super(inflater.getContext(), R.layout.list_friend, friends);
        this.inflater = inflater;
        this.friends = friends;
        this.places = places;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView= inflater.inflate(R.layout.list_friend, null, true);
        TextView friendNameView = (TextView) rowView.findViewById(R.id.friend_name);
        TextView friendPlaceView = (TextView) rowView.findViewById(R.id.friend_place);
        friendNameView.setText(friends[position]);
        friendPlaceView.setText(places[position]);
        return rowView;
    }
}
