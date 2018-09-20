package tecnocard.com.chiringuito.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import tecnocard.com.chiringuito.ProductViewModel;
import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static List<Producto> list;
    private ProductViewModel mProductViewModel;

    public RecyclerAdapter(List<Producto> list, ProductViewModel mProductViewModel){
        RecyclerAdapter.list = list;
        this.mProductViewModel = mProductViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);

        return new MyViewHolder(view, parent.getContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.name.setText(list.get(position).getNombre());
        holder.precio.setText("$" + list.get(position).getPrecio());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;
        ImageView edit, delete;
        Context context;


        MyViewHolder(View itemView, final Context context) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTextView);
            precio = itemView.findViewById(R.id.precioTextView);
            edit = itemView.findViewById(R.id.editImg);
            delete = itemView.findViewById(R.id.deleteImg);

            this.context = context;

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    @SuppressLint("InflateParams") final View alertView = inflater.inflate(R.layout.productos_alert_layout, null);

                    builder.setView(alertView);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText newName = alertView.findViewById(R.id.newNameEdit);
                            EditText newPrice = alertView.findViewById(R.id.newPriceEdit);
                            String name = newName.getText().toString();
                            String priceString = newPrice.getText().toString();
                            if(!name.matches("") || !priceString.matches("")){
                                Producto newProduct = new Producto(name, Double.parseDouble(priceString));
                                editAt(getAdapterPosition(), name, Double.parseDouble(priceString), context);
                            }
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(getAdapterPosition(), context);
                }
            });


        }
    }

    private void removeAt(int position, Context context) {
        Producto producto = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        Toast toast = Toast.makeText(context, "Producto Eliminado",Toast.LENGTH_SHORT );
        toast.show();
        mProductViewModel.delete(producto);
    }

    private void editAt(int position, String name, double price, Context context){
        list.get(position).setNombre(name);
        list.get(position).setPrecio(price);
        notifyItemChanged(position);
        Toast toast = Toast.makeText(context, "Producto Editado",Toast.LENGTH_SHORT );
        toast.show();

        Producto producto = list.get(position);
        mProductViewModel.edit(producto);
    }

    public void setProducts(List<Producto> productos){
        list = productos;
        notifyDataSetChanged();
    }

    public void add(Context context, Producto producto){
        list.add(producto);
        notifyItemInserted(list.size() - 1);
        Toast toast = Toast.makeText(context, "Producto Agregado",Toast.LENGTH_SHORT );
        toast.show();
        mProductViewModel.insert(producto);
    }


}
