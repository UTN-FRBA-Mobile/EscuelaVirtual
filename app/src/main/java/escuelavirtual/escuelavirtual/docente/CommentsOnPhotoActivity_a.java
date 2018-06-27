package escuelavirtual.escuelavirtual.docente;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.Respuesta;
import escuelavirtual.escuelavirtual.data.Tag;
import escuelavirtual.escuelavirtual.data.remote.APIService;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsOnPhotoActivity_a extends AppCompatActivity {

    public int centralPositionOfTag = 35;
    public Map<Integer, TagView> tagsAdded = new HashMap<>();
    public APIService mAPIService;
    private static Respuesta respuestaSeleccionada;
    private static Ejercicio ejercicioSeleccionado;
    private static Curso cursoSeleccionado;

    public static void setRespuestaSeleccionada(Respuesta respuestaSeleccionada) {
        CommentsOnPhotoActivity_a.respuestaSeleccionada = respuestaSeleccionada;
    }

    public static void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        CommentsOnPhotoActivity_a.ejercicioSeleccionado = ejercicioSeleccionado;
    }

    public static void setCursoSeleccionado(Curso cursoSeleccionado) {
        CommentsOnPhotoActivity_a.cursoSeleccionado = cursoSeleccionado;
    }

    /** Called when the activity is first created. */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_on_photo_a);
        mAPIService = ApiUtils.getAPIService();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_create_and_edit_comments_on_photo_id_a);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewsController.setCommentBox((EditText) findViewById(R.id.comment_box_id));
        ViewsController.getCommentBox().setVisibility(View.INVISIBLE);
        ViewsController.getCommentBox().setEnabled(false);

        ViewsController.setTagAndNumberLayout((RelativeLayout)findViewById(R.id.tag_and_number_id));
        ViewsController.getTagAndNumberLayout().setVisibility(View.INVISIBLE);

        ViewsController.setNumberOverTag((TextView)findViewById(R.id.tag_number_id));

        ViewsController.setInfoDetailButton((Button) findViewById(R.id.info_id));
        ViewsController.getInfoDetailButton().setVisibility(View.VISIBLE);
        ViewsController.getInfoDetailButton().setEnabled(true);
        ViewsController.getInfoDetailButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(view.getContext(), "Ejercicio: " + ejercicioSeleccionado.getCodigoEjercicio() + "\nRespuesta: " + respuestaSeleccionada.getCodigoRespuesta() + "\nAlumno: " + respuestaSeleccionada.getNombreAlumno(), Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        // Comment section
        ViewsController.setCommentSection((LinearLayout)findViewById(R.id.comment_section_id));

        ViewsController.setBaseImage((ImageView)findViewById(R.id.base_image_id));
        ViewsController.getBaseImage().setImageBitmap(base64ToBitMap(respuestaSeleccionada.getImagenRespuestaBase64()));
        ViewsController.getBaseImage().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TagDrawer.seletcNoneTag();
            }
        });

        getCommentsTag(respuestaSeleccionada.getCodigoRespuesta());
    }

    private Bitmap base64ToBitMap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    @Override
    public boolean onSupportNavigateUp() {
        EjercicioActivity.setCursoSeleccionado(cursoSeleccionado);
        EjercicioActivity.setEjercicioSeleccionado(ejercicioSeleccionado);
        startActivity(new Intent(this, EjercicioActivity.class));
        return true;
    }

    private void getCommentsTag(String foto) {
        RelativeLayout baseImageLayout = (RelativeLayout) findViewById(R.id.tags_layout_id);
        ViewsController.setBaseImageLayout(baseImageLayout);
        ViewsController.turnOffCommentBox();
        tagsAdded = new HashMap<>();

        // TODO: remover este ejemplo harcodeado
        Tag tag = new Tag(35, 304, 243, 50, "Hola");
        tagsAdded.put(tag.getNumberOfTag(), new TagView(ViewsController.getBaseImage().getContext() ,tag).setFromAlumno());
        tag = new Tag(35, 404, 243, 51, "Chau");
        tagsAdded.put(tag.getNumberOfTag(), new TagView(ViewsController.getBaseImage().getContext() ,tag).setFromAlumno());
        TagDrawer.reDrawTags(tagsAdded, false);
        ViewsController.setKeyboard((InputMethodManager) getSystemService(ViewsController.getCommentBox().getContext().INPUT_METHOD_SERVICE));
        // ------->

        mAPIService.getTag(foto)
                .enqueue(new Callback<List<Tag>>() {
                    @Override
                    public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                        if(response.isSuccessful()) {
                            List<Tag> lista = response.body();
                            for (Tag tag : lista) {
                                TagView tagView = new TagView(ViewsController.getBaseImage().getContext(), tag).setFromAlumno();
                                tagsAdded.put(tag.getNumberOfTag(), tagView);
                            }

                            RelativeLayout baseImageLayout = (RelativeLayout) findViewById(R.id.tags_layout_id);
                            ViewsController.setBaseImageLayout(baseImageLayout);
                            ViewsController.turnOffCommentBox();
                            TagDrawer.reDrawTags(tagsAdded, false);

                            ViewsController.setKeyboard((InputMethodManager) getSystemService(ViewsController.getCommentBox().getContext().INPUT_METHOD_SERVICE));

                            ViewsController.setAddButtonClickListener(tagsAdded);
                            ViewsController.setEditButtonClickListener(tagsAdded);
                            ViewsController.setDeleteButtonClickListener(tagsAdded, mAPIService);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tag>> call, Throwable t) {
                        //Toast.makeText(CommentsOnPhotoActivity_a.this, t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

}
