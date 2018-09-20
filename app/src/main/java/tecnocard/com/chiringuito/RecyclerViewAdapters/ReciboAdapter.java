package tecnocard.com.chiringuito.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;

public class ReciboAdapter extends RecyclerView.Adapter<ReciboAdapter.MyViewHolder> {

    private static List<Producto> compraList;
    private static List<Producto> productsList;
    @SuppressLint("StaticFieldLeak")
    private static TextView totalValueTextView;

    public ReciboAdapter(List<Producto> compraList, List<Producto> productsList,TextView totalValueTextView){
        ReciboAdapter.compraList = compraList;
        ReciboAdapter.productsList = productsList;
        ReciboAdapter.totalValueTextView = totalValueTextView;
    }

    @NonNull
    @Override
    public ReciboAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recibo_layout, parent, false);

        return new MyViewHolder(view, parent.getContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ReciboAdapter.MyViewHolder holder, int position) {

        holder.name.setText(compraList.get(position).getNombre());
        holder.precio.setText("$" + compraList.get(position).getPrecio());

    }

    @Override
    public int getItemCount() {
        return compraList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;
        Button delete;
        Context context;


        MyViewHolder(View itemView, final Context context) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTxtView);
            precio = itemView.findViewById(R.id.priceTxtView);
            delete = itemView.findViewById(R.id.deleteBtn);

            this.context = context;

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(getAdapterPosition());
                }
            });


        }
    }

    @SuppressLint("SetTextI18n")
    private void removeAt(int position) {
        double oldTotal = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
        double newTotal = oldTotal - compraList.get(position).getPrecio();
        totalValueTextView.setText("$ " + newTotal);
        compraList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, compraList.size());
    }

    @SuppressLint("SetTextI18n")
    public void removeAll(){
        final int size = compraList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                compraList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
        totalValueTextView.setText("$ 0.00");
    }



}
