package tecnocard.com.chiringuito;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChiringuitoRepository {

    private ProductsDao mProductDao;
    private UserDao mUserDao;
    private LiveData<List<Producto>> mAllProducts;
    private LiveData<List<Usuario>> mAllUsers;

    ChiringuitoRepository(Application application){
        ChiringuitoRoomDatabase db = ChiringuitoRoomDatabase.getDatabase(application);
        mProductDao = db.productsDao();
        mAllProducts = mProductDao.getAllProducts();
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUsers();
    }

    LiveData<List<Producto>> getmAllProducts() {
        return mAllProducts;
    }

    public void insert (Producto producto) {
        new insertProductAsyncTask(mProductDao).execute(producto);
    }

    public void delete (Producto producto){
        new deleteProductAsyncTask(mProductDao).execute(producto);
    }

    void update(Producto producto) {
        new updateProductAsyncTask(mProductDao).execute(producto);
    }

    private static class insertProductAsyncTask extends AsyncTask<Producto, Usuario, Void> {

        private ProductsDao mAsyncTaskDao;

        insertProductAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Producto... productos) {
            mAsyncTaskDao.insert(productos[0]);
            return null;
        }

    }

    private static class deleteProductAsyncTask extends AsyncTask<Producto, Void, Void> {

        private ProductsDao mAsyncTaskDao;

        deleteProductAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Producto... productos) {
            mAsyncTaskDao.deleteProduct(productos[0]);
            return null;
        }
    }

    private static class updateProductAsyncTask extends AsyncTask<Producto, Void, Void> {

        private ProductsDao mAsyncTaskDao;

        updateProductAsyncTask(ProductsDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Producto... productos) {
            mAsyncTaskDao.updateProduct(productos[0]);
            return null;
        }
    }

    LiveData<List<Usuario>> getmAllUsers() {
        return mAllUsers;
    }

    public void insert (Usuario usuario) {
        new insertUsuarioAsyncTask(mUserDao).execute(usuario);
    }

    public void delete (Usuario usuario){
        new deleteUsuarioAsyncTask(mUserDao).execute(usuario);
    }

    void update(Usuario usuario) {
        new updateUsuarioAsyncTask(mUserDao).execute(usuario);
    }

    public Usuario get (String uid) throws ExecutionException, InterruptedException {
        return new getUsuarioAsyncTask(mUserDao).execute(uid).get();
    }

    Integer exists(String uid) throws ExecutionException, InterruptedException {
        return  new usuarioExistsAsyncTask(mUserDao).execute(uid).get();
    }

    private static class insertUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertUsuarioAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Usuario... usuarios) {
            System.out.println("Entro al segundo insert de insertasynk");
            mAsyncTaskDao.insert(usuarios[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private static class deleteUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void> {

        private UserDao mAsyncTaskDao;

        deleteUsuarioAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Usuario... usuarios) {
            mAsyncTaskDao.deleteUser(usuarios[0]);
            return null;
        }
    }

    private static class updateUsuarioAsyncTask extends AsyncTask<Usuario, Void, Void> {

        private UserDao mAsyncTaskDao;

        updateUsuarioAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Usuario... usuarios) {
            mAsyncTaskDao.updateUser(usuarios[0].getSaldo(), usuarios[0].getUid());
            return null;
        }
    }

    private static class getUsuarioAsyncTask extends AsyncTask<String, Usuario, Usuario> {

        private UserDao mAsyncTaskDao;

        getUsuarioAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Usuario doInBackground(String... strings) {
            return mAsyncTaskDao.getUser(strings[0]);
        }

    }

    private static class usuarioExistsAsyncTask extends AsyncTask<String, Usuario, Integer> {

        private UserDao mAsyncTaskDao;

        usuarioExistsAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Integer doInBackground(String... strings) {
            return mAsyncTaskDao.userExists(strings[0]);
        }

    }


}


