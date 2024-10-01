package pt.ipp.estg.formula1q.api;

import pt.ipp.estg.formula1q.models.CircuitTable;
import pt.ipp.estg.formula1q.models.RaceTable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    public interface APIResult<T> {
        public void onResult(T result);
    }

    private static Retrofit getRetrofit(){
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(RaceTable.class, new RaceTableDeserializer())
                .registerTypeAdapter(CircuitTable.class, new CircuitTableDeserializer())
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl("http://ergast.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    private static FormulaOneAPI getApi(){
        return getRetrofit().create(FormulaOneAPI.class);
    }

    public static void getScheduledRacesByYear(int year, APIResult<RaceTable> apiResult){
        getApi().getSchedules(year).enqueue(new Callback<RaceTable>() {
            @Override
            public void onResponse(Call<RaceTable> call, Response<RaceTable> response) {
                apiResult.onResult(response.body());
            }

            @Override
            public void onFailure(Call<RaceTable> call, Throwable t) {
                // no-op
            }
        });
    }

    public static void getCircuits(APIResult<CircuitTable> apiResult){
        getApi().getCircuits().enqueue(new Callback<CircuitTable>() {
            @Override
            public void onResponse(Call<CircuitTable> call, Response<CircuitTable> response) {
                apiResult.onResult(response.body());
            }

            @Override
            public void onFailure(Call<CircuitTable> call, Throwable t) {
                // no-op
            }
        });
    }
}
