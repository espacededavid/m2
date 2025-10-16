package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Make an API call to the specified URL and return the result.
     * @param url the URL to query
     * @return the response as a String
     * @throws IOException if there is a problem connecting to the API
     */
    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        DogApiBreedFetcher example = new DogApiBreedFetcher();
        String response;
        try {
            response = example.run("https://dog.ceo/api/breed/" + breed + "/list");
            System.out.println(response);

            // Parse the JSON response
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("message");

            // Initialize List<String> from JSONArray
            List<String> subBreeds = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                subBreeds.add(jsonArray.getString(i));
            }

            return subBreeds;
        } catch (IOException | JSONException e) {
//            throw new BreedNotFoundException("Sub-breeds not found");
            return List.of();
        }
    }

    // public static void main(String[] args) {
    //     DogApiBreedFetcher example = new DogApiBreedFetcher();
    //     String breed = "hound";
    //     example.getSubBreeds(breed);
    // }
}