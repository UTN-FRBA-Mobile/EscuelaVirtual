package escuelavirtual.escuelavirtual.data.remote;
import escuelavirtual.escuelavirtual.data.Tag;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @POST("/post_items")
    @FormUrlEncoded
    Call<String> saveTag(@Field("centralPositionOfTag") int centralPositionOfTag,
                      @Field("leftMargin") int leftMargin,
                      @Field("topMargin") int topMargin,
                      @Field("numberOfTag") int numberOfTag,
                      @Field("comment") String comment,
                      @Field("foto") String foto);
   // Call<String> saveTag(@Field("message") String esUnString);


    @GET("/get_items/{id}")
    @FormUrlEncoded
    Call<Tag> getTag(@Query("foto") String foto);
}