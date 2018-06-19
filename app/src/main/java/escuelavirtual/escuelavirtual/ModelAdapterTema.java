package escuelavirtual.escuelavirtual;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by lucas on 3/6/2018.
 */

public class ModelAdapterTema extends RecyclerView.Adapter<ModelAdapterTema.ModelViewHolder>{
    private List<String> items;
    private final ClickListener listener;


    public ModelAdapterTema(List<String> items, ClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void crearTema(){
        items.add("Sasa Agregado");
        notifyItemInserted(items.size() - 1);
    }

    @Override
    public ModelAdapterTema.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelAdapterTema.ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_tema, parent, false),listener);
    }

    @Override
    public void onBindViewHolder(ModelAdapterTema.ModelViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public class ModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private ImageButton editTema;
        private ImageButton deleteTema;
        private TextView textView;
        private WeakReference<ClickListener> listenerRef;

        public ModelViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tv_tema_id);

            listenerRef = new WeakReference<>(listener);
            editTema = (ImageButton) itemView.findViewById(R.id.ibtn_edit_tema_id);
            deleteTema = (ImageButton) itemView.findViewById(R.id.ibtn_delete_tema_id);

            editTema.setOnClickListener(this);
            deleteTema.setOnClickListener(this);
        }

        public void bind(String tema) {
            textView.setText(tema);
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
