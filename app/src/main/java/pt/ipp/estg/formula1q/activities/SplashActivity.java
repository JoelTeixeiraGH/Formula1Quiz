package pt.ipp.estg.formula1q.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import pt.ipp.estg.formula1q.R;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_TIME_OUT = 1000;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        getSupportActionBar().hide();
        Intent intent;

        if(account == null){
            intent  = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
