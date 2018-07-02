package escuelavirtual.escuelavirtual.data.remote;

public class ApiUtils {

    private static APIService apiService;

    private ApiUtils() {}

    //public static final String BASE_URL = "https://us-central1-escuelavirtual-bd.cloudfunctions.net/";
    public static final String BASE_URL = "https://us-central1-escuelavirtual-8f556.cloudfunctions.net/";


    public static APIService getAPIService() {
        if (apiService == null) {
            apiService = RetrofitClient.getClient(BASE_URL).create(APIService.class);
            return apiService;
        } else {
            return apiService;
        }
    }
}