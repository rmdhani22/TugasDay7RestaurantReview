package data.retrofit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import android.util.Log;

import androidx.annotation.NonNull;

public class ApiConfig {
    @NonNull
    public static ApiService getApiService() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restaurant-api.dicoding.dev/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();


        ApiService apiService = retrofit.create(ApiService.class);


        Log.d("ApiConfig", "ApiService: " + apiService);

        return apiService;
    }
}