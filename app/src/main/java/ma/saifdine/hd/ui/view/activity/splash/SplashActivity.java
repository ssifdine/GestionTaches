package ma.saifdine.hd.ui.view.activity.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import ma.saifdine.hd.R;
import ma.saifdine.hd.ui.view.activity.task.TaskActivity;
import ma.saifdine.hd.ui.view.activity.user.LoginActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

//        // Ajoute cette ligne après setContentView()
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        findViewById(R.id.splashLogo).startAnimation(fadeIn);
        findViewById(R.id.splashText).startAnimation(fadeIn);

        // Délai avant de passer à MainActivity (3 secondes)
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Fermer le Splash Screen
        }, 5000); // 5000 ms = 5 secondes
    }
}