package tecnocard.com.chiringuito;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.imageViewHolder> {

    private static List<Producto> productlist;
    private static List<Producto> finalList;
    private static ReciboAdapter reciboAdapter;
    TextView totalValueTextView;

    public ImageAdapter(List<Producto> productlist, List<Producto> finalList, ReciboAdapter reciboAdapter, TextView totalValueTextView){
        this.productlist = productlist;
        this.finalList = finalList;
        this.reciboAdapter = reciboAdapter;
        this.totalValueTextView = totalValueTextView;
    }

    @NonNull
    @Override
    public ImageAdapter.imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        ImageAdapter.imageViewHolder myViewHolder = new ImageAdapter.imageViewHolder(view, parent.getContext());

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ImageAdapter.imageViewHolder holder, int position) {

        holder.name.setText(productlist.get(position).getNombre());
        holder.precio.setText("$" + productlist.get(position).getPrecio());
        holder.image.setImageResource(productlist.get(position).getImageSrc());
    }

    @Override
    public int getItemCount() {
        return productlist.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, precio;
        ImageView image;
        Context context;

        public imageViewHolder(View itemView, final Context context) {

            super(itemView);
            name = itemView.findViewById(R.id.nameProductoTV);
            precio = itemView.findViewById(R.id.precioProductoTV);
            image = itemView.findViewById(R.id.imageProductoTV);
            this.context = context;

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("LE ESTAS PICANDO");
                    reciboAdapter.notifyDataSetChanged();
                    Producto producto = productlist.get(getAdapterPosition());
                    finalList.add(producto);
                    double oldTotal = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
                    double newTotal = oldTotal + producto.getPrecio();
                    totalValueTextView.setText("$ " + newTotal);
                }
            });

        }

        @Override
        public void onClick(View view) {

        }
    }

    public void setProducts(List<Producto> productos){
        productlist = productos;
        notifyDataSetChanged();
    }

}
