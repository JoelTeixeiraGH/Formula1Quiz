package pt.ipp.estg.formula1q.fragments.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.ipp.estg.formula1q.R;

public class LeaderboardFragment extends Fragment {

    private RecyclerView leaderboard;
    private LeaderboardViewModel leaderboardViewModel;
    private LeaderboardAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboard = root.findViewById(R.id.rv_leaderboard);
        leaderboard.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LeaderboardAdapter();
        leaderboard.setAdapter(adapter);

        leaderboardViewModel =
                new ViewModelProvider(this).get(LeaderboardViewModel.class);

        leaderboardViewModel
                .getUserListObservable()
                .observe(getViewLifecycleOwner(), userList -> {
                    adapter.setUserList(userList);
                });

        return root;
    }
}