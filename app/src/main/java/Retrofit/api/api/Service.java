package Retrofit.api.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.BenzinskaModel;
import Models.DatesModel;
import Models.DistanceValidationModel;
import Models.GorivoModel;
import Models.HistoryModel;
import Models.LineGasModel;
import Models.LoginResponseModel;
import Models.ObrtniciModel;
import Models.Ok;
import Models.OpcenitoStatistikaModel;
import Models.OtherExpensesModel;
import Models.OstaliTroskoviNaziviModel;
import Models.OstaliTroskoviStatistikaModel;
import Models.PieGasModel;
import Models.PieOtherModel;
import Models.PieServisiModel;
import Models.PodvrstaModel;
import Models.RazlogModel;
import Models.RegisterResponseModel;
import Models.ParametersModel;
import Models.ServiceModel;
import Models.ServiceNameModel;
import Models.ServiceStatistikaModel;
import Models.ServiceExpensesModel;
import Models.TocenjeModel;
import Models.TocenjeStatistikaModel;
import Models.VehicleModel;
import Models.VehicleProducerModel;
import Models.VehicleTypeModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Interface koji sadrži sve metode za dohvat podataka sa baze koja se nalazi na serveru.
 * Korištenje Retrofira
 */

public interface Service {


    @Headers("Content-Type: application/json")
    @POST("api/tocenja")
    Call<ArrayList<TocenjeModel>> getTocenja(@Body ParametersModel parametersModel);



    @Headers("Content-Type: application/json")
    @POST("api/tocenja/{id}")
    Call<TocenjeModel> getTocenjebyID(@Path("id") String groupId);


    @Headers("Content-Type: application/json")
    @GET("api/benzinska")
    Call<ArrayList<BenzinskaModel>> getBenzinske();



    @Headers("Content-Type: application/json")
    @GET("api/gorivo")
    Call<ArrayList<GorivoModel>> getGorivo();



    @Headers("Content-Type: application/json")
    @GET("api/podvrste")
    Call<ArrayList<PodvrstaModel>> getPodvrsta();



    @Headers("Content-Type: application/json")
    @GET("api/razlog")
    Call<ArrayList<RazlogModel>> getRazlog();


    @Headers("Content-Type: application/json")
    @POST("api/tocenja/add")
    Call<Ok> addTocenje(@Body TocenjeModel tocenjeModel);


    @Headers("Content-Type: application/json")
    @POST("api/benzinska/add")
    Call<Ok> addBenzinska(@Body BenzinskaModel benzinskaModel);

    @Headers("Content-Type: application/json")
    @POST("api/razlog/add")
    Call<Ok> addRazlog(@Body RazlogModel razlogModel);

    @Headers("Content-Type: application/json")
    @POST("api/gorivo/add")
    Call<Ok> addGorivo(@Body GorivoModel gorivoModel);



    @DELETE("api/tocenja/delete/{id}")
    Call<Ok> deleteTocenje(@Path("id") int tocenjeId);


    @PUT("api/tocenja/update/{id}")
    Call<Ok> updateTocenje(@Body TocenjeModel tocenjeModel,@Path("id") int tocenjeId);




    //za servise i troskove + za ove stavke NazivServisa i Obrtnika

    @Headers("Content-Type: application/json")
    @POST("api/servisi")
    Call<List<ServiceModel>> getServise(@Body ParametersModel parametersModel);

    @DELETE("api/servisi/delete/{id}")
    Call<Ok> deleteServisi(@Path("id") int servisId);


    @DELETE("api/servisi/troskovi/delete/{id}")
    Call<Ok> deleteTroskovi(@Path("id") int troskovi_servisId);


    @PUT("api/servisi/update/{id}")
    Call<Ok> updateServisi(@Body ServiceModel serviceModel, @Path("id") int servisiId);

    @Headers("Content-Type: application/json")
    @POST("api/servisi/add")
    Call<Ok> addServisi(@Body ServiceModel serviceModel);

    @Headers("Content-Type: application/json")
    @POST("api/servisi/troskovi/add")
    Call<Ok> addServisiTroskovi (@Body ServiceExpensesModel serviceExpensesModel);


    //Naziv obrnika

    @Headers("Content-Type: application/json")
    @GET("api/serviseri")
    Call<ArrayList<ObrtniciModel>> getObrtnici();

