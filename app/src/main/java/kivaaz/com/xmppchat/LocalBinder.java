package kivaaz.com.xmppchat;

import android.os.Binder;

import java.lang.ref.WeakReference;

/**
 * Created by Muguntan on 10/3/2017.
 */

public class LocalBinder<S> extends Binder {
    private final WeakReference<S> mService;

    public LocalBinder(final S service) {
        this.mService = new WeakReference<S>(service);
    }

    public S getService(){
        return mService.get();
    }
}