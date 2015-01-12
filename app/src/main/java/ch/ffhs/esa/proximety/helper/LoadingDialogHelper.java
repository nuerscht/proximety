package ch.ffhs.esa.proximety.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;

import ch.ffhs.esa.proximety.R;

/**
 * @author  Patrick Bösch.
 */
public class LoadingDialogHelper {

    public static Dialog createDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_loading, null));

        return builder.create();
    }
}