    @Headers("Content-Type: application/json")
    @POST("api/serviseri/add")
    Call<Ok> addObrtnik(@Body ObrtniciModel obrtniciModel);


    @PUT("api/serviseri/update/{id}")
    Call<Ok> updateObrtnik(@Body ObrtniciModel obrtniciModel,@Path("id") int obrtnikId);

    @DELETE("api/serviseri/delete/{id}")
    Call<Ok> deleteObrtnik(@Path("id") int obrtnikId);


    // Nazivi servisa


    @Headers("Content-Type: application/json")
    @GET("api/servisi_naziv")
    Call<ArrayList<ServiceNameModel>> getNazivServisa();

    @Headers("Content-Type: application/json; charset=utf-8")
    @POST("api/servisi_naziv/add")
    Call<Ok> addNazivServisa(@Body ServiceNameModel serviceNameModel);


    @PUT("api/servisi_naziv/update/{id}")
    Call<Ok> updateNazivServisa(@Body ServiceNameModel serviceNameModel, @Path("id") int nazivId);

    @DELETE("api/servisi_naziv/delete/{id}")
    Call<Ok> deleteNazivServisa(@Path("id") int nazivId);



    //NaziviOstalih Troskova

    @Headers("Content-Type: application/json")
    @GET("api/ostali_troskovi_nazivi")
    Call<ArrayList<OstaliTroskoviNaziviModel>> getNaziveTroskova();

    @Headers("Content-Type: application/json")
    @POST("api/ostali_troskovi_nazivi/add")
    Call<Ok> addNazivOstalogTroska(@Body OstaliTroskoviNaziviModel ostaliTroskoviNaziviModel);


    @PUT("api/ostali_troskovi_nazivi/update/{id}")
    Call<Ok> updateNazivOstalogTroska(@Body OstaliTroskoviNaziviModel ostaliTroskoviNaziviModel,@Path("id") int nazivId);

    @DELETE("api/ostali_troskovi_nazivi/delete/{id}")
    Call<Ok> deleteNazivOstalogTroska(@Path("id") int nazivId);



        // za Osale troskove



    //za servise i troskove + za ove stavke NazivServisa i Obrtnika

    @Headers("Content-Type: application/json")
    @POST("api/ostali_troskovi")
    Call<ArrayList<OtherExpensesModel>> getOstaleTroskove(@Body ParametersModel parametersModel);

    @DELETE("api/ostali_troskovi/delete/{id}")
    Call<Ok> deleteOstaleTroskak(@Path("id") int ostaliTrosakId);


    @DELETE("api/ostali_troskovi/troskovi/delete/{id}")
    Call<Ok> deleteTroskoviostale(@Path("id") int troskovi_servisId);

//problem kaj je kad je jedan dan 0 kl, a treba biti
    @PUT("api/ostali_troskovi/update/{id}")
    Call<Ok> updateOstaliTroskovi(@Body OtherExpensesModel otherExpensesModel, @Path("id") int ostaliTroskoviId);

    @Headers("Content-Type: application/json")
    @POST("api/ostali_troskovi/add")
    Call<Ok> addOstaliTroskovi(@Body OtherExpensesModel otherExpensesModel);


    //Statistika


    @Headers("Content-Type: application/json")
    @POST("api/statistika_servisi")
    Call<ServiceStatistikaModel> getServiceStatistic(@Body ParametersModel parametersModel);

    @Headers("Content-Type: application/json")
    @POST("api/statistika_servisi/grafikon1")
    Call<ArrayList<PieServisiModel>> getDataForPieServisi(@Body ParametersModel parametersModel);

    @Headers("Content-Type: application/json")
    @POST("api/statistika_servisi/grafikon2")
    Call<ArrayList<PieServisiModel>> getDataForLineServisi(@Body ParametersModel parametersModel);


    @Headers("Content-Type: application/json")
    @POST("api/statistika_ostali_troskovi")
    Call<OstaliTroskoviStatistikaModel> getOtherStatistic(@Body ParametersModel parametersModel);

    @Headers("Content-Type: application/json")
    @POST("api/statistika_ostali_troskovi/grafikon1")
    Call<ArrayList<PieOtherModel>> getDataForPieOther(@Body ParametersModel parametersModel);

    @Headers("Content-Type: application/json")
    @POST("api/statistika_ostali_troskovi/grafikon2")
    Call<ArrayList<PieOtherModel>> getDataForLineOther(@Body ParametersModel parametersModel);


