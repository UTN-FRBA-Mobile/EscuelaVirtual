package escuelavirtual.escuelavirtual;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ModelAdapterEjercicio extends RecyclerView.Adapter<ModelAdapterEjercicio.ModelViewHolder>{

    private List<Ejercicio> items;
    private Boolean fromAlumno;

    public ModelAdapterEjercicio (List<Ejercicio> items) {
        this.items = items;
        fromAlumno = false;
    }

    public ModelAdapterEjercicio setFromAlumno() {
        this.fromAlumno = true;
        return this;
    }

    @Override
    public ModelAdapterEjercicio.ModelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ModelViewHolder(LayoutInflater.from(parent.getContext()).inflate(fromAlumno ? R.layout.recycle_ejercicio_a : R.layout.recycle_ejercicio, parent, false));
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
        private ImageView preview;
        private AppCompatImageButton deleteButton;
        private AppCompatImageButton editButton;

        public ModelViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tv_ejercicio_id);
            this.preview = (ImageView) itemView.findViewById(R.id.preview_id);
            if(!fromAlumno){
                this.deleteButton = (AppCompatImageButton) itemView.findViewById(R.id.delete_ejercicio_id);
                this.editButton = (AppCompatImageButton) itemView.findViewById(R.id.edit_ejercicio_id);
            }
        }

        public void bind(Ejercicio ejercicio) {
            byte[] decodedString = Base64.decode(ejercicio.getImagenBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            textView.setText(ejercicio.getCodigoEjercicio());
            preview.setImageBitmap(bitmap);
            if(!fromAlumno){
                this.deleteButton.setTag(ejercicio.getCodigoEjercicio());
                this.editButton.setTag(ejercicio.getCodigoEjercicio());
            }
        }
    }
}