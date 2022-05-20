package eu.tutorials.puzzlejava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {
    public AdapterDatos(ArrayList<HashMap<String, String>> listDatos) {
        this.listDatos = listDatos;
    }

    ArrayList<HashMap<String,String>>  listDatos;

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        TextView userText;
        TextView levelText;
        TextView timeText;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            userText = (TextView) itemView.findViewById(R.id.UserRecycler);
            levelText = (TextView) itemView.findViewById(R.id.LevelRecycler);
            timeText = (TextView) itemView.findViewById(R.id.TimeRecycler);
        }
        public void asignarDatos(HashMap<String,String> map_has){
            userText.setText(map_has.get("Usuario"));
            levelText.setText(map_has.get("Nivel"));
            timeText.setText(map_has.get("Puntaje"));
        }
    }
}
