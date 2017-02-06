package com.phoenix2k.priorityreminder;
/**
 * Created by Pushpan on 08/01/17.
 */

public interface CommunicationListener {
     void onDisplayError(String error, boolean force);
     void showProgress(boolean show);

}
