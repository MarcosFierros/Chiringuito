package tecnocard.com.chiringuito.RecyclerViewAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageViewHolder> {

    private List<Producto> productlist;
    private List<Producto> finalList;
    private ReciboAdapter reciboAdapter;
    private TextView totalValueTextView;
    private TextView saldoRValueTextView;
    private double saldo;

    public ImageAdapter(List<Producto> productlist, List<Producto> finalList, ReciboAdapter reciboAdapter, TextView totalValueTextView, TextView saldoRValueTextView, double saldo){
        this.productlist = productlist;
        this.finalList = finalList;
        this.reciboAdapter = reciboAdapter;
        this.totalValueTextView = totalValueTextView;
        this.saldoRValueTextView = saldoRValueTextView;
        this.saldo = saldo;
    }

    @NonNull
    @Override
    public ImageAdapter.imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new imageViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.imageViewHolder holder, int position) {

        holder.name.setText(productlist.get(position).getNombre());
        String placeholder = "$" + productlist.get(position).getPrecio();
        holder.precio.setText(placeholder);
        holder.image.setImageResource(productlist.get(position).getImageSrc());
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    class imageViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;
        ImageView image;
        Context context;

        imageViewHolder(View itemView, final Context context) {

            super(itemView);
            name = itemView.findViewById(R.id.nameProductoTV);
            precio = itemView.findViewById(R.id.precioProductoTV);
            image = itemView.findViewById(R.id.imageProductoTV);
            this.context = context;

            image.setOnClickListener(view -> {
                Producto producto = productlist.get(getAdapterPosition());
                finalList.add(producto);
                reciboAdapter.notifyDataSetChanged();

                double oldTotal = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
                double newTotal = oldTotal + producto.getPrecio();

                String placeholder = "$ " + newTotal;
                totalValueTextView.setText(placeholder);
                placeholder = "$ " + (saldo - newTotal);
                saldoRValueTextView.setText(placeholder);
                if(saldo - newTotal < 0)
                    saldoRValueTextView.setTextColor(Color.RED);
                else
                    saldoRValueTextView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.cardview_dark_background));
            });
        }
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public void setProducts(List<Producto> productos){
        productlist = productos;
        notifyDataSetChanged();
    }

}
