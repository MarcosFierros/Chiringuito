package tecnocard.com.chiringuito;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ReciboAdapter extends RecyclerView.Adapter<ReciboAdapter.MyViewHolder> {

    private static List<Producto> list;

    public ReciboAdapter(List<Producto> list){
        this.list = list;
    }

    @NonNull
    @Override
    public ReciboAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recibo_layout, parent, false);
        ReciboAdapter.MyViewHolder myViewHolder = new ReciboAdapter.MyViewHolder(view, parent.getContext());

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ReciboAdapter.MyViewHolder holder, int position) {

        holder.name.setText(list.get(position).getNombre());
        holder.precio.setText("$" + list.get(position).getPrecio());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, precio;
        Button delete;
        Context context;


        public MyViewHolder(View itemView, final Context context) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTxtView);
            precio = itemView.findViewById(R.id.priceTxtView);
            delete = itemView.findViewById(R.id.deleteBtn);

            this.context = context;

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAt(getAdapterPosition(), context);
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


}
