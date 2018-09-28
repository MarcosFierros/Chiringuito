package tecnocard.com.chiringuito;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_table")
public class Usuario {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "user_uid")
    private String uid;

    @ColumnInfo(name = "user_saldo")
    private double saldo;

    Usuario(int id, String uid, double saldo) {
        this.id = id;
        this.uid = uid;
        this.saldo = saldo;
    }

    @Ignore
    public Usuario(String uid, double saldo) {
        this.uid = uid;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }
    public double getSaldo() {
        return saldo;
    }

    public String getUid() {
        return uid;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String toString(){
        return getUid() + " - " + getSaldo();
    }

}
