package ch.ffhs.esa.proximety.helper;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by boe on 21.12.2014.
 */
public class Gravatar {
    private String email;

    private Bitmap image;

    private int position;

    public Gravatar(String email, int position) {
        this.email = email;
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getPosition() {
        return position;
    }
}
