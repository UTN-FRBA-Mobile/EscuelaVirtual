package escuelavirtual.escuelavirtual;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ModelAdapterCurso extends RecyclerView.Adapter<ModelAdapterCurso.ModelViewHolder>{

    private List<Curso> items;
    private boolean docente;

    public ModelAdapterCurso(List<Curso> items, Context parent) {
        this.items = items;
        this.docente = (parent.getClass().getName().contains("docente"));
    }

    @Override
    public ModelAdapterCurso.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_curso, parent, false));
    }

    @Override
    public void onBindViewHolder(ModelAdapterCurso.ModelViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;
        private AppCompatImageButton deleteButton;
        private AppCompatImageButton editButton;

        public ModelViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tv_curso_id);
            this.deleteButton = (AppCompatImageButton) itemView.findViewById(R.id.ibtn_delete_curso_id);
            this.editButton = (AppCompatImageButton) itemView.findViewById(R.id.ibtn_edit_curso_id);
            this.editButton.setVisibility(docente?View.VISIBLE:View.GONE);
        }

        public void bind(Curso curso) {
            textView.setText(curso.getName());
            this.deleteButton.setTag(curso.getName());
            this.editButton.setTag(curso.getName());
        }
    }
}