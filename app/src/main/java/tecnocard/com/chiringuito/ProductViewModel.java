package tecnocard.com.chiringuito;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {

    private ChiringuitoRepository mRepository;
    private LiveData<List<Producto>> mAllProducts;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ChiringuitoRepository(application);
        mAllProducts = mRepository.getmAllProducts();
    }

    public LiveData<List<Producto>> getAllProducts() { return mAllProducts; }

    public void insert(Producto producto) { mRepository.insert(producto); }


}


