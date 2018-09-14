package tecnocard.com.chiringuito.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import tecnocard.com.chiringuito.R;
import tecnocard.com.chiringuito.UI.Fragments.ProductosFragment;
import tecnocard.com.chiringuito.UI.Fragments.RecargasFragment;
import tecnocard.com.chiringuito.UI.Fragments.SettingsFragment;
import tecnocard.com.chiringuito.UI.Fragments.VentasFragment;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
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

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new VentasFragment()).commit();


    }


}
