package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tecnocard.com.chiringuito.ProductViewModel;
import tecnocard.com.chiringuito.Producto;
import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.RecyclerViewAdapters.AlertAdapter;
import tecnocard.com.chiringuito.RecyclerViewAdapters.ImageAdapter;
import tecnocard.com.chiringuito.RecyclerViewAdapters.ReciboAdapter;

public class VentasFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private AlertAdapter alertAdapter;
    private ImageAdapter imageAdapter;
    private ReciboAdapter reciboAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_ventas, container, false);


        List<Producto> finalList = new ArrayList<>();
        List<Producto> productoList = new ArrayList<>();
        final TextView totalValueTextView = view.findViewById(R.id.totalValueTxtView);
        RecyclerView recyclerView = view.findViewById(R.id.imageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        layoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        reciboAdapter = new ReciboAdapter(finalList, productoList, totalValueTextView);
        imageAdapter = new ImageAdapter(productoList, finalList, reciboAdapter, totalValueTextView);
        recyclerView.setAdapter(imageAdapter);

        RecyclerView reciboRecyclerView = view.findViewById(R.id.finalListRecyclerView);
        reciboRecyclerView.setItemAnimator(new DefaultItemAnimator());
        reciboRecyclerView.setHasFixedSize(true);
        reciboRecyclerView.addItemDecoration(
                new DividerItemDecoration(view.getContext(),DividerItemDecoration.HORIZONTAL));
        layoutManager = new LinearLayoutManager(view.getContext());
        reciboRecyclerView.setLayoutManager(layoutManager);
        reciboRecyclerView.setAdapter(reciboAdapter);

        ProductViewModel productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Producto>>() {
            @Override
            public void onChanged(@Nullable List<Producto> productos) {
                imageAdapter.setProducts(productos);
            }
        });

        Button readyBtn = view.findViewById(R.id.readyBtn);
        readyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                @SuppressLint("InflateParams") View alertView = inflater.inflate(R.layout.alert_layout, null);

                TextView totalAlertTxtView = alertView.findViewById(R.id.alertTotalValueTV);
                totalAlertTxtView.setText(totalValueTextView.getText());

                RecyclerView alertRecyclerView = alertView.findViewById(R.id.alertRv);
                layoutManager = new LinearLayoutManager(view.getContext());
                alertRecyclerView.setLayoutManager(layoutManager);
                alertAdapter = new AlertAdapter(reciboAdapter.Collapse());
                alertRecyclerView.setAdapter(alertAdapter);

                builder.setView(alertView);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Hacer el cobro en sql
                        reciboAdapter.removeAll();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
