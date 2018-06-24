package escuelavirtual.escuelavirtual.alumno;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import escuelavirtual.escuelavirtual.R;

public class RespuestaAddActivity extends AppCompatActivity {

    private static ImageView rtaPhoto;
    private static Bitmap rtaPhotoBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respuesta_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rtaPhoto = (ImageView) findViewById(R.id.rta_photo_id);
        rtaPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try{
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            rtaPhoto.setBackgroundColor(Color.WHITE);
            rtaPhoto.setImageBitmap(bitmap);
            rtaPhotoBitmap = bitmap;
        }catch (Exception e){
            // No hacer ningún cambio porque no se sacó la foto
        }
    }

}
