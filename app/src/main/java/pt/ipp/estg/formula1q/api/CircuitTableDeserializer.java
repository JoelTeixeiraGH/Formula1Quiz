package pt.ipp.estg.formula1q.api;

import pt.ipp.estg.formula1q.models.CircuitTable;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class CircuitTableDeserializer implements JsonDeserializer<CircuitTable> {
    @Override
    public CircuitTable deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext)
            throws JsonParseException {
        JsonElement data = jsonElement.getAsJsonObject()
                .get("MRData").getAsJsonObject()
                .get("CircuitTable");

        return new Gson().fromJson(data, CircuitTable.class);
    }
}