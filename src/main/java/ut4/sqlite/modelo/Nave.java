/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut4.sqlite.modelo;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 *
 * @author Ayoub Atidi Belbaz
 */
// Mapeamos la clase abstracta como entidad base con estrategia de herencia JOINED
@Entity
@Table(name = "naves")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Nave implements Serializable {

    // Identificador primario autoincrementado
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nave_id")
    private int id;

    // Nombre de la nave
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    // Modelo o clase de la nave
    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    // Estado operativo ('Critico', 'Estable', 'Mantenimiento')
    @Column(name = "estado_sistema", nullable = false, length = 20)
    private String estadoSistema;

    // Relación ManyToOne hacia Planeta con carga diferida
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "planeta_origen_id")
    private Planeta planetaOrigen;

    // Constructor por defecto requerido por JPA
    public Nave() {
    }

    public Nave(String nombre, String modelo, String estadoSistema) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.estadoSistema = estadoSistema;
    }

    public Nave(String nombre, String modelo, String estadoSistema, Planeta planetaOrigen) {
        this.nombre = nombre;
        this.modelo = modelo;
        this.estadoSistema = estadoSistema;
        this.planetaOrigen = planetaOrigen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNaveId() {
        return id;
    }

    public void setNaveId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getEstadoSistema() {
        return estadoSistema;
    }

    public void setEstadoSistema(String estadoSistema) {
        this.estadoSistema = estadoSistema;
    }

    public Planeta getPlanetaOrigen() {
        return planetaOrigen;
    }

    public void setPlanetaOrigen(Planeta planetaOrigen) {
        this.planetaOrigen = planetaOrigen;
    }

    @Override
    public String toString() {
        return "Nave [ID=" + id + ", Nombre=" + nombre + ", Modelo=" + modelo + ", Estado=" + estadoSistema + "]";
    }
}
