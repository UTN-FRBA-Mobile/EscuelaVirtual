package escuelavirtual.escuelavirtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ModelAdapterEjercicio extends RecyclerView.Adapter<ModelAdapterEjercicio.ModelViewHolder>{

    private List<Ejercicio> items;

    public ModelAdapterEjercicio (List<Ejercicio> items) {
        this.items = items;
    }

    @Override
    public ModelAdapterEjercicio.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_ejercicio, parent, false));
    }

    @Override
    public void onBindViewHolder(ModelAdapterEjercicio.ModelViewHolder holder, int position) {
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
            this.textView = (TextView) itemView.findViewById(R.id.tv_ejercicio_id);
        }

        public void bind(Ejercicio ejer) {
            textView.setText(ejer.getName());
        }
    }
}