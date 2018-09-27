package tecnocard.com.chiringuito.RecyclerViewAdapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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
import android.widget.RelativeLayout;
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

        Producto producto = list.get(position);

        holder.image.setImageResource(producto.getImageSrc());
        holder.name.setText(producto.getNombre());
        holder.precio.setText("$" + producto.getPrecio());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;
        ImageView image;
        Context context;
        RelativeLayout viewBackground1, viewBackground2, viewForeground;

        MyViewHolder(View itemView, final Context context) {
            super(itemView);
            name = itemView.findViewById(R.id.nameTextView);
            precio = itemView.findViewById(R.id.precioTextView12);
            viewBackground1 = itemView.findViewById(R.id.view_background1);
            viewBackground2 = itemView.findViewById(R.id.view_background2);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            image = itemView.findViewById(R.id.imageProducto);
            this.context = context;
        }

        void changeBackgroundAt(boolean b){
            if(b) {
                viewBackground1.setVisibility(View.VISIBLE);
                viewBackground2.setVisibility(View.INVISIBLE);
            } else {
                viewBackground1.setVisibility(View.INVISIBLE);
                viewBackground2.setVisibility(View.VISIBLE);
            }
        }
    }

    public void removeAt(int position, Context context) {
        Producto producto = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        Toast toast = Toast.makeText(context, "Producto Eliminado",Toast.LENGTH_SHORT );
        toast.show();
        mProductViewModel.delete(producto);
    }

    public void editAt(int position, String name, double price, Context context){
        list.get(position).setNombre(name);
        list.get(position).setPrecio(price);
        notifyItemChanged(position);
        Toast toast = Toast.makeText(context, "Producto Editado",Toast.LENGTH_SHORT );
        toast.show();
        notifyDataSetChanged();
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

    public void add(Context context, Producto producto, int position){
        list.add(position, producto);
        notifyItemInserted(position);
        Toast toast = Toast.makeText(context, "Producto Agregado",Toast.LENGTH_SHORT );
        toast.show();
        mProductViewModel.insert(producto);
    }


}
