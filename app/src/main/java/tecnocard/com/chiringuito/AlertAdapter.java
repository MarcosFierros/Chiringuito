package tecnocard.com.chiringuito;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AlertAdapter extends  RecyclerView.Adapter<AlertAdapter.MyViewHolder>{

    private static List<Producto> list;
    private static TextView totalValueTextView;

    public AlertAdapter(List<Producto> list, TextView totalValueTextView){
        this.list = list;
        this.totalValueTextView = totalValueTextView;
    }

    @NonNull
    @Override
    public AlertAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alert_recycler_layout, parent, false);
        AlertAdapter.MyViewHolder myViewHolder = new AlertAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AlertAdapter.MyViewHolder holder, int position) {

        Producto p = list.get(position);
            holder.name.setText(p.getQty() + "x " + p.getNombre());
            holder.precio.setText("$ " + (p.getQty() * p.getPrecio()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;

        public MyViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.alertPNameTV);
            precio = itemView.findViewById(R.id.alertPPriceTV);
        }
    }




}
