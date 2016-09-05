package pruebaap.werinc.co.com.pruebapp.configuraciones;

/**
 * Created by werinc on 31/08/16.
 */
public class Configuraciones {

    private int tiempoComprobarInternet = 2000;
    private String nombreJson = "jsonString.txt";

    public int getTiempoComprobarInternet() {
        return tiempoComprobarInternet;
    }

    public void setTiempoComprobarInternet(int tiempoComprobarInternet) {
        this.tiempoComprobarInternet = tiempoComprobarInternet;
    }

    public String getNombreJson() {
        return nombreJson;
    }

    public void setNombreJson(String nombreJson) {
        this.nombreJson = nombreJson;
    }
}
