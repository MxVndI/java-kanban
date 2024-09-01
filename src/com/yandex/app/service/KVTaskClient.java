package com.yandex.app.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.yandex.app.exception.RequestFailedExeption;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private String url;
    private String tokenApi;
    private HttpClient client;

    public KVTaskClient(int port) {
        url = "http://localhost:" + port + "/";
        client = HttpClient.newHttpClient();
        tokenApi = register(url);
    }

    private String register(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "registered"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new RequestFailedExeption("Невозможно сделать зарегестрированный запрос, код ошибки = " + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            throw new RequestFailedExeption("Невозможно создать запрос", exception);
        }
    }

    public String load(String key) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + ">&API_TOKEN=" + tokenApi))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RequestFailedExeption("Невозможно загрузить запрос, код ошибки - " + response.statusCode());
            }
            JsonElement jsonElement = JsonParser.parseString(response.body());
            System.out.println("Данные успешно загружены с сервера");
            return jsonElement.toString();
        } catch (IOException | InterruptedException exception) {
            throw new RequestFailedExeption("Невозможно загрузить запрос", exception);
        }
    }


    public void save(String key, String value) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + ">&API_TOKEN=" + tokenApi))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RequestFailedExeption("Невозможно сохранить запрос, код ошибки - " + response.statusCode());
            }
        } catch (IOException | InterruptedException exception) {
            throw new RequestFailedExeption("Невозможно сохранить запрос", exception);
        }
    }
}
