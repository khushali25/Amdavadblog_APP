package Helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class Reachability {

    private final ConnectivityManager mConnectivityManager;


    public Reachability(Context context) {
        mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean isConnected() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }}