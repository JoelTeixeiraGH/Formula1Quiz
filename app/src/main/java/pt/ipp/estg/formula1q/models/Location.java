package pt.ipp.estg.formula1q.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location{

    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("long")
    @Expose
    private String _long;
    @SerializedName("locality")
    @Expose
    private String locality;
    @SerializedName("country")
    @Expose
    private String country;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLong() {
        return _long;
    }

    public void setLong(String _long) {
        this._long = _long;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public android.location.Location toGoogleLocation(){
        android.location.Location newLocation = new android.location.Location("");
        newLocation.setLatitude(Double.valueOf(lat));
        newLocation.setLongitude(Double.valueOf(_long));

        return newLocation;
    }
}