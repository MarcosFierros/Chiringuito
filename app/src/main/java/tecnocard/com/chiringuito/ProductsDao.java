package tecnocard.com.chiringuito;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface ProductsDao {

    @Insert
    void insert(Producto producto);

    @Query("DELETE FROM products_table")
    void deleteAll();

    @Query("SELECT * FROM products_table")
    LiveData<List<Producto>> getAllProducts();

    @Delete
    void deleteProduct(Producto producto);

    @Update
    void updateProduct(Producto producto);

}
