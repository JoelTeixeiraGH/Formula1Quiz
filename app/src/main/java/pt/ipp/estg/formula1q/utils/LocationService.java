package pt.ipp.estg.formula1q.utils;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import pt.ipp.estg.formula1q.database.question.QuestionRepository;
import pt.ipp.estg.formula1q.api.APIClient;
import pt.ipp.estg.formula1q.models.Circuit;
import pt.ipp.estg.formula1q.models.CircuitTable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.time.LocalDate;
import java.util.List;

public class LocationService extends Service {
    private static final int LOCATION_REQUESTS_INTERVAL = 15 * 60 * 1000;
    private static final int CIRCUIT_RADIUS_IN_METERS = 1500;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private List<Circuit> circuitList;
    private Circuit nearestCircuit;
    private boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        APIClient.getCircuits(new APIClient.APIResult<CircuitTable>() {
            @Override
            public void onResult(CircuitTable result) {
                circuitList = result.getCircuits();
                requestLocationUpdate();
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {

        locationRequest = LocationRequest.create();
        locationRequest
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_REQUESTS_INTERVAL)
                .setFastestInterval(LOCATION_REQUESTS_INTERVAL);

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);

                        QuestionRepository.getInstance(LocationService.this)
                                .getRandomQuestion(question -> {
                                    if(question == null) {
                                        QuestionRepository.getInstance(LocationService.this)
                                                .setHasMoreQuestions(false);
                                        stopSelf();
                                    } else {
                                        for (Location location : locationResult.getLocations()) {
                                            nearestCircuit = getNearestCircuit(location);
                                            if (nearestCircuit != null) {
                                                Notification.send(
                                                        LocationService.this,
                                                        "New Question for " + nearestCircuit.getCircuitName(),
                                                        "Click to answer!",
                                                        nearestCircuit.getCircuitName(),
                                                        String.valueOf(LocalDate.now().getYear()));
                                            }
                                        }
                                    }
                        });
                    }
                }, null
        );
    }

    private Circuit getNearestCircuit(Location currentLocation){
        Location circuitLocation;

        for (Circuit circuit : circuitList) {
             circuitLocation = circuit.getLocation().toGoogleLocation();

            if (currentLocation.distanceTo(circuitLocation) <= CIRCUIT_RADIUS_IN_METERS){
                return circuit;
            }
        }
        return null;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}