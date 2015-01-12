package ch.ffhs.esa.proximety.helper;

import android.graphics.Bitmap;

/**
 * Created by Patrick Bösch.
 */
public class Gravatar {
    private final String email;

    private Bitmap image;

    private final int position;

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
