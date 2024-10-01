package pt.ipp.estg.formula1q.fragments.leaderboard;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pt.ipp.estg.formula1q.R;
import pt.ipp.estg.formula1q.models.User;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private static final int LIMIT = 20;
    private List<User> userList;
    private Context mContext;

    public LeaderboardAdapter(){
        this.userList = new ArrayList<>();
    }

    public void setUserList(List<User> userList){
        this.userList = userList;
        userList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if (o1.getScore() >= o2.getScore()) {
                    return -1;
                }
                if (o2.getScore() < o2.getScore()) {
                    return 1;
                }
                return 0;
            }
        });
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_leaderboard_item, parent, false);

        mContext = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.score.setText(String.valueOf(userList.get(position).getScore()));
        holder.username.setText(userList.get(position).getDisplayName());
        holder.ranking.setText(String.valueOf(position+1));

        String usernameFromEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        if(holder.username.getText().toString()
                .equals(usernameFromEmail)){

            Typeface boldTf = ResourcesCompat.getFont(mContext, R.font.formulaonebold);
            holder.score.setTypeface(boldTf);
            holder.username.setTypeface(boldTf);
            holder.ranking.setTypeface(boldTf);
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(userList.size(), LIMIT);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username, score, ranking;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.leaderboard_username);
            score = itemView.findViewById(R.id.leaderboard_score);
            ranking = itemView.findViewById(R.id.leaderboard_ranking);
        }
    }
}
