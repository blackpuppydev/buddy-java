package th.co.ais.genesis.module.buddy.wifi;

import android.app.Activity;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pManager;

public class ConnectWifi {

    Activity activity;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;


    public ConnectWifi(Activity activity){
        this.activity = activity;

    }

}
