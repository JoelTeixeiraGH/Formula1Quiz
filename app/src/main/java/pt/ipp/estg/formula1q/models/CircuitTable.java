package pt.ipp.estg.formula1q.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CircuitTable {

    @SerializedName("Circuits")
    @Expose
    private List<Circuit> circuits = null;

    public List<Circuit> getCircuits() {
        return circuits;
    }

    public void setCircuits(List<Circuit> circuits) {
        this.circuits = circuits;
    }
}