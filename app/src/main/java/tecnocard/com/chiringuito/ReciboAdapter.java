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
    private static TextView totalValueTextView;

    public ReciboAdapter(List<Producto> list, TextView totalValueTextView){
        this.list = list;
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
                    removeAt(getAdapterPosition());
                }
            });


        }
    }

    public  void removeAt(int position) {
        double oldTotal = Double.parseDouble(totalValueTextView.getText().toString().replace("$", ""));
        double newTotal = oldTotal - list.get(position).getPrecio();
        totalValueTextView.setText("$ " + newTotal);
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public void removeAll(){
        final int size = list.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                list.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
        totalValueTextView.setText("$ 0.00");
    }

    public String Collapse(){
        String ret = "";
        for(int i = 0; i < list.size(); i++){
            Producto p = list.get(i);
            for(int j = 0; i < list.size(); i++){
                Producto p2 = list.get(j);
            }
            if(p.added())
               p.addQty();
        }

        for(int i = 0; i < list.size(); i++){
            Producto p = list.get(i);
            if(p.added())
                p.setCollapsed(true);
            if(!p.isCollapsed())
                ret += p.toString()+ "\n";
        }

        return ret;
    }


}
