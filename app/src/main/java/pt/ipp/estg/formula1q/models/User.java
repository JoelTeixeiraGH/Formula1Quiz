package pt.ipp.estg.formula1q.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    private int score;
    private String displayName;

    public User(){}

    public User(int points, String displayName) {
        this.score = score;
        this.displayName = displayName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "User{" +
                ", score=" + score +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
