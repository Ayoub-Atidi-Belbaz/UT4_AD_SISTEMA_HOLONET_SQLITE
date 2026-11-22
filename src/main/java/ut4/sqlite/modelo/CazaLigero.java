/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut4.sqlite.modelo;

import jakarta.persistence.*;

/**
 *
 * @author Ayoub Atidi Belbaz
 */
@Entity
@Table(name = "cazas_ligeros")
@PrimaryKeyJoinColumn(name = "caza_id")
public class CazaLigero extends Nave {

    @Column(name = "potencia_fuego", nullable = false)
    private Integer potenciaFuego;

    public CazaLigero() {
        super();
    }

    public CazaLigero(String nombre, String modelo, String estadoSistema, Integer potenciaFuego) {
        super(nombre, modelo, estadoSistema);
        this.potenciaFuego = potenciaFuego;
    }

    public CazaLigero(String nombre, String modelo, String estadoSistema, Planeta planetaOrigen, Integer potenciaFuego) {
        super(nombre, modelo, estadoSistema, planetaOrigen);
        this.potenciaFuego = potenciaFuego;
    }

    public Integer getPotenciaFuego() {
        return potenciaFuego;
    }

    public void setPotenciaFuego(Integer potenciaFuego) {
        this.potenciaFuego = potenciaFuego;
    }

    @Override
    public String toString() {
        return "CazaLigero [ID=" + getId() + ", Nombre=" + getNombre() + ", Modelo=" + getModelo()
                + ", Estado=" + getEstadoSistema() + ", PotenciaFuego=" + potenciaFuego + "]";
    }
}
