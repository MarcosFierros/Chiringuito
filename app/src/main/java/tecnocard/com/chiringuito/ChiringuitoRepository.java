package tecnocard.com.chiringuito;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ChiringuitoRepository {

    private ProductsDao mProductDao;
    private LiveData<List<Producto>> mAllProducts;

    ChiringuitoRepository(Application application){
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

    public void delete (Producto producto){
        new deleteAsyncTask(mProductDao).execute(producto);
    }

    public void update (Producto producto) {
        new updateAsyncTask(mProductDao).execute(producto);
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

    private static class deleteAsyncTask extends AsyncTask<Producto, Void, Void> {

        private ProductsDao mAsyncTaskDao;

        deleteAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Producto... productos) {
            mAsyncTaskDao.deleteProduct(productos[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Producto, Void, Void> {

        private ProductsDao mAsyncTaskDao;

        updateAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Producto... productos) {
            mAsyncTaskDao.updateProduct(productos[0]);
            return null;
        }
    }

}


