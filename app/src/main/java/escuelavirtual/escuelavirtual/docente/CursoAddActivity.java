package escuelavirtual.escuelavirtual.docente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoAddActivity extends AppCompatActivity {

    private EditText codigoCurso;
    private EditText nombreCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_add);

        codigoCurso = (EditText) findViewById(R.id.et_curso_code_id);
        nombreCurso = (EditText) findViewById(R.id.et_curso_descripcion_id);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EventoTeclado keyboard = new EventoTeclado();
        nombreCurso.setOnEditorActionListener(keyboard);
    }

    class EventoTeclado implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                confirm_AddCurso(null);
            }
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                confirm_logout();
            default:
                return false;
        }
    }

    private void confirm_logout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("¿Cerrar Sesión?");
        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        logout();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    public void confirm_AddCurso(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "Esta a punto de crear el curso:%n%s%n%n¿Confirma su creación?",
                nombreCurso.getText()));
        alertDialogBuilder.setPositiveButton("Sí",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        addCursoConfirm();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void addCursoConfirm() {
        final ProgressDialog progress = new ProgressDialog(CursoAddActivity.this);
        progress.setMessage("Guardando....");
        progress.setCanceledOnTouchOutside(false);
        progress.setTitle("Guardando el curso:  " + nombreCurso.getText().toString());
        Loading.ejecutar(progress);
        ApiUtils.getAPIService().guardarCurso(codigoCurso.getText().toString(), nombreCurso.getText().toString(),null,FirebaseAuth.getInstance().getCurrentUser().getUid())
        .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(CursoAddActivity.this, "Sus cambios han sido guardados.",Toast.LENGTH_SHORT).show();
                    MainActivity.cursos.add(new Curso(codigoCurso.getText().toString(), nombreCurso.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),null));
                    Intent intent = new Intent(CursoAddActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(CursoAddActivity.this, "El curso ya existe",Toast.LENGTH_SHORT).show();
                }
                Loading.terminar(progress);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(CursoAddActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                Loading.terminar(progress);
            }
        });
    }

    public void cancel_AddCurso(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "¿Desea cancelar la creación del curso?"));

        alertDialogBuilder.setPositiveButton("Sí",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        addCursoCanceled();
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void addCursoCanceled() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
