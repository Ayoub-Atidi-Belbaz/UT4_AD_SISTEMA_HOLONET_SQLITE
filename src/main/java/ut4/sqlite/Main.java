/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package ut4.sqlite;

import java.util.List;
import ut4.sqlite.controlador.HoloNetControl;
import ut4.sqlite.modelo.CazaLigero;
import ut4.sqlite.modelo.DestructorEstelar;
import ut4.sqlite.modelo.Planeta;
import ut4.sqlite.util.HibernateUtil;

/**
 *
 * @author Ayoub Atidi Belbaz
 */
public class Main {

    public static void main(String[] args) {
        HoloNetControl control = new HoloNetControl();

        System.out.println("=== SISTEMA DE INTELIGENCIA TÁCTICA HOLONET (SQLITE - JOINED) ===");

        // OPERACIÓN 1: Registro polimórfico de naves
        System.out.println("\n--- OPERACIÓN 1: Alta Polimórfica (persist) ---");
        Planeta yavinIV = new Planeta();
        yavinIV.setId(2); // Yavin IV (Alianza Rebelde)

        CazaLigero cazaAWing = new CazaLigero("Caza A-Wing", "RZ-1", "Estable", yavinIV, 90);
        control.registrarNuevaNave(cazaAWing);

        Planeta coruscant = new Planeta();
        coruscant.setId(1); // Coruscant (Imperio)

        DestructorEstelar destructorDevastador = new DestructorEstelar(
                "Destructor Devastador", "Clase Vengador", "Estable", coruscant, 12000);
        control.registrarNuevaNave(destructorDevastador);

        // OPERACIÓN 3: Consulta polimórfica con HQL y JOIN FETCH
        System.out.println("\n--- OPERACIÓN 3: Consulta Polimórfica HQL (JOIN FETCH) ---");
        List<CazaLigero> cazas = control.obtenerCazasPorPotenciaMinima(80);
        if (cazas != null) {
            for (CazaLigero caza : cazas) {
                System.out.println("🛸 Caza: " + caza.getNombre()
                        + " | Modelo: " + caza.getModelo()
                        + " | Potencia: " + caza.getPotenciaFuego()
                        + " | Planeta: " + (caza.getPlanetaOrigen() != null ? caza.getPlanetaOrigen().getNombre() : "Sin asignar"));
            }
        }

        // OPERACIÓN 2: Actualización en estado Detached con merge()
        System.out.println("\n--- OPERACIÓN 2: Ciclo Detached y Sincronización (merge) ---");
        if (cazas != null && !cazas.isEmpty()) {
            CazaLigero cazaModificar = cazas.get(0);
            cazaModificar.setEstadoSistema("Mantenimiento");
            System.out.println("Modificando entidad fuera de sesión: " + cazaModificar.getNombre() + " -> " + cazaModificar.getEstadoSistema());
            control.actualizarEstadoDesconectado(cazaModificar);
        }

        // OPERACIÓN 4: Eliminación por ID respetando cascada jerárquica
        System.out.println("\n--- OPERACIÓN 4: Eliminación de Nave (find + remove) ---");
        boolean eliminada = control.darDeBajaNave(4);
        System.out.println("Resultado baja nave ID 4: " + (eliminada ? "Completada" : "No encontrada"));

        // OPERACIÓN 5: Carga masiva atómica desde CSV
        System.out.println("\n--- OPERACIÓN 5: Carga Masiva Atómica desde CSV ---");
        int totalCSV = control.importarFlotaDesdeCSV("refuerzos_borde_exterior.csv");
        System.out.println("Total de naves procesadas desde CSV: " + totalCSV);

        // Reporte final de comprobación
        System.out.println("\n--- REPORTE FINAL: Flota Operativa en HoloNet ---");
        List<CazaLigero> flotaFinal = control.obtenerCazasPorPotenciaMinima(1);
        if (flotaFinal != null) {
            for (CazaLigero c : flotaFinal) {
                System.out.println("• " + c.getNombre() + " (" + c.getModelo() + ") - Potencia: "
                        + c.getPotenciaFuego() + " - Base: " + (c.getPlanetaOrigen() != null ? c.getPlanetaOrigen().getNombre() : "Sin Base"));
            }
        }

        // Cierre de conexiones
        HibernateUtil.shutdown();
        System.out.println("\n=== SESIÓN FINALIZADA CON ÉXITO ===");
    }
}
