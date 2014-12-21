package ch.ffhs.esa.proximety.activities.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.domain.OpenRequest;

/**
 * Created by sandro on 21.12.14.
 */
public class OpenRequestAdapter extends ArrayAdapter<OpenRequest> {
    private final Context context;
    private final int resource;

    public OpenRequestAdapter(Context context, int resource, OpenRequest[] objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        OpenRequestHolder holder = null;

        if(row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource,parent,false);

            holder = new OpenRequestHolder();
            holder.persImage = (ImageView) row.findViewById(R.id.open_request_personImg);
            holder.title = (TextView) row.findViewById(R.id.open_request_title);

            row.setTag(holder);


        } else {
            holder = (OpenRequestHolder) row.getTag();
        }

        holder.persImage.setImageDrawable(this.getItem(position).getImage());
        holder.title.setText(this.getItem(position).getName());

        return row;

    }

    public static class OpenRequestHolder {
        ImageView persImage;
        TextView title;
    }
}
