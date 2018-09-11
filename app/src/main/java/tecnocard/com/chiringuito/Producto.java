package tecnocard.com.chiringuito;

import android.support.annotation.NonNull;
import android.widget.ImageView;

public class Producto implements Comparable<Producto>{

    private int id;
    private String nombre;
    private double precio;
    private int imageSrc;
    private int qty;
    private boolean collapsed;

    public Producto(){
        this.id = -1;
        this.nombre = "";
        this.precio = 0.0;
        this.imageSrc = R.drawable.ic_menu_camera;
        this.qty = 1;
        this.collapsed = false;
    }

    public Producto(int id, String nombre, double precio){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imageSrc = R.drawable.ic_menu_camera;
        this.qty = 1;
        this.collapsed = false;
    }

    public void addQty(){
        this.qty++;
    }

    public boolean added(){
        return qty > 1;
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
