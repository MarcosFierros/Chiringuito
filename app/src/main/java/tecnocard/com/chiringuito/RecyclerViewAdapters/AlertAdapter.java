package tecnocard.com.chiringuito.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;

public class AlertAdapter extends  RecyclerView.Adapter<AlertAdapter.MyViewHolder>{

    private static List<Producto> list;

    public AlertAdapter(List<Producto> list){
        AlertAdapter.list = list;
    }

    @NonNull
    @Override
    public AlertAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_recycler_layout, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.MyViewHolder holder, int position) {

        Producto p = list.get(position);
            holder.name.setText(p.getQty() + "x " + p.getNombre());
            holder.precio.setText("$ " + (p.getQty() * p.getPrecio()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;

        MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.alertPNameTV);
            precio = itemView.findViewById(R.id.alertPPriceTV);
        }
    }




}
