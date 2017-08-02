package Retrofit.api.api;


import Models.maps_model.Example;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * interface koji sadrži metode putem kojih dohvacam podatke vezane za Google Maps
 * Korištenje Retrofira
 */
public interface RetrofitMaps {

    @GET("api/place/nearbysearch/json?sensor=true&key=AIzaSyCG_ID5stN-waJVBjJwOgsWmxjU85bjxXs")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);


    @GET("api/directions/json?key=AIzaSyCG_ID5stN-waJVBjJwOgsWmxjU85bjxXs")
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}


