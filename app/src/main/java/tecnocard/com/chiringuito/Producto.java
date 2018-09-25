package tecnocard.com.chiringuito;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "products_table")
public class Producto implements Comparable<Producto>{

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "product_name")
    private String nombre;

    @ColumnInfo(name = "product_price")
    private double precio;

    @ColumnInfo(name = "product_img")
    private int imageSrc;

    private int qty;
    private boolean collapsed;

    @Ignore
    public Producto(){
        this.nombre = "";
        this.precio = 0.0;
        this.imageSrc = R.drawable.ic_menu_camera;
        this.qty = 1;
        this.collapsed = false;
    }


    public Producto(String nombre, double precio){
        this.nombre = nombre;
        this.precio = precio;
        this.imageSrc = R.drawable.ic_menu_camera;
        this.qty = 1;
        this.collapsed = false;
    }

    @Ignore
    public Producto(String nombre, double precio, int imgSrc){
        this.nombre = nombre;
        this.precio = precio;
        this.imageSrc = R.drawable.ic_menu_camera;
        this.qty = 1;
        this.collapsed = false;
    }

    public void addQty(){
        this.qty++;
    }

    @Override
    public String toString() {
        double finalPrecio = precio*qty;
        return qty + "x " + nombre +"\t\t $ "+ finalPrecio;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setImageSrc(int imageSrc) {
        this.imageSrc = imageSrc;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getImageSrc() {
        return imageSrc;
    }

    public int getQty() {
        return qty;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    @Override
    public int compareTo(@NonNull Producto producto) {
        if(this.getQty() ==  producto.getQty())
            return this.nombre.compareTo(producto.getNombre());
        else
            return this.getQty() - producto.getQty();
    }

}
