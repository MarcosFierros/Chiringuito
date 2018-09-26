package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tecnocard.com.chiringuito.ProductViewModel;
import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.RecyclerViewAdapters.AlertAdapter;
import tecnocard.com.chiringuito.RecyclerViewAdapters.RecyclerAdapter;
import tecnocard.com.chiringuito.RecyclerViewAdapters.RecyclerItemTouchHelper;
import tecnocard.com.chiringuito.UI.MainActivity;

public class ProductosFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private ProductViewModel mProductViewModel;
    private List<Producto> list;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_productos, container, false);

        list = new ArrayList<>();
        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        mProductViewModel.getAllProducts().observe(this, productos -> {
            adapter.setProducts(productos);
            list = productos;
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(new ArrayList<>(), mProductViewModel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, this);

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        FloatingActionButton addButton = view.findViewById(R.id.fab);
        addButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LayoutInflater inflater1 = LayoutInflater.from(view.getContext());
            @SuppressLint("InflateParams") final View alertView = inflater1.inflate(R.layout.productos_alert_layout, null);


            builder.setView(alertView);
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                EditText newName = alertView.findViewById(R.id.newNameEdit);
                EditText newPrice = alertView.findViewById(R.id.newPriceEdit);
                String name = newName.getText().toString();
                String priceString = newPrice.getText().toString();
                if(!name.matches("") || !priceString.matches("")){
                    Producto newProduct = new Producto(name, Double.parseDouble(priceString));
                    adapter.add(view.getContext(), newProduct);
                }



            });
            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {

            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();  //<-- See This!

        });

        return view;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

        final Producto deletedProduct = list.get(viewHolder.getAdapterPosition());
        String name = deletedProduct.getNombre();
        int index = viewHolder.getAdapterPosition();

        System.out.println("HOLA TAROLA");
        ((RecyclerAdapter.MyViewHolder) viewHolder).changeBackgroundAt(true);

        if(direction == ItemTouchHelper.LEFT) {
            adapter.removeAt(index, this.getContext());

            Snackbar snackbar = Snackbar.make
                    (view, name + " Eliminado", Snackbar.LENGTH_LONG);
            snackbar.setAction("DESHACER", v -> {
                adapter.add(this.getContext(), deletedProduct, index);
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        } else if(direction == ItemTouchHelper.RIGHT) {

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LayoutInflater inflater = LayoutInflater.from(view.getContext());
            @SuppressLint("InflateParams") final View alertView = inflater.inflate(R.layout.productos_alert_layout, null);
            builder.setView(alertView);
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                EditText newName = alertView.findViewById(R.id.newNameEdit);
                EditText newPrice = alertView.findViewById(R.id.newPriceEdit);
                String name2 = newName.getText().toString();
                String priceString = newPrice.getText().toString();
                if(!name2.matches("") || !priceString.matches("")){
                    Producto newProduct = new Producto(name2, Double.parseDouble(priceString));
                    adapter.editAt(index, name2, Double.parseDouble(priceString), this.getContext());
                }
            });
            builder.setNegativeButton("Cancelar", (dialogInterface, i) -> {
                adapter.notifyDataSetChanged();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


    }
}
