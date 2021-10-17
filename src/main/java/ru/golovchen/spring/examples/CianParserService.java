package ru.golovchen.spring.examples;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

@Service
public class CianParserService {
    public List<Offer> getHouses(int page, long minPrice, long maxPrice) throws IOException {
        HttpPost request = new HttpPost("https://api.cian.ru/search-offers/v2/search-offers-desktop/");
        //language=JSON
        final String json = "{\"jsonQuery\":{\"_type\":\"suburbansale\",\"price\":{\"type\":\"range\",\"value\":{\"gte\":1,\"lte\":99999999999}},\"object_type\":{\"type\":\"terms\",\"value\":[1]},\"engine_version\":{\"type\":\"term\",\"value\":2},\"page\":{\"type\":\"term\",\"value\":2},\"region\":{\"type\":\"terms\",\"value\":[2]}}}";
        final Gson gson = new Gson();
        final JsonObject searchRequest = gson.fromJson(json, JsonObject.class);
        searchRequest.getAsJsonObject("jsonQuery").getAsJsonObject("page").addProperty("value", page);
        searchRequest.getAsJsonObject("jsonQuery").getAsJsonObject("price").getAsJsonObject("value").addProperty("gte", minPrice);
        searchRequest.getAsJsonObject("jsonQuery").getAsJsonObject("price").getAsJsonObject("value").addProperty("lte", maxPrice);
        request.setEntity(new StringEntity(gson.toJson(searchRequest), ContentType.APPLICATION_JSON));

        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String entityContents = EntityUtils.toString(entity);

        final JsonObject responseObject = gson.fromJson(entityContents, JsonObject.class);
        final String offersAsJsonString = responseObject.getAsJsonObject("data").get("offersSerialized").toString();

        return gson.fromJson(offersAsJsonString, new TypeToken<List<Offer>>() {}.getType());
    }
}
