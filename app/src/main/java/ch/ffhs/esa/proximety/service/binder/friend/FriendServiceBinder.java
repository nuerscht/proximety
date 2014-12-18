package ch.ffhs.esa.proximety.service.binder.friend;

import java.util.List;

import ch.ffhs.esa.proximety.service.binder.ServiceBinder;

/**
 * Created by boe on 15.12.2014.
 */
public class FriendServiceBinder extends ServiceBinder {

    public void sendRequest(int friendId, String token) {

    }

    public void confirmRequest(int requestId, String token) {

    }

    public void declineRequest(int requestId, String token) {

    }

    public void deleteFriend(int friendId, String token) {

    }

    public List<String> search(String searchText, String token) {
        return null;
    }

    public void queryOpenRequests(String token) {

    }

    private void invokeWebService() {

    }
}
