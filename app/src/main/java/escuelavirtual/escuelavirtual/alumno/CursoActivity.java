package escuelavirtual.escuelavirtual.alumno;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import escuelavirtual.escuelavirtual.Curso;
import escuelavirtual.escuelavirtual.Ejercicio;
import escuelavirtual.escuelavirtual.ModelAdapterEjercicio;
import escuelavirtual.escuelavirtual.R;
import escuelavirtual.escuelavirtual.common.Loading;
import escuelavirtual.escuelavirtual.common.LogoutableActivity;
import escuelavirtual.escuelavirtual.data.EjercicioPersistible;
import escuelavirtual.escuelavirtual.data.remote.ApiUtils;
import escuelavirtual.escuelavirtual.docente.TemasActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CursoActivity extends LogoutableActivity {

    static final List<Ejercicio> ejercicios = new ArrayList<>();
    private static Curso cursoSeleccionado;
    private static AutoCompleteTextView temaEjercicioTextView;



    public static Curso getCursoSeleccionado() {
        return cursoSeleccionado;
    }

    public static void setCursoSeleccionado(Curso c) {
        cursoSeleccionado = c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curso_a);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.main_title_id)).setText("Curso: " + cursoSeleccionado.getCodigo() + " - Ejercicios");

        if (ejercicios.isEmpty()) {
            cargarEjercicios();
        } else {
            refreshEjercicios();
        }

        temaEjercicioTextView = (AutoCompleteTextView) findViewById(R.id.tema_id);
        this.disableTema();

        TemasActivity.getTemasAvailable(this, null);

        cargarEjercicios();
    }

    public void disableTema(){
        temaEjercicioTextView.setEnabled(false);
        temaEjercicioTextView.setBackgroundResource(R.drawable.rounded_text_comment_box_gray);
        temaEjercicioTextView.setTextColor(Color.GRAY);
    }

    public void enableTema(){
        temaEjercicioTextView.setEnabled(true);
        temaEjercicioTextView.setBackgroundResource(R.drawable.rounded_text_comment_box);
        temaEjercicioTextView.setTextColor(Color.BLACK);
        temaEjercicioTextView.setOnEditorActionListener(new EventoTeclado());

        if(TemasActivity.getTemasObtenidos()){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TemasActivity.getTemasDisponibles());
            temaEjercicioTextView.setAdapter(adapter);
            Toast.makeText(this, "Temas cargados con éxito", Toast.LENGTH_SHORT).show();
        }else{
            this.disableTema();
            Toast.makeText(this, "No se pudieron cargar los temas. Por favor, esperá unos segundos hasta que se carguen, y volvé a intentar", Toast.LENGTH_SHORT).show();
        }
    }

    class EventoTeclado implements TextView.OnEditorActionListener{
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if(actionId == EditorInfo.IME_ACTION_DONE){
                InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(temaEjercicioTextView.getWindowToken(), 0);
                temaEjercicioTextView.clearFocus();
                aplicarFiltro();
            }
            return false;
        }
    }

    private void aplicarFiltro(){
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvEjercicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        if("".equals(temaEjercicioTextView.getText().toString())){
            recyclerView.setAdapter(new ModelAdapterEjercicio(ejercicios).setFromAlumno());
        }else{
            List<Ejercicio> ejerciciosFiltrados = new ArrayList<>();
            for(Ejercicio ejercicio : ejercicios){
                if(temaEjercicioTextView.getText().toString().equals(ejercicio.getTema())){
                    ejerciciosFiltrados.add(new Ejercicio(ejercicio.getCodigoEjercicio(), ejercicio.getImagenBase64(), ejercicio.getTema()));
                }
            }

            recyclerView.setAdapter(new ModelAdapterEjercicio(ejerciciosFiltrados).setFromAlumno());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        cargarEjercicios();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEjercicios();
    }

    private void cargarEjercicios() {
        final ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Cargando ejercicios...");
        progress.setTitle("Por favor, espere...");
        progress.setCanceledOnTouchOutside(false);
        Loading.ejecutar(progress);

        ApiUtils.getAPIService().getEjercicios(cursoSeleccionado.getDocente(), cursoSeleccionado.getCodigo())
                .enqueue(new Callback<List<EjercicioPersistible>>() {
                    @Override
                    public void onResponse(Call<List<EjercicioPersistible>> call, Response<List<EjercicioPersistible>> response) {
                        if(response.isSuccessful()) {
                            List<EjercicioPersistible> lista = response.body();
                            ejercicios.clear();
                            for (EjercicioPersistible ejercicio : lista) {
                                ejercicios.add(new Ejercicio(ejercicio.getCodigoEjercicio(), ejercicio.getImagenBase64(), ejercicio.getTema()));
                            }
                            refreshEjercicios();
                        }
                        Loading.terminar(progress);
                    }

                    @Override
                    public void onFailure(Call<List<EjercicioPersistible>> call, Throwable t) {
                        Toast.makeText(escuelavirtual.escuelavirtual.alumno.CursoActivity.this, "Ha ocurrido un error cargando los ejercicios. Intente nuevamente.", Toast.LENGTH_SHORT).show();
                        Loading.terminar(progress);
                    }
                });
    }

    private void refreshEjercicios() {
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvEjercicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterEjercicio(ejercicios).setFromAlumno());
    }

    @Override
    public boolean onSupportNavigateUp() {
        startActivity(new Intent(this, MainActivity.class));
        return true;
    }

    public void gotoEjercicio(View button) {
        EjercicioActivity.setEjercicioSeleccionado(this.findExerciseSelected(button));
        EjercicioActivity.setCursoSeleccionado(cursoSeleccionado);
        Intent intent = new Intent(this, EjercicioActivity.class);
        startActivity(intent);
    }

    private Ejercicio findExerciseSelected(View view){
        for(int i = 0; i < this.ejercicios.size(); i++){
            if(this.ejercicios.get(i).getCodigoEjercicio().equals(view.getTag())){
                return this.ejercicios.get(i);
            }
            try{
                if(((TextView)view).getText().toString().contains(ejercicios.get(i).getCodigoEjercicio())){
                    return this.ejercicios.get(i);
                }
            }catch (Exception e){

            }
        }
        return null;
    }
}
