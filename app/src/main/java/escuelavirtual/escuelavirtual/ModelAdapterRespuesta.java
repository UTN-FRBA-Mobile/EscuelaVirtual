package escuelavirtual.escuelavirtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lucas on 3/6/2018.
 */

public class ModelAdapterRespuesta extends RecyclerView.Adapter<ModelAdapterRespuesta.ModelViewHolder>{
    private Boolean esDocente;
    private List<Respuesta> items;

    public ModelAdapterRespuesta(Boolean esDocente, List<Respuesta> items) {
        this.items = items;
        this.esDocente = esDocente;
    }

    @Override
    public ModelAdapterRespuesta.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelAdapterRespuesta.ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_respuesta, parent, false));
    }

    @Override
    public void onBindViewHolder(ModelAdapterRespuesta.ModelViewHolder holder, int position) {
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
            this.textView = (TextView) itemView.findViewById(R.id.tv_respuesta_id);
        }

        public void bind(Respuesta respuesta) {
            textView.setTag(respuesta.getCodigoRespuesta());
            String rtaLabel = respuesta.getCodigoCurso() + " - " + respuesta.getCodigoEjercicio() + " - " + respuesta.getCodigoRespuesta();
            if(esDocente) rtaLabel += " - " + respuesta.getNombreAlumno();
            textView.setText(rtaLabel);
        }
    }
}
