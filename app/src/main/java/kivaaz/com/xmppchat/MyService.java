package kivaaz.com.xmppchat;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

/**
 * Created by Muguntan on 9/27/2017.
 */

public class MyService extends Service {

    private static final String DOMAIN = "jabber.network";
    private static final String USERNAME = "Wakerz";
    private static final String PASSWORD = "123456";
    public static ConnectivityManager cm;
    public static MyXMPP xmpp;
    public static boolean ServerChatCreated = false;
    String text = "";
    public Chat chat;

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder<MyService>(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        xmpp = MyXMPP.getInstance(MyService.this,DOMAIN,USERNAME,PASSWORD);
        xmpp.connect("onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        xmpp.disconnect();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public static boolean isNetworkConnected(){
        return cm.getActiveNetworkInfo() != null;
    }



}