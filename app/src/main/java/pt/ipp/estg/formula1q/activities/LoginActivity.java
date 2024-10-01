package pt.ipp.estg.formula1q.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.database.Firebase;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import java.time.LocalDate;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 200;
    private static GoogleSignInClient mGoogleSignInClient;
    private Button signInButton;
    private FirebaseAuth mAuth;
    private GoogleSignInAccount gAccount;
    private static OnCompleteListener signOutCompleteListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this::signIn);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestIdToken(getString(R.string.clientId))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        boolean shouldSignOut = getIntent().getBooleanExtra("shouldSignOut", false);
        if(shouldSignOut){
            signOut();
        }

        signOutCompleteListener = new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private void launchMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            gAccount = task.getResult(ApiException.class);
            firebaseAuthWithGoogle(gAccount.getIdToken());
        } catch (ApiException e) {
            Toast.makeText(this, "Login failed", Toast.LENGTH_LONG).show();
            Log.w("LoginActivity", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LoginActivity", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Firebase.initializeUserIfNotExists();
                            Toast.makeText(LoginActivity.this, "Welcome " + gAccount.getGivenName(), Toast.LENGTH_LONG).show();
                            launchMainActivity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed(2)", Toast.LENGTH_LONG).show();
                            Log.w("LoginActivity", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    private static void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(signOutCompleteListener);
    }
}