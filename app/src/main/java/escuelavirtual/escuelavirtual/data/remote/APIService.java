package escuelavirtual.escuelavirtual.data.remote;
import escuelavirtual.escuelavirtual.data.Tag;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {

    @POST("/Hola_Mundo")
    @FormUrlEncoded
    /*Call<Tag> saveTag(@Field("centralPositionOfTag") int centralPositionOfTag,
                      @Field("leftMargin") int leftMargin,
                      @Field("topMargin") int topMargin,
                      @Field("numberOfTag") int numberOfTag,
                      @Field("comment") String comment);*/
    Call<String> saveTag(@Field("message") String esUnString);
}