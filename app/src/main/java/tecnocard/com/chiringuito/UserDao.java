package tecnocard.com.chiringuito;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(Usuario usuario);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("SELECT * FROM user_table")
    LiveData<List<Usuario>> getAllUsers();

    @Query("SELECT * FROM user_table WHERE user_uid == :id")
    Usuario getUser(String id);

    @Query("SELECT COUNT(*) FROM user_table")
    int getUserCount();

    @Delete
    void deleteUser(Usuario usuario);

    @Query("UPDATE user_table SET user_saldo=:saldo WHERE user_uid = :uid")
    void updateUser(double saldo, String uid);

    @Query("SELECT COUNT(*) FROM user_table WHERE user_uid == :id")
    int userExists(String id);

}
