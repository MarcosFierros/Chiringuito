package tecnocard.com.chiringuito.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.desfire.DESFireFactory;
import com.nxp.nfclib.desfire.IDESFireEV1;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UI.Fragments.ProductosFragment;
import tecnocard.com.chiringuito.UI.Fragments.RecargasFragment;
import tecnocard.com.chiringuito.UI.Fragments.SettingsFragment;
import tecnocard.com.chiringuito.UI.Fragments.VentasFragment;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private TextView m_textView = null;
    private String m_strKey = "a98dfaaa612de680d5153dfa80b33283";
    private NxpNfcLib m_linInstance = null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new VentasFragment()).commit();

        intializeLibrary();

    }

    @Override
    protected void onResume() {
        m_linInstance.startForeGroundDispatch();
        super.onResume();
    }

    @Override
    protected void onPause() {
        m_linInstance.stopForeGroundDispatch();
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent");
        cardLogic(intent);
        super.onNewIntent(intent);
    }

    private void cardLogic(final Intent intent) {
        CardType cardType = m_linInstance.getCardType(intent);
        Log.d(TAG, "Card type found: " + cardType.getTagName());
        if( cardType == CardType.DESFireEV1) {
            IDESFireEV1 objDESFireRFireEV1 = DESFireFactory.getInstance().getDESFire(m_linInstance.getCustomModules());
            try {
                objDESFireRFireEV1.getReader().connect();
            }catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            menuItem -> {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()){
                    case R.id.nav_ventas:
                        selectedFragment = new VentasFragment();
                        break;
                    case R.id.nav_products:
                        selectedFragment = new ProductosFragment();
                        break;
                    case R.id.nav_recargas:
                        selectedFragment = new RecargasFragment();
                        break;
                    case R.id.nav_settings:
                        selectedFragment = new SettingsFragment();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,
                            selectedFragment).commit();
                }

                return true;

            };

    private void intializeLibrary() {
        m_linInstance = NxpNfcLib.getInstance();
        m_linInstance.registerActivity(this, m_strKey);
    }

}
