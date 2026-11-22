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
@Table(name = "destructores_estelares")
@PrimaryKeyJoinColumn(name = "destructor_id")
public class DestructorEstelar extends Nave {

    @Column(name = "capacidad_escudos", nullable = false)
    private Integer capacidadEscudos;

    public DestructorEstelar() {
        super();
    }

    public DestructorEstelar(String nombre, String modelo, String estadoSistema, Integer capacidadEscudos) {
        super(nombre, modelo, estadoSistema);
        this.capacidadEscudos = capacidadEscudos;
    }

    public DestructorEstelar(String nombre, String modelo, String estadoSistema, Planeta planetaOrigen, Integer capacidadEscudos) {
        super(nombre, modelo, estadoSistema, planetaOrigen);
        this.capacidadEscudos = capacidadEscudos;
    }

    public Integer getCapacidadEscudos() {
        return capacidadEscudos;
    }

    public void setCapacidadEscudos(Integer capacidadEscudos) {
        this.capacidadEscudos = capacidadEscudos;
    }

    @Override
    public String toString() {
        return "DestructorEstelar [ID=" + getId() + ", Nombre=" + getNombre() + ", Modelo=" + getModelo()
                + ", Estado=" + getEstadoSistema() + ", Escudos=" + capacidadEscudos + "]";
    }
}
