package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.concurrent.ExecutionException;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UI.MainActivity;
import tecnocard.com.chiringuito.UserViewModel;
import tecnocard.com.chiringuito.Usuario;

public class RecargasFragment extends Fragment {

    double total, saldo, recarga = 0;
    ArrayDeque<Double> valuesList = new ArrayDeque<>();
    private UserViewModel mUserViewModel;
    private Usuario usuario;

    TextView uidTextView;
    TextView saldoTextView;
    TextView recargaTextView;
    TextView newSaldoTextView;
    AlertDialog alertDialog;
    View view;
    ConstraintLayout layout;
    ConstraintSet constraintSet1;
    ConstraintSet constraintSet2;

    SettingsFragment settingsFragment;

    public RecargasFragment(){
        this.settingsFragment = null;
    }
    @SuppressLint("ValidFragment")
    public RecargasFragment(SettingsFragment settingsFragment){
        this.settingsFragment = settingsFragment;
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_recargas, container, false);
        layout = view.findViewById(R.id.layout);

        constraintSet1  = new ConstraintSet();
        constraintSet2 = new ConstraintSet();

        constraintSet1.clone(layout);
        constraintSet2.clone(view.getContext(), R.layout.fragment_recargas_2);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        saldoTextView = view.findViewById(R.id.saldo_textview);
        recargaTextView = view.findViewById(R.id.recarga_textview);
        newSaldoTextView = view.findViewById(R.id.newsaldo_textview);


        usuario = null;
        if (settingsFragment.isNFCOn()) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

            LayoutInflater inflater1 = LayoutInflater.from(view.getContext());
            @SuppressLint("InflateParams") View alertView = inflater1.inflate(R.layout.ventas_alert_wait, null);
            builder.setView(alertView);
            alertDialog = builder.create();
            alertDialog.show();
        } else {
            try {
                usuario = mUserViewModel.get("1");

                uidTextView = view.findViewById(R.id.uidTextView);

                saldo = usuario.getSaldo();
                uidTextView.setText(String.valueOf(usuario.getUid()));
                recarga = 0;
                total = saldo;
                setTextViews(saldo, recarga, total);

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }


        Button btn10 = view.findViewById(R.id.Btn10);
        Button btn20 = view.findViewById(R.id.Btn20);
        Button btn50 = view.findViewById(R.id.Btn50);
        Button btn100 = view.findViewById(R.id.Btn100);
        Button btn200 = view.findViewById(R.id.Btn200);
        Button btn500 = view.findViewById(R.id.Btn500);
        btn10.setOnClickListener(new BtnListener(10));
        btn20.setOnClickListener(new BtnListener(20));
        btn50.setOnClickListener(new BtnListener(50));
        btn100.setOnClickListener(new BtnListener(100));
        btn200.setOnClickListener(new BtnListener(200));
        btn500.setOnClickListener(new BtnListener(500));

        Button okBtn = view.findViewById(R.id.okBtn);
        Button backBtn = view.findViewById(R.id.backBtn);
        Button agregarBtn = view.findViewById(R.id.agregarBtn);

        okBtn.setOnClickListener(v -> {
            usuario.setSaldo(total);
            mUserViewModel.edit(usuario);
            saldo = usuario.getSaldo();
            total = saldo;
            recarga = 0;
            setTextViews(saldo, recarga, total);
            valuesList.clear();

            TransitionManager.beginDelayedTransition(layout);
            constraintSet1.applyTo(layout);

        });
        backBtn.setOnClickListener( v -> {
            if(!valuesList.isEmpty()) {
                recarga -= valuesList.removeLast();
                total = saldo + recarga;
                setTextViews(saldo, recarga, total);
            }
        });
        agregarBtn.setOnClickListener( v -> {
            TransitionManager.beginDelayedTransition(layout);
            constraintSet2.applyTo(layout);
        });
        return view;
    }

    public void dismissAlert(){
        try {
            String uid = MainActivity.getUID();
            Log.e("Recargas", uid);
            if(mUserViewModel.userExists(uid))
                usuario = mUserViewModel.get(uid);
            else{
                usuario = new Usuario(uid, 0);
                mUserViewModel.insert(usuario);
            }

            uidTextView = view.findViewById(R.id.uidTextView);
            saldoTextView = view.findViewById(R.id.saldo_textview);

            uidTextView.setText(String.valueOf(usuario.getUid()));

            setTextViews(usuario.getSaldo(), 0, usuario.getSaldo());

            if(alertDialog != null)
                alertDialog.dismiss();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    class BtnListener implements View.OnClickListener {

        double value;
        BtnListener(double value){
            this.value = value;
        }

        @Override
        public void onClick(View v) {
            if(usuario != null) {
                saldo = usuario.getSaldo();
                recarga += value;
                total = saldo + recarga;

                setTextViews(saldo, recarga, total);
                valuesList.offer(value);
            }

        }
    }


    void setTextViews(double saldo, double recarga, double total) {
        String placeHolder = "$ " + String.valueOf(saldo);
        saldoTextView.setText(placeHolder);
        placeHolder = "$ " + String.valueOf(recarga);
        recargaTextView.setText(placeHolder);
        placeHolder = "$ " + String.valueOf(total);
        newSaldoTextView.setText(placeHolder);
    }

}
