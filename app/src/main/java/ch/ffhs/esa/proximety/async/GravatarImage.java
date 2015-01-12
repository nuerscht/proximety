package ch.ffhs.esa.proximety.async;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ch.ffhs.esa.proximety.helper.Gravatar;

/**
 * @author  Sandro Dallo.
 */
public abstract class GravatarImage extends AsyncTask<Gravatar, Void, Gravatar> {

    protected Gravatar doInBackground(Gravatar... gravatars) {
        Gravatar gravatar = gravatars[0];
        Bitmap bitmap = null;
        try {
            String GRAVATAR_BASE_URL = "http://www.gravatar.com/avatar/";
            if (gravatar.getEmail() != null && !gravatar.getEmail().isEmpty())
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(GRAVATAR_BASE_URL.concat(md5Hex(gravatar.getEmail()))).getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }

        gravatar.setImage(bitmap);

        return gravatar;
    }

    protected abstract void onPostExecute(Gravatar gravatar);

    private static String hex(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte anArray : array) {
            sb.append(Integer.toHexString((anArray
                    & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }

    private static String md5Hex(String message) {
        try {
            MessageDigest md =
                    MessageDigest.getInstance("MD5");
            return hex (md.digest(message.getBytes("CP1252")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Log.i("GravatarImage", e.getMessage());
        }
        return null;
    }
}
