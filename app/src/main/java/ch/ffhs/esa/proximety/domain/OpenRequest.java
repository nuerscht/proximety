package ch.ffhs.esa.proximety.domain;

import android.graphics.drawable.Drawable;

import java.util.Date;

/**
 * Created by sandro on 21.12.14.
 */
public class OpenRequest {
    private String name;
    private String email;
    private Drawable image;
    private Date received;

    public OpenRequest(String name, String email, Drawable image, Date received){

        this.name = name;
        this.email = email;
        this.image = image;
        this.received = received;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }
}
