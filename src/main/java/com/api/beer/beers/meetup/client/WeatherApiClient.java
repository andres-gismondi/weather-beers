package com.api.beer.beers.meetup.client;

import com.api.beer.beers.meetup.client.model.WeatherResponse;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherApiClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherApiClient.class);

    private static final String URL = "https://community-open-weather-map.p.rapidapi.com/find";
    private static final String HEADER_API_KEY = "6981542202msh8376179f7d38fa6p123e5ajsnbdadb4e2c316";
    private static final String HEADER_HOST = "community-open-weather-map.p.rapidapi.com";

    private ObjectMapper mapper;
    private OkHttpClient client;

    public WeatherApiClient() {
        this.mapper = new ObjectMapper();
        this.client = new OkHttpClient();
        this.mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public WeatherResponse getWeather(String city) throws IOException {
        LOGGER.info("Getting city {} with client", city);
        HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
        urlBuilder.addQueryParameter("q",city)
                .addQueryParameter("units", "metric");
        String finalUrl = urlBuilder.build().toString();
        LOGGER.info("Getting request client");
        Request request = new Request.Builder()
                .url(finalUrl)
                .get()
                .addHeader("x-rapidapi-host",HEADER_HOST)
                .addHeader("x-rapidapi-key",HEADER_API_KEY)
                .build();

        Call call = this.client.newCall(request);
        LOGGER.info("Executing request");
        Response response = call.execute();
        final byte[] responseBytes = response.body().bytes();
        return this.mapper.readValue(responseBytes, WeatherResponse.class);
    }

}
