package escuelavirtual.escuelavirtual.alumno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import escuelavirtual.escuelavirtual.LoginActivity;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.data.CursoPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CursoAddActivity extends AppCompatActivity {

    private EditText etCursoCode;
    private TextView tvCursoDetail;
    private CursoPersistible cursoASuscribir;
    private FloatingActionButton fabFindCurso;
    private FloatingActionButton fabConfirmAddCurso;
    private FloatingActionButton fabCancelAddCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_add_a);

        etCursoCode = (EditText) findViewById(R.id.et_curso_code_id);
        tvCursoDetail = (TextView) findViewById(R.id.tv_curso_detail_id);
        fabFindCurso = (FloatingActionButton) findViewById(R.id.fabFindCurso_id);
        fabConfirmAddCurso = (FloatingActionButton) findViewById(R.id.fabConfirmCurso_id);
        fabCancelAddCurso = (FloatingActionButton) findViewById(R.id.fabCancelCurso_id);

        showFind(true);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void find_Curso(View view){
        final ProgressDialog progress = new ProgressDialog(CursoAddActivity.this);
        progress.setMessage("Suscribiendose....");
        progress.setTitle("Aguarde un instante....");
        Loading.ejecutar(progress);
        ApiUtils.getAPIService().getCursoByCodigo(etCursoCode.getText().toString())
                .enqueue(new Callback<CursoPersistible>() {
                    @Override
                    public void onResponse(Call<CursoPersistible> call, Response<CursoPersistible> response) {
                        Loading.terminar(progress);
                        if(response.isSuccessful()) {
                            cursoASuscribir = response.body();
                            tvCursoDetail.setText(response.body().getDescripcion());
                            showFind(false);
                        } else {
                            Toast.makeText(CursoAddActivity.this, "No se encontró ningún curso que coincida con su búsqueda.", Toast.LENGTH_SHORT).show();
                            showFind(true);
                        }
                    }
                    @Override
                    public void onFailure(Call<CursoPersistible> call, Throwable t) {
                        Loading.terminar(progress);
                        Toast.makeText(CursoAddActivity.this, "Ha ocurrido un error. Intente nuevamente.",Toast.LENGTH_SHORT).show();
                        showFind(true);
                    }

                });
    }

    public void confirm_AddCurso(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(String.format(
                "Está a punto de inscribirse al curso:%n%s%n%n¿Está seguro?",
                cursoASuscribir.getCurso()));
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
        progress.setMessage("Suscribiendose....");
        progress.setTitle("Aguarde un instante....");
        Loading.ejecutar(progress);
        ApiUtils.getAPIService().postSuscripcion(cursoASuscribir.getCurso(),FirebaseAuth.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(CursoAddActivity.this, "Usted se ha suscripto al curso satisfactoriamente.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CursoAddActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(CursoAddActivity.this, "El curso no existe",Toast.LENGTH_SHORT).show();
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
        alertDialogBuilder.setMessage(String.format("¿Desea cancelar la suscripción al curso?"));

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

    private void showFind(boolean mostrar){
        tvCursoDetail.setVisibility(mostrar?View.GONE:View.VISIBLE);
        fabFindCurso.setVisibility(mostrar?View.VISIBLE:View.GONE);
        fabConfirmAddCurso.setVisibility(mostrar?View.GONE:View.VISIBLE);
        fabCancelAddCurso.setVisibility(mostrar?View.GONE:View.VISIBLE);
    }

}