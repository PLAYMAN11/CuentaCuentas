package com.cuentacuentas;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Adap_card_consumo_total extends RecyclerView.Adapter<Adap_card_consumo_total.targetaViewholder>{

    private final Map<String, Double> usuarios;
    private final List<String> listaClaves;

    public  Adap_card_consumo_total(Map<String, Double> usuarios){
        this.listaClaves=new ArrayList<>(usuarios.keySet());
        this.usuarios=usuarios;
    }



    @NonNull
    @Override
    public targetaViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.res_final_card,parent,false);
        return new targetaViewholder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull Adap_card_consumo_total.targetaViewholder holder, int position) {
        String nombreUsuario = listaClaves.get(position);
        Double totalUsuario = usuarios.getOrDefault(nombreUsuario, 0.0);
        holder.nombre.setText(nombreUsuario);
        holder.total.setText("$"+totalUsuario);
    }

    @Override
    public int getItemCount() {
        return usuarios.size();
    }

    static class targetaViewholder extends RecyclerView.ViewHolder{

        TextView nombre;
        TextView total;

        public targetaViewholder(View itemView){
            super(itemView);
            nombre=itemView.findViewById(R.id.nombre_res_final);
            total=itemView.findViewById(R.id.Cantidad_res_final);
        }
    }


}