    @Headers("Content-Type: application/json")
    @POST("api/statistika_tocenje")
    Call<TocenjeStatistikaModel> getTocenjeStatistic(@Body ParametersModel parametersModel);

    @Headers("Content-Type: application/json")
    @POST("api/statistika_tocenje/grafikon1")
    Call<ArrayList<PieGasModel>> getDataForPieGas(@Body ParametersModel parametersModel);

    @Headers("Content-Type: application/json")
    @POST("api/statistika_tocenje/grafikon2")
    Call<ArrayList<PieGasModel>> getDataForPie2Gas(@Body ParametersModel parametersModel);


    @Headers("Content-Type: application/json")
    @POST("api/statistika_tocenje/grafikon3")
    Call<ArrayList<LineGasModel>> getDataForLineGas(@Body ParametersModel parametersModel);


    @Headers("Content-Type: application/json")
    @POST("api/statistika_opcenito")
    Call<ArrayList<OpcenitoStatistikaModel>> getOpcenitoStatistic(@Body ParametersModel parametersModel);


    @Headers("Content-Type: application/json")
    @POST("api/povijest")
    Call<ArrayList<HistoryModel>> getHistory(@Body ParametersModel parametersModel);


    @Headers("Content-Type: application/json")
    @POST("api/povijest/datumi")
    Call<DatesModel> getMaxMinDate(@Body ParametersModel parametersModel);


    //Za vozila


    @Headers("Content-Type: application/json")
    @GET("api/vozila")
    Call<ArrayList<VehicleModel>> getVehicle();

    @Headers("Content-Type: application/json")
    @POST("api/vozila/add")
    Call<Ok> insertVehicle(@Body VehicleModel vehicleModel);

    @Headers("Content-Type: application/json")
    @PUT("api/vozila/update/{id}")
    Call<Ok> updateVehicle(@Body VehicleModel vehicleModel,@Path("id") int idVozila);

    @Headers("Content-Type: application/json")
    @DELETE("api/vozila/delete/{id}")
    Call<Ok> deleteVehicle(@Path("id") int idVozila);


//proizvodaci vozila


    @Headers("Content-Type: application/json")
    @GET("api/proizvodac")
    Call<ArrayList<VehicleProducerModel>> getVehicleProducers();

    @Headers("Content-Type: application/json")
    @POST("api/proizvodac/add")
    Call<Ok> insertVehicleProducer(@Body VehicleProducerModel vehicleModel);

    @Headers("Content-Type: application/json")
    @PUT("api/proizvodac/update/{id}")
    Call<Ok> updateVehicleProducer(@Body VehicleModel vehicleModel,@Path("id") int idProducera);

    @Headers("Content-Type: application/json")
    @DELETE("api/proizvodac/delete/{id}")
    Call<Ok> deleteVehicleProducer(@Body VehicleModel vehicleModel,@Path("id") int idProducera);




//tipovi vozila


    @Headers("Content-Type: application/json")
    @GET("api/vozilo_vrsta")
    Call<ArrayList<VehicleTypeModel>> getVehicleType();

    @Headers("Content-Type: application/json")
    @POST("api/vozilo_vrsta/add")
    Call<Ok> insertVehicleType(@Body VehicleModel vehicleModel);

    @Headers("Content-Type: application/json")
    @PUT("api/vozilo_vrsta/update/{id}")
    Call<Ok> updateVehicleType(@Body VehicleModel vehicleModel,@Path("id") int idType);

    @Headers("Content-Type: application/json")
    @DELETE("api/vozilo_vrsta/delete/{id}")
    Call<Ok> deleteVehicleType(@Body VehicleModel vehicleModel,@Path("id") int idType);


// LogInRegister

    @FormUrlEncoded
    @POST("api/register")
    Call<RegisterResponseModel> userRegistration(@Field("name") String name, @Field("email") String email, @Field("pass") String password);

    @FormUrlEncoded
    @POST("api/login")
    Call<LoginResponseModel> userLogIn(@Field("email") String email, @Field("pass") String password);

    @FormUrlEncoded
    @POST("api/povijest/datumiNovi")
    Call<DistanceValidationModel> distanceValidation(@Field("vozilo_id") int vozilo_id, @Field("km_unosa") Double km_unosa, @Field("datum_unosa")Date datum_unosa);

}
