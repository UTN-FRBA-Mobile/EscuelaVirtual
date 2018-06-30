package escuelavirtual.escuelavirtual.data.remote;

import java.util.List;

import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.EjercicioPersistible;
import escuelavirtual.escuelavirtual.data.RespuestaPersistible;
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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @POST("/post_items/{ruta}")
    @FormUrlEncoded
    Call<String> saveTag(@Path("ruta") String ruta,
                        @Field("centralPositionOfTag") int centralPositionOfTag,
                      @Field("leftMargin") int leftMargin,
                      @Field("topMargin") int topMargin,
                      @Field("numberOfTag") int numberOfTag,
                      @Field("comment") String comment,
                      @Field("foto") String foto);

    @GET("/get_tag/{ruta}")
    Call<List<Tag>> getTag(@Path("ruta") String ruta,
                           @Query("foto") String foto);


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

    @GET("/get_ejercicios")
    Call<List<EjercicioPersistible>> getEjercicios(@Query("docente") String docente,
                                                   @Query("curso") String curso);

    @GET("/get_ejercicios_by_curso")
    Call<List<EjercicioPersistible>> getEjerciciosByCurso(@Query("curso") String curso);

    @GET("/get_respuestas")
    Call<List<RespuestaPersistible>> getRespuestas(
            @Query("codCurso") String codigoCurso,
            @Query("codEjercicio") String codigoEjercicio
    );


    @HTTP(method = "DELETE", path = "/delete_cursos", hasBody = true)
    Call<String> deleteCurso(@Body CursoPersistible cursoPersistible);

    @HTTP(method = "DELETE", path = "/delete_ejercicio", hasBody = true)
    Call<String> deleteEjercicio(@Body EjercicioPersistible cursoPersistible);

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

    @POST("/post_ejercicio")
    @FormUrlEncoded
    Call<String> crearEjercicio(@Field("codCurso") String codigoCurso,
                                  @Field("codEjercicio") String codigoEjercicio,
                                  @Field("imagenBase64") String imagenBase64,
                                  @Field("tema") String tema,
                                  @Field("uid") String uid);


    @PUT("/put_ejercicio")
    @FormUrlEncoded
    Call<String> actualizarEjercicio(@Field("codCurso") String codigoCurso,
                                  @Field("codEjercicioViejo") String codigoEjercicioViejo,
                                  @Field("codEjercicioNuevo") String codigoEjercicioNuevo,
                                  @Field("imagenBase64") String imagenBase64,
                                  @Field("tema") String tema,
                                  @Field("uid") String uid);
    @POST("/post_respuesta")
    Call<RespuestaPersistible> postRespuesta(@Body RespuestaPersistible respuestaP);

    @POST("/post_suscripcion")
    @FormUrlEncoded
    Call<String> postSuscripcion(@Field("codCurso") String codigoCurso,
                               @Field("uidUsuario") String usuario);

    @GET("/get_cursos_suscriptos")
    Call<List<CursoPersistible>> getCursoSuscripto(@Query("alumno") String alumno);

    @GET("/get_curso_by_codigo")
    Call<CursoPersistible> getCursoByCodigo(@Query("codCurso") String codCurso);

    @POST("/delete_cursos_suscriptos")
    @FormUrlEncoded
    Call<String> deleteCursoSuscriptos(@Field("alumno") String alumno,
                                       @Field("curso") String curso);
}