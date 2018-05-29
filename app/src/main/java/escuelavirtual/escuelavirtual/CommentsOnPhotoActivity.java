package escuelavirtual.escuelavirtual;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import escuelavirtual.escuelavirtual.data.Tag;
import escuelavirtual.escuelavirtual.data.remote.APIService;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsOnPhotoActivity extends AppCompatActivity {

    public int centralPositionOfTag = 35;
    public Map<Integer, TagView> tagsAdded;
    public Map<Integer, TagView> tagsGuardar;
    private APIService mAPIService;

    /** Called when the activity is first created. */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_on_photo);
        mAPIService = ApiUtils.getAPIService();

        //foto_test en algun momento va a tener que ser el nombre de la imagen
        getCommentsTag("foto_test");


        Button persistir = (Button) findViewById(R.id.save_id);
        persistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                        Integer _final = tagsGuardar.size();
                        for (Integer key : tagsGuardar.keySet()) {
                            _final--;
                            TagView tag = tagsGuardar.get(key);
                            sendTag(key, tag, "foto_test", 0 == _final);
                        }

                } else {
                    Toast.makeText(CommentsOnPhotoActivity.this, "No dispone de conexión a internet",Toast.LENGTH_SHORT).show();
                }



            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_create_and_edit_comments_on_photo_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: completar con las tags persistidas
        tagsAdded = new HashMap<>();
        tagsGuardar = new HashMap<>();

        ViewsController.setCommentBox((EditText) findViewById(R.id.comment_box_id));
        ViewsController.getCommentBox().setVisibility(View.INVISIBLE);
        ViewsController.getCommentBox().setEnabled(false);

        ViewsController.setTagAndNumberLayout((RelativeLayout)findViewById(R.id.tag_and_number_id));
        ViewsController.getTagAndNumberLayout().setVisibility(View.INVISIBLE);

        ViewsController.setNumberOverTag((TextView)findViewById(R.id.tag_number_id));

        // Button to add comment of comment box
        ViewsController.setAddCommentButton((Button) findViewById(R.id.apply_comment_id));
        ViewsController.getAddCommentButton().setVisibility(View.INVISIBLE);
        ViewsController.getAddCommentButton().setEnabled(false);

        // Button to edit comment of comment box
        ViewsController.setEditCommentButton((Button) findViewById(R.id.edit_comment_id));
        ViewsController.getEditCommentButton().setVisibility(View.INVISIBLE);
        ViewsController.getEditCommentButton().setEnabled(false);

        // Button to delete comment of comment box
        ViewsController.setDeleteCommentButton((Button) findViewById(R.id.delete_comment_id));
        ViewsController.getDeleteCommentButton().setVisibility(View.INVISIBLE);
        ViewsController.getDeleteCommentButton().setEnabled(false);

        // Button to save changes
        ViewsController.setSaveButton((Button) findViewById(R.id.save_id));
        ViewsController.getSaveButton().setVisibility(View.VISIBLE);
        ViewsController.getSaveButton().setEnabled(true);

        // Button to Info Details
        ViewsController.setInfoDetailButton((Button) findViewById(R.id.info_id));
        ViewsController.getInfoDetailButton().setVisibility(View.VISIBLE);
        ViewsController.getInfoDetailButton().setEnabled(false);

        // Comment section
        ViewsController.setCommentSection((LinearLayout)findViewById(R.id.comment_section_id));

        ViewsController.setBaseImage((ImageView)findViewById(R.id.base_image_id));
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
                    TagDrawer.drawTag(tagsAdded,tagsGuardar, tagView);

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

                    ViewsController.getAddCommentButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            TagView tagViewToUpdate = tagsAdded.get(ViewsController.getNumberOverTagAsInteger());
                            tagViewToUpdate.setComment(ViewsController.getCommentBox().getText().toString());
                            ViewsController.turnOffCommentBox();
                        }
                    });

                    ViewsController.getEditCommentButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            ViewsController.getCommentBox().setEnabled(true);
                            ViewsController.getCommentBox().setSelection(ViewsController.getCommentBox().getText().length());
                            ViewsController.getCommentBox().requestFocus();
                            ViewsController.getKeyboard().showSoftInput(ViewsController.getCommentBox(), InputMethodManager.SHOW_IMPLICIT);
                        }
                    });

                    ViewsController.getDeleteCommentButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View button) {
                            ViewsController.turnOffCommentBox();
                            tagsAdded.remove(ViewsController.getNumberOverTagAsInteger());
                            tagsGuardar.remove(ViewsController.getNumberOverTagAsInteger());
                            TagDrawer.reDrawTags(tagsAdded, false);
                        }
                    });
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
    }



    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, CursoActivity.class));
        return true;
    }



    //Eric
    public void sendTag(final Integer key, final TagView tag, String foto, final boolean _final) {
        mAPIService.saveTag(tag.getCentralPositionOfTag(), tag.getLeftMargin(), tag.getTopMargin(),tag.getNumberOfTag(),tag.getComment(),foto)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            tagsGuardar.remove(key);
                            if(tagsGuardar.size() == 0 && _final){
                                Toast.makeText(CommentsOnPhotoActivity.this, "Sus cambios han sido guardados.",Toast.LENGTH_SHORT).show();
                            }else{
                                if(_final) Toast.makeText(CommentsOnPhotoActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(CommentsOnPhotoActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCommentsTag(String foto) {
        mAPIService.getTag(foto)
               .enqueue(new Callback<List<Tag>>() {
                    @Override
                    public void onResponse(Call<List<Tag>> call, Response<List<Tag>> response) {
                        if(response.isSuccessful()) {

                            List<Tag> lista = response.body();
                            for (Tag tag : lista) {
                                final TagView tagView = new TagView(ViewsController.getBaseImage().getContext(), tag);
                                RelativeLayout baseImageLayout = (RelativeLayout) findViewById(R.id.tags_layout_id);
                                ViewsController.setBaseImageLayout(baseImageLayout);
                                TagDrawer.drawTag(tagsAdded,tagsGuardar, tagView);
                            }

                            ViewsController.turnOffCommentBox();
                            tagsGuardar = new HashMap<>();


                        }
                    }

                    @Override
                    public void onFailure(Call<List<Tag>> call, Throwable t) {
                        //Toast.makeText(CommentsOnPhotoActivity.this, t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

}
