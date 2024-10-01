package pt.ipp.estg.formula1q.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.api.APIClient;
import pt.ipp.estg.formula1q.database.question.QuestionRepository;
import pt.ipp.estg.formula1q.dialogs.BackgroundLocationRationaleDialog;
import pt.ipp.estg.formula1q.dialogs.ProfileDialog;
import pt.ipp.estg.formula1q.models.Race;
import pt.ipp.estg.formula1q.utils.Notification;
import pt.ipp.estg.formula1q.utils.RaceCalendar;
import pt.ipp.estg.formula1q.utils.LocationService;
import pt.ipp.estg.formula1q.utils.RaceService;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.time.LocalDate;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity {

    private static final int RC_FINE_LOCATION = 56;
    private static final int RC_CALENDAR_RW_ACTION_ADD = 57;
    private static final int RC_CALENDAR_RW_ACTION_REMOVE = 570;
    private static final int RC_CAMERA = 58;
    private boolean backPressedBefore = false;

    private FirebaseUser currentUser;
    private MainActivityViewModel viewModel;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        ImageButton btn_randomQuestion = findViewById(R.id.btn_random_question);
        TextView tv_username = findViewById(R.id.tv_username);
        TextView tv_score = findViewById(R.id.tv_score);
        ImageView iv_profileImg = findViewById(R.id.iv_profileImg);
        LinearLayout profileView = findViewById(R.id.ll_profile);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_leaderboard)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        tv_username.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]);

        viewModel = new MainActivityViewModel(getApplication());

        viewModel.getUserPicObservable()
                .observe(this, bitmap -> {
                    profileView.setOnClickListener(v -> {
                        new ProfileDialog(bitmap)
                                .show(getSupportFragmentManager(), null);
                    });
                    iv_profileImg.setImageBitmap(bitmap);
                });

        viewModel.getUserScoreObservable()
                .observe(this, score -> {
                    tv_score.setText(String.valueOf(score) +  " points");
                });

        btn_randomQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("eventName", "Random Questions");
                intent.putExtra("eventDate", String.valueOf(LocalDate.now().getYear()));
                startActivity(intent);
            }
        });


        findViewById(R.id.navigation_camera)
                .setOnClickListener(v->{
                    if(checkSelfPermission(CAMERA) == PERMISSION_GRANTED){
                        startActivity(new Intent(MainActivity.this, CameraActivity.class));
                    } else {
                        requestPermissions(new String[]{CAMERA}, RC_CAMERA);
                    }
                });


        findViewById(R.id.navigation_more).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MainActivity.this, findViewById(R.id.navigation_more));
            popup.getMenuInflater().inflate(R.menu.extras_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                switch(item.getItemId()){
                    case R.id.action_add_to_calendar:
                        if (checkSelfPermission(WRITE_CALENDAR) != PERMISSION_GRANTED){
                            requestPermissions(new String[]{WRITE_CALENDAR, READ_CALENDAR},
                                    RC_CALENDAR_RW_ACTION_ADD);
                        }
                        else {
                            APIClient.getScheduledRacesByYear(LocalDate.now().getYear(), result -> {
                                RaceCalendar.addEvents(this, result.getRaces());
                            });
                        }
                        break;
                    case R.id.action_remove_from_calendar:
                        if (checkSelfPermission(WRITE_CALENDAR) != PERMISSION_GRANTED){
                            requestPermissions(new String[]{WRITE_CALENDAR, READ_CALENDAR},
                                    RC_CALENDAR_RW_ACTION_REMOVE);
                        }
                        else {
                            APIClient.getScheduledRacesByYear(LocalDate.now().getYear(), result -> {
                                RaceCalendar.removeEvents(this, true);
                            });
                            break;
                        }
                }
                return true;
            });
            popup.show();
        });

        QuestionRepository.getInstance(this)
                .getHasMoreQuestionsObservable()
                .observe(this, hasMoreQuestions -> {
                    if(!hasMoreQuestions){
                        btn_randomQuestion.setOnClickListener(v -> {
                            Toast.makeText(this, "You've answered all the questions", Toast.LENGTH_SHORT).show();
                        });
                        btn_randomQuestion.setBackground(getDrawable(R.drawable.disabled_dice));
                        stopService(new Intent(this, LocationService.class));
                    }
                });

        if (checkSelfPermission(ACCESS_FINE_LOCATION) == PERMISSION_GRANTED){
            mStartService(LocationService.class);
        } else {
            requestPermissions(new String[]{ACCESS_FINE_LOCATION}, RC_FINE_LOCATION);
        }

        mStartService(RaceService.class);
    }

    public void mStartService(Class<?> serviceClass){
        if(!isServiceRunning(serviceClass)){
            Intent intent = new Intent(this, serviceClass);
            startService(intent);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case RC_FINE_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED){
                    mStartService(LocationService.class);
                }
                new BackgroundLocationRationaleDialog()
                        .show(getSupportFragmentManager(), null);
                break;
            case RC_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED){
                    startActivity(new Intent(MainActivity.this, CameraActivity.class));
                }
                break;
            case RC_CALENDAR_RW_ACTION_ADD:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED){
                    APIClient.getScheduledRacesByYear(LocalDate.now().getYear(), result -> {
                        RaceCalendar.addEvents(this, result.getRaces());
                    });
                }
                break;
            case RC_CALENDAR_RW_ACTION_REMOVE:
                if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED){
                    APIClient.getScheduledRacesByYear(LocalDate.now().getYear(), result -> {
                        RaceCalendar.removeEvents(this, true);
                    });
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (backPressedBefore) {
            super.onBackPressed();
            return;
        }

        this.backPressedBefore = true;
        Toast.makeText(this, "Press again to quit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedBefore = false;
            }
        }, 2000);
    }
}