package ch.ffhs.esa.proximety.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ch.ffhs.esa.proximety.helper.Gravatar;

/**
 * Created by boe on 21.12.2014.
 */
public abstract class GravatarImage extends AsyncTask<Gravatar, Void, Gravatar> {
    private String GRAVATAR_BASE_URL = "http://www.gravatar.com/avatar/";

    protected Gravatar doInBackground(Gravatar... gravatars) {
        Gravatar gravatar = gravatars[0];
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(GRAVATAR_BASE_URL.concat(md5Hex(gravatar.getEmail()))).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        gravatar.setImage(bitmap);

        return gravatar;
    }

    protected abstract void onPostExecute(Gravatar gravatar);

    public static String hex(byte[] array) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i) {
            sb.append(Integer.toHexString((array[i]
                    & 0xFF) | 0x100).substring(1,3));
        }
        return sb.toString();
    }

    public static String md5Hex (String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException e) {
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
