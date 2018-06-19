package escuelavirtual.escuelavirtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lucas on 3/6/2018.
 */

public class ModelAdapterRespuesta extends RecyclerView.Adapter<ModelAdapterRespuesta.ModelViewHolder>{
    private List<String> items;
    private final ClickListener listener;


    public ModelAdapterRespuesta(List<String> items, ClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void crearTema(){
        items.add("Sasa Agregado");
        notifyItemInserted(items.size() - 1);
    }

    @Override
    public ModelAdapterRespuesta.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelAdapterRespuesta.ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_respuesta, parent, false),listener);
    }

    @Override
    public void onBindViewHolder(ModelAdapterRespuesta.ModelViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView textView;
        private WeakReference<ClickListener> listenerRef;

        public ModelViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tv_respuesta_id);

            listenerRef = new WeakReference<>(listener);

            textView.setOnClickListener(this);
        }

        public void bind(String respuesta) {
            textView.setText(respuesta);
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().onPositionClicked(v,getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public interface ClickListener {

        void onPositionClicked(View v,int position);

        void onLongClicked(View v, int position);
    }
}
