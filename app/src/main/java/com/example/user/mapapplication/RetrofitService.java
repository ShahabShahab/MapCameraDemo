package com.example.user.mapapplication;

import com.example.user.mapapplication.Model.ClinicResponse;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 6/21/2017.
 */

public interface RetrofitService {

    @GET("/api/v1/patients/clinics/locations")
    Call<ClinicResponse> getClinicInRegion(@Query("sw") String sw, @Query("ne") String ne);

}
