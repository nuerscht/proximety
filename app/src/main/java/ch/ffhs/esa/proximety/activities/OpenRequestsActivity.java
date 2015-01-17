package ch.ffhs.esa.proximety.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.identity.intents.AddressConstants;
import com.google.gson.Gson;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ch.ffhs.esa.proximety.R;
import ch.ffhs.esa.proximety.async.GravatarImage;
import ch.ffhs.esa.proximety.consts.ProximetyConsts;
import ch.ffhs.esa.proximety.domain.Friend;
import ch.ffhs.esa.proximety.helper.Gravatar;
import ch.ffhs.esa.proximety.list.OpenRequestList;
import ch.ffhs.esa.proximety.service.binder.friend.FriendServiceBinder;
import ch.ffhs.esa.proximety.service.handler.ResponseHandler;
/**
 * @author  Sandro Dallo.
 */
public class OpenRequestsActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout refreshLayout;
    private String[] ids = null;

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        loadList();
    }

    @Override
    public void onResume() {
        super.onResume();

        setRefreshing();
        loadList();
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_requests);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        refreshLayout.setSize(SwipeRefreshLayout.LARGE);
        setRefreshing();
    }

    private void loadList() {
        FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), null);
        fsb.queryOpenRequests(new ResponseHandler(getApplicationContext()) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, Object response) {
                if (statusCode == 200) {
                    onListLoadSuccess((JSONArray) response);
                } else {
                    Toast.makeText(getApplicationContext(), getErrorMessage(response), Toast.LENGTH_SHORT).show();
                }
            }

            public void onError(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onError(statusCode, headers, throwable, errorResponse);
                refreshLayout.setRefreshing(false);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                refreshLayout.setRefreshing(false);
            }

            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void setRefreshing() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
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
                final String id = ids[(int)button.getTag()];
                FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), null);
                fsb.confirmRequest(id, new ResponseHandler(getApplicationContext()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Object response) {
                        if (statusCode == 200) {
                            Intent intent = new Intent(getApplicationContext(), FriendDetailActivity.class);
                            try {
                                intent.putExtra(ProximetyConsts.FRIENDS_DETAIL_FRIEND_ID, ((JSONObject)response).getString("_id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);
                            finish();
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
                final String id = ids[(int)button.getTag()];
                FriendServiceBinder fsb = new FriendServiceBinder(getApplicationContext(), null);
                fsb.declineRequest(id, new ResponseHandler(getApplicationContext()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, Object response) {
                        if (statusCode == 200) {
                            Intent intent = getIntent();
                            startActivity(intent);
                            finish();
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
        refreshLayout.setRefreshing(false);
    }
}
