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
     * @throws BreedNotFoundException if the breed does not exist (or if the API call fails for any reason)
     */
    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
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
            throw new BreedNotFoundException("Sub-breeds not found");
        }
    }

    public static void main(String[] args) throws BreedNotFoundException {
        DogApiBreedFetcher example = new DogApiBreedFetcher();
        String breed = "hound";
        example.getSubBreeds(breed);
    }
}