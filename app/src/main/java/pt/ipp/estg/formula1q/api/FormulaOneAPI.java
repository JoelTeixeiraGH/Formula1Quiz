package pt.ipp.estg.formula1q.api;

import pt.ipp.estg.formula1q.models.CircuitTable;
import pt.ipp.estg.formula1q.models.RaceTable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FormulaOneAPI {
    @GET("/api/f1/{year}.json")
    Call<RaceTable> getSchedules(@Path("year") int year);

    @GET("/api/f1/circuits.json")
    Call<CircuitTable> getCircuits();
}
