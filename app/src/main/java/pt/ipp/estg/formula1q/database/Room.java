package pt.ipp.estg.formula1q.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import pt.ipp.estg.formula1q.database.question.QuestionDao;
import pt.ipp.estg.formula1q.models.Question;

@androidx.room.Database(entities = {Question.class}, version = 1)
@TypeConverters({Room.Converters.class})
public abstract class Room extends RoomDatabase {

    private static Room instance;

    public abstract QuestionDao questionDao();

    public static synchronized Room getInstance(Context context){
        if(instance == null) {
            instance = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                    Room.class, "f1q_db")
                    .addCallback(cb)
                    .fallbackToDestructiveMigration()
                    .build();
            instance.query("select 1", null); // o callback só é executado se for feita alguma query à db
        }
        return instance;
    }

    private static RoomDatabase.Callback cb = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateQuestionsAsyncTask().execute();
        }
    };

    private static class PopulateQuestionsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            instance.questionDao().insert(
                    new Question(
                            "Which driver suffered the most retirements (DNFs) in the 2020" +
                                    " season?",
                            "Kevin Magnussen",
                            "Lance Stroll",
                            "Max Verstappen",
                            0,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Which driver raced the most laps in 2020?",
                            "Lewis Hamilton",
                            "Kimi Raikkonen",
                            "Lando Norris",
                            2,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Where is Red Bull Racing based?",
                            "United Kingdom",
                            "Italy",
                            "Austria",
                            0,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Which was the very first Formula One race?",
                            "Swedish Winter Grand Prix",
                            "Pau Grand Prix",
                            "Turin Grand Prix",
                            2,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Which of these drivers did NOT set a fastest lap in 2020?",
                            "George Russell",
                            "Esteban Ocon",
                            "Carlos Sainz",
                            1,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Which one of these races had the fewest classified finishers" +
                                    " in 2020?",
                            "Tuscan Grand Prix",
                            "Emilia Romagna Grand Prix",
                            "Eifel Grand Prix",
                            0,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Who received this helmet as a parting gift in 2020?",
                            "Sebastian Vettel",
                            "Romain Grosjean",
                            "Chase Carey",
                            2,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Daniel Ricciardo paid tribute to which of these sportspersons" +
                                    " with a special helmet design in 2020? ",
                            "Dale Earnhardt",
                            "Kobe Bryant",
                            "Diego Maradona",
                            1,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Despite 2020 running to just 17 races, Lewis Hamilton still " +
                                    "managed to win 11 of them. But in which season did Hamilton " +
                                    "last fail to reach double digits for victories?",
                            "2011",
                            "2013",
                            "2017",
                            2,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Which driver said this during a race in 2020: “Did you hydrate " +
                                    "during the race? You must have some sweaty hands as well so " +
                                    "don’t forget to sanitise”?",
                            "Max Verstappen – 70th Anniversary Grand Prix",
                            "Lando Norris – Styrian Grand Prix",
                            "Pierre Gasly – Italian Grand Prix",
                            0,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "What was special about Lando Norris’ helmet for the " +
                                    "British Grand Prix 2020?",
                            "It was designed by Norris to raise money for the charity Mind",
                            "It was designed by a six-year-old",
                            "It featured Nigel Mansell’s helmet colours",
                            1,
                            Question.Category.STATIC));
            instance.questionDao().insert(
                    new Question(
                            "Which of these was the first Grand Prix of 2020 to have " +
                                    "fans in the stands?",
                            "Eifel Grand Prix",
                            "Pau Grand Prix",
                            "Sakhir Grand Prix",
                            2,
                            Question.Category.STATIC));
            return null;
        }
    }

    public static class Converters {
        @TypeConverter
        public static Question.Category fromStringToCategory(String string) {
            return Question.Category.valueOf(string);
        }

        @TypeConverter
        public static String fromCategoryToString(Question.Category category){
            return category.toString();
        }
    }
}
