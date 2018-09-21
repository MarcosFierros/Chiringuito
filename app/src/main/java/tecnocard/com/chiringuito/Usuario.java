package tecnocard.com.chiringuito;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user_table")
public class Usuario {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "user_saldo")
    private double saldo;

    Usuario(int uid, double saldo) {
        this.uid = uid;
        this.saldo = saldo;
    }

    public double getSaldo() {
        return saldo;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String toString(){
        return getUid() + " - " + getSaldo();
    }

}
