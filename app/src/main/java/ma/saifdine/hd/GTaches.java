package ma.saifdine.hd;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class GTaches extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}
