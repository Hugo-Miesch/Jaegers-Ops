package fr.esgi.pajavafx;

import fr.esgi.pajavafx.models.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ApiClient {
    private static final String API_URL = "http://jaegers-ops.duckdns.org:54122";

    public static void addUser(User user, Callback<Boolean> callback) {
        JSONObject json = new JSONObject();
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        sendRequest("POST", "/clients", json.toString(), callback);
    }

    public static void getUsers(Callback<List<User>> callback) {
        sendRequest("GET", "/clients", null, callback);
    }

    public static void updateUser(User user, Callback<Boolean> callback) {
        JSONObject json = new JSONObject();
        json.put("name", user.getName());
        json.put("email", user.getEmail());
        sendRequest("PUT", "/clients/" + user.getId(), json.toString(), callback);
    }

    public static void deleteUser(int userId, Callback<Boolean> callback) {
        sendRequest("DELETE", "/clients/" + userId, null, callback);
    }

    public static void loginSuperAdmin(String email, String password, Callback<String> callback) {
        JSONObject json = new JSONObject();
        json.put("email", email);
        json.put("password", password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "/super_admin/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();

        HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    System.out.println("Response Status Code: " + response.statusCode());
                    System.out.println("Response Body: " + response.body());

                    if (response.statusCode() == 200) {
                        callback.onResponse("Connexion réussie !");
                    } else {
                        String errorMessage = "Erreur lors de la connexion.";
                        try {
                            JSONObject jsonResponse = new JSONObject(response.body());
                            if (jsonResponse.has("error")) {
                                errorMessage = jsonResponse.getString("error");
                            } else if (jsonResponse.has("message")) {
                                errorMessage = jsonResponse.optString("message", errorMessage);
                            }
                        } catch (Exception e) {
                            System.out.println("Erreur lors de la transformation de la réponse JSON : " + e.getMessage());
                        }
                        callback.onResponse(errorMessage);
                    }
                })
                .exceptionally(e -> {
                    e.printStackTrace();
                    callback.onResponse("Erreur de connexion.");
                    return null;
                });
    }

    private static void sendRequest(String method, String endpoint, String jsonData, Callback<?> callback) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL + endpoint).openConnection();
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            if (jsonData != null) {
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = jsonData.getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
            }

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            if (method.equals("GET")) {
                JSONArray jsonArray = new JSONArray(response.toString());
                List<User> users = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    User user = new User(
                            jsonObject.getInt("id"),
                            jsonObject.getString("name"),
                            jsonObject.getString("email")
                    );
                    users.add(user);
                }
                ((Callback<List<User>>) callback).onResponse(users);
            } else {
                ((Callback<Boolean>) callback).onResponse(connection.getResponseCode() == HttpURLConnection.HTTP_CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onResponse(null);
        }
    }

    public interface Callback<T> {
        void onResponse(T result);
    }
}
