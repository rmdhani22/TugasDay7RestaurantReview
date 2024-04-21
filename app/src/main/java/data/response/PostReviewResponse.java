package data.response;

import java.util.List; // Menambahkan import List
import com.google.gson.annotations.SerializedName;

public class PostReviewResponse {
    @SerializedName("customerReviews")
    private List<CustomerReviewsItem> customerReviews;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public List<CustomerReviewsItem> getCustomerReviews() {
        return customerReviews;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}