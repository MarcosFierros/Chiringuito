package tecnocard.com.chiringuito;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.v4.widget.CircularProgressDrawable;

import java.util.List;

public class ChiringuitoRepository {

    private ProductsDao mProductDao;
    private LiveData<List<Producto>> mAllProducts;

    public ChiringuitoRepository(Application application){
        ChiringuitoRoomDatabase db = ChiringuitoRoomDatabase.getDatabase(application);
        mProductDao = db.productsDao();
        mAllProducts = mProductDao.getAllProducts();
    }

    LiveData<List<Producto>> getmAllProducts() {
        return mAllProducts;
    }

    public void insert (Producto producto) {
        new insertAsyncTask(mProductDao).execute(producto);
    }

    private static class insertAsyncTask extends AsyncTask<Producto, Void, Void> {

        private ProductsDao mAsyncTaskDao;

        insertAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Producto... productos) {
            mAsyncTaskDao.insert(productos[0]);
            return null;
        }
    }

}


