package escuelavirtual.escuelavirtual.docente;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Gravity;
import android.view.MotionEvent;
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
import escuelavirtual.escuelavirtual.alumno.CursoAddActivity;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.Tag;
import escuelavirtual.escuelavirtual.data.remote.APIService;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsOnPhotoActivity extends AppCompatActivity {

    public int centralPositionOfTag = 35;
    public Map<Integer, TagView> tagsAdded = new HashMap<>();
    public APIService mAPIService;
    private static Respuesta respuestaSeleccionada;
    private static Ejercicio ejercicioSeleccionado;
    private static Curso cursoSeleccionado;

    public static void setRespuestaSeleccionada(Respuesta respuestaSeleccionada) {
        CommentsOnPhotoActivity.respuestaSeleccionada = respuestaSeleccionada;
    }

    public static void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        CommentsOnPhotoActivity.ejercicioSeleccionado = ejercicioSeleccionado;
    }

    public static void setCursoSeleccionado(Curso cursoSeleccionado) {
        CommentsOnPhotoActivity.cursoSeleccionado = cursoSeleccionado;
    }

    /** Called when the activity is first created. */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_on_photo);
        mAPIService = ApiUtils.getAPIService();

        Button persistir = (Button) findViewById(R.id.save_id);
        persistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                        Integer _final = tagsAdded.size();
                        for (Integer key : tagsAdded.keySet()) {
                            _final--;
                            TagView tag = tagsAdded.get(key);
                            String ruta = respuestaSeleccionada.getCodigoCurso() + respuestaSeleccionada.getCodigoEjercicio() + respuestaSeleccionada.getCodigoRespuesta();
                            sendTag(key, tag, respuestaSeleccionada.getImagenRespuestaBase64(), 0 == _final, ruta);
                        }

                } else {
                    Toast.makeText(CommentsOnPhotoActivity.this, "No dispone de conexión a internet",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_create_and_edit_comments_on_photo_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ViewsController.setCommentBox((EditText) findViewById(R.id.comment_box_id));
        ViewsController.getCommentBox().setVisibility(View.INVISIBLE);
        ViewsController.getCommentBox().setEnabled(false);

        ViewsController.setTagAndNumberLayout((RelativeLayout)findViewById(R.id.tag_and_number_id));
        ViewsController.getTagAndNumberLayout().setVisibility(View.INVISIBLE);

        ViewsController.setNumberOverTag((TextView)findViewById(R.id.tag_number_id));

        ViewsController.setAddCommentButton((Button) findViewById(R.id.apply_comment_id));
        ViewsController.disableAndHideButton("add");

        ViewsController.setEditCommentButton((Button) findViewById(R.id.edit_comment_id));
        ViewsController.disableAndHideButton("edit");

        ViewsController.setDeleteCommentButton((Button) findViewById(R.id.delete_comment_id));
        ViewsController.disableAndHideButton("delete");

        ViewsController.setSaveButton((Button) findViewById(R.id.save_id));
        ViewsController.enableAndShowButton("save");

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
        ViewsController.getBaseImage().setOnTouchListener(new View.OnTouchListener() {

            final Handler handler = new Handler();
            int touchX;
            int touchY;

            // This runs when a tag is added over image
            Runnable mLongPressed = new Runnable() {
                public void run() {
                    // Keyboard
                    ViewsController.setKeyboard((InputMethodManager) getSystemService(ViewsController.getCommentBox().getContext().INPUT_METHOD_SERVICE));
                    ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);

                    // Create a tag
                    Integer leftMargin = touchX - centralPositionOfTag;
                    Integer topMargin = touchY - centralPositionOfTag;
                    Tag tag = new Tag(centralPositionOfTag, leftMargin, topMargin);
                    final TagView tagView = new TagView(ViewsController.getBaseImage().getContext(), tag);

                    // Draw a tag
                    RelativeLayout baseImageLayout = (RelativeLayout) findViewById(R.id.tags_layout_id);
                    ViewsController.setBaseImageLayout(baseImageLayout);
                    TagDrawer.drawTag(tagsAdded, tagView);

                    // Set pipe/focus into comment box
                    ViewsController.getCommentBox().setEnabled(true);
                    ViewsController.getCommentBox().setVisibility(View.VISIBLE);
                    ViewsController.getCommentBox().requestFocus();

                    // Activate button to send comments when first tag is added
                    ViewsController.getAddCommentButton().setEnabled(true);
                    ViewsController.getEditCommentButton().setEnabled(false);
                    ViewsController.getDeleteCommentButton().setEnabled(true);

                    ViewsController.getAddCommentButton().setVisibility(View.VISIBLE);
                    ViewsController.getEditCommentButton().setVisibility(View.INVISIBLE);
                    ViewsController.getDeleteCommentButton().setVisibility(View.VISIBLE);

                    ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);

                    ViewsController.setAddButtonClickListener(tagsAdded);
                    ViewsController.setEditButtonClickListener(tagsAdded);
                    ViewsController.setDeleteButtonClickListener(tagsAdded, mAPIService);
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do this to allow clearing comment section if image is touched
                        ViewsController.turnOffCommentBox();
                        TagDrawer.reDrawTags(tagsAdded, true);

                        touchX = (int) event.getX();
                        touchY = (int) event.getY();
                        handler.postDelayed(mLongPressed, 200);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_MOVE:
                        handler.removeCallbacks(mLongPressed);
                        break;
                }
                return true;
            }
        });

        ViewsController.setKeyboard((InputMethodManager) getSystemService(ViewsController.getCommentBox().getContext().INPUT_METHOD_SERVICE));
        getCommentsTag(respuestaSeleccionada.getImagenRespuestaBase64());
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

    //Eric
    public void sendTag(final Integer key, final TagView tag, String foto, final boolean _final, final String ruta) {
        final ProgressDialog progress = new ProgressDialog(CommentsOnPhotoActivity.this);
        progress.setMessage("Guardando...");
        progress.setCanceledOnTouchOutside(false);
        progress.setTitle("Aguarde un instante...");
        Loading.ejecutar(progress);
        mAPIService.saveTag(ruta,tag.getCentralPositionOfTag(), tag.getLeftMargin(), tag.getTopMargin(),tag.getNumberOfTag(),tag.getComment(),foto)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {

                            if(_final){
                                Toast.makeText(CommentsOnPhotoActivity.this, "Sus cambios han sido guardados.",Toast.LENGTH_SHORT).show();
                            }else{
                                if(_final) Toast.makeText(CommentsOnPhotoActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                            }

                            Loading.terminar(progress);

                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(CommentsOnPhotoActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                        Loading.terminar(progress);

                    }
                });
    }

    private void getCommentsTag(String foto) {
        final ProgressDialog progress = new ProgressDialog(CommentsOnPhotoActivity.this);
        progress.setMessage("Buscando...");
        progress.setCanceledOnTouchOutside(false);
        progress.setTitle("Aguarde un instante...");
        Loading.ejecutar(progress);
        RelativeLayout baseImageLayout = (RelativeLayout) findViewById(R.id.tags_layout_id);
        ViewsController.setBaseImageLayout(baseImageLayout);
        ViewsController.turnOffCommentBox();
        tagsAdded = new HashMap<>();

        mAPIService.getTag(foto)
               .enqueue(new Callback<List<Tag>>() {
                    @Override
                    public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                        if(response.isSuccessful()) {
                            Loading.terminar(progress);
                            List<Tag> lista = response.body();
                            for (Tag tag : lista) {
                                TagView tagView = new TagView(ViewsController.getBaseImage().getContext(), tag);
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
                        Loading.terminar(progress);
                        //Toast.makeText(CommentsOnPhotoActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

}
