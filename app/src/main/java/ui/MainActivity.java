package ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.example.restaurantreview.databinding.ActivityMainBinding;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

import data.response.CustomerReviewsItem;
import data.response.PostReviewResponse;
import data.response.Restaurant;
import data.response.RestaurantResponse;
import data.retrofit.ApiConfig;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static final String TAG = "MainActivity";
    private static final String RESTAURANT_ID = "uewq1zg2zlskfw1e867";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Buat objek LayoutManager dan atur ke RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvReview.setLayoutManager(layoutManager);

        // Panggil metode untuk mendapatkan data restoran
        findRestaurant();

        // Atur listener untuk tombol kirim
        binding.btnSend.setOnClickListener(view -> {
            if (binding.edReview.getText() != null) {
                postReview(binding.edReview.getText().toString());
            }
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }


    private void postReview(String review) {
        showLoading(true);
        // Membuat pemanggilan API untuk memposting review
        Call<PostReviewResponse> client = ApiConfig.getApiService().postReview(RESTAURANT_ID, "Curly", review);
        client.enqueue(new Callback<PostReviewResponse>() {
            @Override
            public void onResponse(Call<PostReviewResponse> call, Response<PostReviewResponse> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Mengatur data review setelah berhasil memposting
                        setReviewData(response.body().getCustomerReviews());
                    }
                } else {
                    if (response.body() != null) {
                        // Menangani kesalahan jika terjadi
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<PostReviewResponse> call, Throwable t) {
                showLoading(false);
                // Menangani kesalahan jaringan atau lainnya
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void findRestaurant() {
        showLoading(true);
        // Membuat pemanggilan API untuk mendapatkan detail restoran
        Call<RestaurantResponse> client = ApiConfig.getApiService().getRestaurant(RESTAURANT_ID);
        client.enqueue(new Callback<RestaurantResponse>() {
            @Override
            public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                showLoading(false);
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        // Mengatur data restoran dan review setelah mendapatkan respons sukses
                        setRestaurantData(response.body().getRestaurant());
                        setReviewData(response.body().getRestaurant().getCustomerReviews());
                    }
                } else {
                    if (response.body() != null) {
                        // Menangani kesalahan jika terjadi
                        Log.e(TAG, "onFailure: " + response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                showLoading(false);
                // Menangani kesalahan jaringan atau lainnya
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void setRestaurantData(Restaurant restaurant) {
        // Mengatur data restoran ke tampilan
        binding.tvTitle.setText(restaurant.getName());
        binding.tvDescription.setText(restaurant.getDescription());
        Glide.with(MainActivity.this)
                .load("https://restaurant-api.dicoding.dev/images/large/" + restaurant.getPictureId())
                .into(binding.ivPicture);
    }

    private void setReviewData(List<CustomerReviewsItem> customerReviews) {
        ArrayList<String> listReview = new ArrayList<>();
        for (CustomerReviewsItem review : customerReviews) {
            listReview.add(review.getReview() + "\n- " + review.getName());
        }
        ReviewAdapter adapter = new ReviewAdapter(listReview);
        binding.rvReview.setAdapter(adapter);
        binding.edReview.setText("");
    }


    private void showLoading(Boolean isLoading) {
        // Menampilkan atau menyembunyikan loading indicator
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}