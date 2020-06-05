package dev.dstankovic.popularmoviesjava.requests;

import dev.dstankovic.popularmoviesjava.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();

    private static Retrofit.Builder sRetrofitBuilder = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client);

    private static Retrofit sRetrofit = sRetrofitBuilder.build();

    private static MovieApi sMovieApi = sRetrofit.create(MovieApi.class);

    public static MovieApi getMovieApi() {
        return sMovieApi;
    }

}
