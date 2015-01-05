package ch.ffhs.esa.proximety.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.async.GravatarImage;
import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.helper.Gravatar;
import ch.ffhs.esa.proximety.list.OpenRequestList;
import ch.ffhs.esa.proximety.service.binder.friend.FriendServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;

public class OpenRequestsActivity extends ActionBarActivity {

    private final String LOG_TAG = "OpenRequetActivity";

    String[] ids = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_requests);

        loadList();
    }

    private void loadList() {
        FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext());
        fsb.queryOpenRequests(new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                if (statusCode == 200) {
                    onListLoadSuccess((JSONArray) response);
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onButtonRequestAccept(final View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = ids[(int)button.getTag()];

                FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext());
                fsb.confirmRequest(id, new ResponseHandler(getApplicationContext()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Object response) {
                        if (statusCode == 200) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setMessage(getText(R.string.friend_accept_info)).setTitle(getText(R.string.friend_accept));
        AlertDialog dialog = builder.create();
        dialog.show();

        String id = ids[(int)button.getTag()];
    }

    public void onButtonRequestCancel(final View button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton(R.string.refuse, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = ids[(int)button.getTag()];

                FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext());
                fsb.declineRequest(id, new ResponseHandler(getApplicationContext()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Object response) {
                        if (statusCode == 200) {
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        builder.setMessage(getText(R.string.friend_refuse_info)).setTitle(getText(R.string.friend_refuse));
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void onListLoadSuccess(JSONArray openRequests) {
        LayoutInflater inflater = getLayoutInflater();

        String[] friends = new String[openRequests.length()];
        final Bitmap[] images = new Bitmap[openRequests.length()];
        ids = new String[openRequests.length()];
        final OpenRequestList openRequestList = new OpenRequestList(inflater, friends, images);

        Gson gson = new Gson();
        for(int i = 0; i < openRequests.length(); i++) {
            try {
                Friend friend = gson.fromJson(openRequests.getJSONObject(i).getJSONObject("requester").toString(), Friend.class);
                ids[i] = openRequests.getJSONObject(i).getString("_id");
                friends[i] = friend.name;
                images[i] = BitmapFactory.decodeResource(getResources(), R.drawable.placeholder_friend);

                Gravatar gravatar = new Gravatar(friend.email, i);

                new GravatarImage() {
                    @Override
                    protected void onPostExecute(Gravatar gravatar) {
                        if (gravatar.getImage() != null) {
                            images[gravatar.getPosition()] = gravatar.getImage();
                            //Handle refresh data
                            openRequestList.refresh();
                        }
                    }
                }.execute(gravatar);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        openRequestList.refresh();
        //List view
        final ListView listView = (ListView)findViewById(R.id.listview);
        listView.setAdapter(openRequestList);
    }

}
