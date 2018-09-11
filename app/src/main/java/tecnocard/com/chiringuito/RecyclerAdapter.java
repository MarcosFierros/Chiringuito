package tecnocard.com.chiringuito;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private static List<Producto> list;

    public RecyclerAdapter(List<Producto> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view, parent.getContext());

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.name.setText(list.get(position).getNombre());
        holder.precio.setText("$" + list.get(position).getPrecio());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;
        EditText nameEdit, precioEdit;
        Button edit, delete, ok;
        Context context;


        public MyViewHolder(View itemView, final Context context) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTextView);
            precio = itemView.findViewById(R.id.precioTextView);
            edit = itemView.findViewById(R.id.editBtn);
            delete = itemView.findViewById(R.id.deleteBtn);

            nameEdit = itemView.findViewById(R.id.nombreEditTxt);
            precioEdit = itemView.findViewById(R.id.precioEditTxt);
            ok = itemView.findViewById(R.id.okBtn);

            this.context = context;

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    name.setVisibility(View.INVISIBLE);
                    precio.setVisibility(View.INVISIBLE);
                    edit.setVisibility(View.INVISIBLE);
                    delete.setVisibility(View.INVISIBLE);

                    nameEdit.setVisibility(View.VISIBLE);
                    precioEdit.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.VISIBLE);

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(getAdapterPosition(), context);
                }
            });

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    name.setVisibility(View.VISIBLE);
                    precio.setVisibility(View.VISIBLE);
                    edit.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.VISIBLE);

                    nameEdit.setVisibility(View.INVISIBLE);
                    precioEdit.setVisibility(View.INVISIBLE);
                    ok.setVisibility(View.INVISIBLE);

                    if(nameEdit.getText().length() != 0 && precioEdit.getText().length() != 0){
                        String name = nameEdit.getText().toString();
                        double precio = Double.parseDouble(precioEdit.getText().toString());
                        editAt(getAdapterPosition(), name, precio, context);
                    }

                }
            });

            precioEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    precioEdit.clearFocus();
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        ok.performClick();
                        return true;
                    }
                    return false;
                }
            });

        }
    }

    public  void removeAt(int position, Context context) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        Toast toast = Toast.makeText(context, "Producto Eliminado",Toast.LENGTH_SHORT );
        toast.show();
    }

    public void editAt(int position, String name, double price, Context context){
        list.get(position).setNombre(name);
        list.get(position).setPrecio(price);
        notifyItemChanged(position);
        Toast toast = Toast.makeText(context, "Producto Editado",Toast.LENGTH_SHORT );
        toast.show();
    }

    public void setProducts(List<Producto> productos){
        list = productos;
        notifyDataSetChanged();
    }

    public static class ProductViewModel {
    }
}
