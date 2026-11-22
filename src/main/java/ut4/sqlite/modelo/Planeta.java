/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut4.sqlite.modelo;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ayoub Atidi Belbaz
 */
@Entity
@Table(name = "planetas")
public class Planeta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "planeta_id")
    private int id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "clima", length = 50)
    private String clima;

    @Column(name = "afiliacion", length = 50)
    private String afiliacion = "Neutral";

    @OneToMany(mappedBy = "planetaOrigen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Nave> listaNaves = new ArrayList<>();

    public Planeta() {
    }

    public Planeta(String nombre, String afiliacion) {
        this.nombre = nombre;
        this.afiliacion = afiliacion;
    }

    public Planeta(String nombre, String clima, String afiliacion) {
        this.nombre = nombre;
        this.clima = clima;
        this.afiliacion = afiliacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlanetaId() {
        return id;
    }

    public void setPlanetaId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClima() {
        return clima;
    }

    public void setClima(String clima) {
        this.clima = clima;
    }

    public String getAfiliacion() {
        return afiliacion;
    }

    public void setAfiliacion(String afiliacion) {
        this.afiliacion = afiliacion;
    }

    public List<Nave> getListaNaves() {
        return listaNaves;
    }

    public void setListaNaves(List<Nave> listaNaves) {
        this.listaNaves = listaNaves;
    }

    public void agregarNave(Nave nave) {
        this.listaNaves.add(nave);
        nave.setPlanetaOrigen(this);
    }

    @Override
    public String toString() {
        return "Planeta [ID=" + id + ", Nombre=" + nombre + ", Clima=" + clima + ", Afiliación=" + afiliacion + "]";
    }
}
