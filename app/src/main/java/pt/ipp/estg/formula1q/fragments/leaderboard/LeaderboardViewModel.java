package pt.ipp.estg.formula1q.fragments.leaderboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import pt.ipp.estg.formula1q.database.Firebase;
import pt.ipp.estg.formula1q.models.User;

public class LeaderboardViewModel extends ViewModel {

    private MutableLiveData<List<User>> userList;

    public LeaderboardViewModel() {
        userList = new MutableLiveData<>();

        Firebase.getUsersRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<User> newUserList = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()){
                    User user = child.getValue(User.class);
                    newUserList.add(user);
                }
                userList.setValue(newUserList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    public LiveData<List<User>> getUserListObservable() {
        return userList;
    }
}