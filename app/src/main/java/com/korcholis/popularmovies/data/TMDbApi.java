package com.korcholis.popularmovies.data;

import android.content.Context;

import com.korcholis.popularmovies.exceptions.ConnectionNotAvailableException;
import com.korcholis.popularmovies.utils.ConnectionChecker;
import com.korcholis.popularmovies.utils.Constants;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDbApi {
    private static final String TMDB_URL = "https://api.themoviedb.org/3/";
    private static final String QUERY_API_KEY = "api_key";

    private static Retrofit instance = null;
    private static Context context;

    public static TMDbApiSignature instance(Context context) {
        TMDbApi.context = context;
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(TMDB_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(createOkHttpClient())
                    .build();
        }

        return instance.create(TMDbApiSignature.class);
    }


    public enum SortCriteria {
        MostPopular("popular"),
        HighRated("top_rated");

        private final String value;

        SortCriteria(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    public static OkHttpClient createOkHttpClient() {
        File httpCacheDirectory = new File(context.getCacheDir(), Constants.CACHE_DIR);
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);

        final OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {

            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request original = chain.request();
                final HttpUrl originalHttpUrl = original.url();
                final HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(QUERY_API_KEY, Constants.TMDB_API_KEY)
                        .build();
                final Request.Builder requestBuilder = original.newBuilder()
                        .url(url);
                final Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (ConnectionChecker.isNetworkAvailable(context)) {
                    int maxAge = 60; // read from cache for 1 minute
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                } else {
                    int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        }).addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                if (ConnectionChecker.isNetworkAvailable(context)) {
                    return chain.proceed(chain.request());
                } else {
                    throw new ConnectionNotAvailableException();
                }
            }
        }).cache(cache);

        return httpClient.build();
    }
}
