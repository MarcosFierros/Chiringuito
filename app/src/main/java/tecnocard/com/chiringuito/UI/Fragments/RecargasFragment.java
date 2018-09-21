package tecnocard.com.chiringuito.UI.Fragments;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UserViewModel;
import tecnocard.com.chiringuito.Usuario;

public class RecargasFragment extends Fragment {

    private UserViewModel mUserViewModel;
    private Usuario usuario;

    TextView uidTextView;
    TextView saldoTextView;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recargas, container, false);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        usuario = null;
        try {
            usuario = mUserViewModel.get(getUid());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        uidTextView = view.findViewById(R.id.uidTextView);
        saldoTextView = view.findViewById(R.id.saldoTextView);

        uidTextView.setText(String.valueOf(usuario.getUid()));
        saldoTextView.setText("$ " + String.valueOf(usuario.getSaldo()));

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

        return view;
    }

    static Integer getUid(){
//        Cambiar esto cuando tengamos lo de nfc
        return 1;
    }


    class BtnListener implements View.OnClickListener {

        double value;
        BtnListener(double value){
            this.value = value;
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            usuario.setSaldo(usuario.getSaldo() + value);
            mUserViewModel.edit(usuario);
            saldoTextView.setText("$ " + String.valueOf(usuario.getSaldo()));
        }
    }

}
