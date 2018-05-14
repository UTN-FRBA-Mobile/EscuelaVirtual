package escuelavirtual.escuelavirtual.data.remote;

public class ApiUtils {

    private ApiUtils() {}

    public static final String BASE_URL = "https://us-central1-escuelavirtual-bd.cloudfunctions.net/";

    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}