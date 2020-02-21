package com.android.example.github.api;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dependencies in build.gradle
 * implementation "com.squareup.retrofit2:retrofit:2.4.0"
 * implementation "com.squareup.retrofit2:converter-gson:2.4.0"
 * implementation "com.google.code.gson:gson:2.8.6"
 */
public class VJavaEndpoint {

    private static final String TAG = "VJavaEndpoint";
    private static final String BASE_URL = "http://ec2-54-187-236-58.us-west-2.compute.amazonaws.com:8021";

    private final Retrofit retrofit;

    public VJavaEndpoint() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();
    }

    public void searchMotion(int row, int column, @NonNull Date startDate, long timeInterval, @Nullable final CompletionCallback callback) {
        final Api api = retrofit.create(Api.class);

        final long startTimeSec = 1582265703;
        final List<List<Integer>> list = new ArrayList<>();
        for(int i = 0 ;i < 9;i++){
            final List<Integer> motion = new ArrayList<>();
            motion.add(i);
            motion.add(i);
            list.add(motion);
        }
        final MotionSearchBody body = new MotionSearchBody(
                list,
                startTimeSec,
                startTimeSec + timeInterval
        );

        Gson gson = new Gson();
        String json = gson.toJson(body);

        Log.d(TAG,json);
        api.motionSearch(body).enqueue(new Callback<MotionSearchResponse>() {

            @Override
            public void onResponse(@NonNull Call<MotionSearchResponse> call, @NonNull Response<MotionSearchResponse> response) {
                MotionSearchResponse motionSearchResponse = response.body();
                if (motionSearchResponse != null) {
                    if (callback != null) callback.onComplete(motionSearchResponse.getMotionAt(), null);
                } else {
                    if (callback != null) callback.onComplete(new ArrayList<Motion>(), null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MotionSearchResponse> call, @NonNull Throwable t) {
                if (callback != null) callback.onComplete(new ArrayList<Motion>(), t);
            }
        });
    }

    public interface CompletionCallback {
        void onComplete(@NonNull List<Motion> motionList, @Nullable Throwable error);
    }

    public interface Api {
        @Headers({"Content-Type: application/json"})
        @POST("/ios/search")
        Call<MotionSearchResponse> motionSearch(@Body MotionSearchBody motionSearchRequest);
    }

    @SuppressWarnings("unused")
    public class MotionSearchBody {

        @NonNull
        @SerializedName("motionZones")
        private final List<List<Integer>> motionZones;

        @SerializedName("startTimeSec")
        private final long startTimeSec;

        @SerializedName("endTimeSec")
        private final long endTimeSec;

        MotionSearchBody(@NonNull List<List<Integer>> motionZones, long startTimeSec, long endTimeSec) {
            this.motionZones = motionZones;
            this.startTimeSec = startTimeSec;
            this.endTimeSec = endTimeSec;
        }
    }

    @SuppressWarnings("unused")
    public class MotionSearchResponse {

        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        @SerializedName("motionAt")
        private List<List<Long>> motionAt;

        @SerializedName("nextEndTimeSec")
        private int nextEndTimeSec;

        public int getNextEndTimeSec() {
            return nextEndTimeSec;
        }

        List<Motion> getMotionAt() {
            List<Motion> list = new ArrayList<>();
//            for (List<Long> motion : motionAt) {
//                list.add(new Motion(new Date(motion.get(0) * 1000), motion.get(1)));
//            }
            return list;
        }
    }

    public class Motion {
        private Date date;
        private long durationSeconds;

        Motion(Date date, long durationSeconds) {
            this.date = date;
            this.durationSeconds = durationSeconds;
        }

        Date getDate() {
            return date;
        }

        long getDurationSeconds() {
            return durationSeconds;
        }

        @Override
        public String toString() {
            return "Motion(date=" + getDate() + ", durationSeconds=" + getDurationSeconds() + ")";
        }
    }
}
