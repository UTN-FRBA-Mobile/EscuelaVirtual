package escuelavirtual.escuelavirtual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_global_id);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

//     /Testeo de RecyclerView - Carga de items

        List<Ejercicio> ejecicios = new ArrayList<>();
        ejecicios.add(new Ejercicio(R.string.ejercicio_1));

        List<Curso> cursos = new ArrayList<>();
        cursos.add(new Curso(R.string.curso_1, ejecicios));
        cursos.add(new Curso(R.string.curso_2, ejecicios));
        cursos.add(new Curso(R.string.curso_3, ejecicios));
        cursos.add(new Curso(R.string.curso_4, ejecicios));
        cursos.add(new Curso(R.string.curso_5, ejecicios));
        cursos.add(new Curso(R.string.curso_6, ejecicios));
        cursos.add(new Curso(R.string.curso_7, ejecicios));
        cursos.add(new Curso(R.string.curso_8, ejecicios));
        cursos.add(new Curso(R.string.curso_9, ejecicios));

//     \End

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new ModelAdapterCurso(cursos));
    }

    public void gotoCurso(View view){

        Intent intent = new Intent(this, CursoActivity.class);
        startActivity(intent);
    }

/*        Button goToCommentsPageButton = (Button) findViewById(R.id.go_to_comments_page_id);
        goToCommentsPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent intent = new Intent(button.getContext(), CommentsOnPhotoActivity.class);
                startActivity(intent);
            }
        });*/
}
