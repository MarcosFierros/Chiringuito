package tecnocard.com.chiringuito.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;

public class ReciboAdapter extends RecyclerView.Adapter<ReciboAdapter.MyViewHolder> {

    private List<Producto> compraList;
    private TextView totalValueTextView;
    private TextView saldoRTextView;
    private View view;
    private double saldo;

    public ReciboAdapter(List<Producto> compraList ,TextView totalValueTextView, TextView saldoRTextView, double saldo){
        this.compraList = compraList;
        this.totalValueTextView = totalValueTextView;
        this.saldoRTextView = saldoRTextView;
        this.saldo = saldo;
    }

    @Override
    public ReciboAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recibo_layout, parent, false);

        return new MyViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ReciboAdapter.MyViewHolder holder, int position) {

        holder.name.setText(compraList.get(position).getNombre());
        String placeholder = "$" + compraList.get(position).getPrecio();
        holder.precio.setText(placeholder);

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

            delete.setOnClickListener(view -> removeAt(getAdapterPosition()));
        }
    }

    private void removeAt(int position) {
        double oldTotal = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
        double newTotal = oldTotal - compraList.get(position).getPrecio();
        double newSaldo = saldo - newTotal;

        String placeholder = "$ " + newTotal;
        totalValueTextView.setText(placeholder);
        placeholder = "$ " + newSaldo;
        saldoRTextView.setText(placeholder);
        if(newSaldo < 0)
            saldoRTextView.setTextColor(Color.RED);
        else
            saldoRTextView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.cardview_dark_background));

        compraList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, compraList.size());
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void removeAll(){
        final int size = compraList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                compraList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
        String placeholder = "$ 0.00";
        totalValueTextView.setText(placeholder);
    }

}
