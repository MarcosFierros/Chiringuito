package tecnocard.com.chiringuito;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Producto.class, Usuario.class}, exportSchema = false, version = 2)
public abstract class ChiringuitoRoomDatabase extends RoomDatabase {

    public abstract ProductsDao productsDao();
    public abstract UserDao userDao();
    private static ChiringuitoRoomDatabase INSTANCE;

    public static ChiringuitoRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ChiringuitoRoomDatabase.class){
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ChiringuitoRoomDatabase.class, "chiringuito_database")
                            .addCallback(sRoomDatabaseCallback).fallbackToDestructiveMigration()
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDBAsync(INSTANCE).execute();
                }

            };

    private static class PopulateDBAsync extends AsyncTask<Void, Void, Void> {

        private final ProductsDao mDao;
        private final UserDao mUDao;

        PopulateDBAsync(ChiringuitoRoomDatabase db){
            mDao = db.productsDao();
            mUDao = db.userDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mDao.deleteAll();
            Producto p = new Producto("Papas",15.0, R.drawable.papas);
            mDao.insert(p);
            p = new Producto("Tostilocos", 25.0, R.drawable.tostilocos);
            mDao.insert(p);
            p = new Producto("Frutas", 20.5, R.drawable.frutas);
            mDao.insert(p);
            p = new Producto("Verduras", 22.5, R.drawable.verduras);
            mDao.insert(p);
            p = new Producto("Conchitas", 17.5, R.drawable.conchitas);
            mDao.insert(p);

            mUDao.deleteAll();
            Usuario u = new Usuario(1, 100);
            mUDao.insert(u);

            return null;
        }
    }


}
