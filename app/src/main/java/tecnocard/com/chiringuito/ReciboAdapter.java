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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReciboAdapter extends RecyclerView.Adapter<ReciboAdapter.MyViewHolder> {

    private static List<Producto> compraList;
    private static List<Producto> productsList;
    private static TextView totalValueTextView;

    public ReciboAdapter(List<Producto> compraList, List<Producto> productsList,TextView totalValueTextView){
        this.compraList = compraList;
        this.productsList = productsList;
        this.totalValueTextView = totalValueTextView;
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

        holder.name.setText(compraList.get(position).getNombre());
        holder.precio.setText("$" + compraList.get(position).getPrecio());

    }

    @Override
    public int getItemCount() {
        return compraList.size();
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
                    removeAt(getAdapterPosition());
                }
            });


        }
    }

    public  void removeAt(int position) {
        double oldTotal = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
        double newTotal = oldTotal - compraList.get(position).getPrecio();
        totalValueTextView.setText("$ " + newTotal);
        compraList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, compraList.size());
    }

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

    public List<Producto> Collapse(){

        List<Producto> collapsedList = new ArrayList<>();
        for(Producto p: productsList){
            p.setQty(0);
            collapsedList.add(p);
        }

        for(Producto p1: compraList){
            for(Producto p2: collapsedList){
                if(p1.compareTo(p2) == 0)
                    p2.addQty();
            }
        }

        for (Iterator<Producto> iterator = collapsedList.iterator(); iterator.hasNext(); ) {
            Producto p = iterator.next();
            if(p.getQty() ==0){
                iterator.remove();
            }
        }

        return collapsedList;
    }

}
