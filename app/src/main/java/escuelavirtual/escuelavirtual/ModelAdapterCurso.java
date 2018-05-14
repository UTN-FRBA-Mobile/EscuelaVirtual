package escuelavirtual.escuelavirtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class ModelAdapterCurso extends RecyclerView.Adapter<ModelAdapterCurso.ModelViewHolder>{

    private List<Curso> items;

    public ModelAdapterCurso(List<Curso> items) {
        this.items = items;
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

        public ModelViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.text_view);
        }

        public void bind(Curso curso) {
            textView.setText(curso.getTextId());
        }
    }
}