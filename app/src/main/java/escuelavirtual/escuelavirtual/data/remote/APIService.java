package escuelavirtual.escuelavirtual.data.remote;

import java.util.List;

import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.Tag;
import escuelavirtual.escuelavirtual.data.TemaPersistible;
import escuelavirtual.escuelavirtual.data.UsuarioPersistible;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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

    @GET("/get_tag")
    Call<List<Tag>> getTag(@Query("foto") String foto);


    @HTTP(method = "DELETE", path = "/delete_tag", hasBody = true)
    Call<Tag> deleteTag(@Body Tag tag);

    @POST("/guardar_cursos")
    @FormUrlEncoded
    Call<String> guardarCurso(@Field("curso") String curso,
                              @Field("descripcion") String descripcion,
                              @Field("ejecicioList") List<Ejercicio> ejecicioList,
                              @Field("docente") String docente);

    @GET("/get_cursos")
    Call<List<CursoPersistible>> getCurso(@Query("docente") String docente);


    @HTTP(method = "DELETE", path = "/delete_cursos", hasBody = true)
    Call<String> deleteCurso(@Body CursoPersistible cursoPersistible);

    @POST("/post_temas")
    @FormUrlEncoded
    Call<String> guardarTemas(@Field("tema") String tema,
                              @Field("docente") String doocente);

    @GET("/get_temas")
    Call<List<TemaPersistible>> getTema(@Query("docente") String docente);


    @HTTP(method = "DELETE", path = "/delete_temas", hasBody = true)
    Call<String> deleteTema(@Body TemaPersistible temaPersistible);

    @POST("/update_temas")
    @FormUrlEncoded
    Call<String> updateTemas(@Field("viejo") String viejo,
                              @Field("tema") String tema,
                              @Field("docente") String docente);

    @POST("/update_cursos")
    @FormUrlEncoded
    Call<String> updateCursos(@Field("codViejo") String codViejo,
                              @Field("codNuevo") String codNuevo,
                              @Field("descViejo") String descViejo,
                              @Field("descNuevo") String descNuevo,
                              @Field("docente") String docente
                              );

    @POST("/post_usuario")
    @FormUrlEncoded
    Call<String> guardarUsuario(@Field("nombre") String nombre,
                                @Field("perfil") int perfil,
                                @Field("uid") String uid,
                                @Field("token") String token);

    @GET("/gat_usuario")
    Call<UsuarioPersistible> getUsuario(@Query("uid") String uid);


}