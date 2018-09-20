package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import tecnocard.com.chiringuito.UI.MainActivity;

public class ProductosFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter adapter;
    private ProductViewModel mProductViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_productos, container, false);

        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        mProductViewModel.getAllProducts().observe(this, new Observer<List<Producto>>() {
            @Override
            public void onChanged(@Nullable List<Producto> productos) {
                adapter.setProducts(productos);
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerAdapter(new ArrayList<Producto>(), mProductViewModel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));

        FloatingActionButton addButton = view.findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            adapter.add(view.getContext(), newProduct);
                        }



                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();  //<-- See This!

            }
        });

        return view;
    }
}
