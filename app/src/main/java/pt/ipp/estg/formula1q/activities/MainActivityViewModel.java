package pt.ipp.estg.formula1q.activities;

import android.app.Application;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import pt.ipp.estg.formula1q.database.Firebase;
import pt.ipp.estg.formula1q.utils.GetProfilePicAsyncTask;

public class  MainActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Bitmap> profilePic;
    private MutableLiveData<Integer> userScore;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        new GetProfilePicAsyncTask(bitmap -> this.profilePic.setValue(bitmap))
                .execute();

        profilePic = new MutableLiveData<>();
        userScore = new MutableLiveData<>();

        Firebase.getScoreRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    int points = snapshot.getValue(Integer.class).intValue();
                    userScore.setValue(points);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public void addPoints(int points){
        Firebase.addPoints(points);
    }

    public MutableLiveData<Integer> getUserScoreObservable(){
        return userScore;
    }
    public MutableLiveData<Bitmap> getUserPicObservable(){
        return profilePic;
    }
}
