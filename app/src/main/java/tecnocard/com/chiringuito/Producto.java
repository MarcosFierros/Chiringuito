package tecnocard.com.chiringuito;

import android.widget.ImageView;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int imageSrc;

    public Producto(){
        this.id = -1;
        this.nombre = "";
        this.precio = 0.0;
        this.imageSrc = R.drawable.ic_menu_camera;
    }

    public Producto(int id, String nombre, double precio){
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imageSrc = R.drawable.ic_menu_camera;

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
}
