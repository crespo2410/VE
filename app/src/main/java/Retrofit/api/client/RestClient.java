package Retrofit.api.client;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import Retrofit.api.api.Service;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Stvaranje http klijenta za Retrofit
 */

public class RestClient {

    //moj BASE_URL,ako ne radi umjesto http://diplomskivehicleapi.esy.es postoviti lokalnu ip adresu prema uputama
    //iz diplomskog rada
    private static final String BASE_URL = "http://diplomskivehicleapi.esy.es/slimapp/public/";
    private static RestClient restClient = null;
    private Service service;

    private RestClient() {

        //Postavljanje logginga kako bi mogli pratiti što se dešava kod
        //samih poziva
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        //converter tj. parser
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();


        //konfiguracija Retrofita, postavljanje clienta,baseUrl,Convertera
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        //kreiranje interfacea
        service = retrofit.create(Service.class);
    }


    /**
     * Metoda za dobivanje instance klase
     *
     * @return - vrača instancu klase
     */
    public static RestClient getInstance() {
        if (restClient == null) {

            restClient = new RestClient();

        }

        return restClient;
    }


    /**
     * Metoda za dobivanje stvorenog servisa - sučelja
     *
     * @return
     */
    public Service getApiService() {
        return service;
    }


}
