/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ut4.sqlite.controlador;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ut4.sqlite.modelo.CazaLigero;
import ut4.sqlite.modelo.DestructorEstelar;
import ut4.sqlite.modelo.Nave;
import ut4.sqlite.modelo.Planeta;
import ut4.sqlite.util.HibernateUtil;

/**
 *
 * @author Ayoub Atidi Belbaz
 */
public class HoloNetControl {

    // Operación 1: Alta polimórfica utilizando persist()
    public void registrarNuevaNave(Nave nave) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(nave);
            tx.commit();
            System.out.println("✔ Nave registrada con éxito. ID: " + nave.getId());
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ Error al registrar nave: " + e.getMessage());
        }
    }

    // Operación 2: Actualización de entidad en estado Detached con merge()
    public void actualizarEstadoDesconectado(Nave naveModificada) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Nave naveSincronizada = (Nave) session.merge(naveModificada);
            tx.commit();
            System.out.println("✔ Estado actualizado en BD para: " + naveSincronizada.getNombre()
                    + " [Nuevo estado: " + naveSincronizada.getEstadoSistema() + "]");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ Error al sincronizar estado desconectado: " + e.getMessage());
        }
    }

    // Operación 3: Consulta polimórfica HQL con JOIN FETCH para erradicar problema N+1
    public List<CazaLigero> obtenerCazasPorPotenciaMinima(int potenciaMinima) {
        String hql = "SELECT c FROM CazaLigero c JOIN FETCH c.planetaOrigen "
                   + "WHERE c.potenciaFuego >= :minPotencia "
                   + "ORDER BY c.potenciaFuego DESC";
        List<CazaLigero> listaCazas = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<CazaLigero> query = session.createQuery(hql, CazaLigero.class);
            query.setParameter("minPotencia", potenciaMinima);
            listaCazas = query.getResultList();
            System.out.println("✔ Consulta HQL ejecutada. Cazas recuperados: " + (listaCazas != null ? listaCazas.size() : 0));
        } catch (Exception e) {
            System.err.println("❌ Error en consulta HQL: " + e.getMessage());
        }
        return listaCazas;
    }

    // Operación 4: Eliminación por ID con find() y remove() respetando cascada jerárquica
    public boolean darDeBajaNave(int idNave) {
        Transaction tx = null;
        boolean eliminado = false;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Nave nave = session.find(Nave.class, idNave);
            if (nave != null) {
                session.remove(nave);
                tx.commit();
                eliminado = true;
                System.out.println("✔ Nave eliminada con éxito. ID: " + idNave);
            } else {
                tx.rollback();
                System.out.println("❌ No se encontró ninguna nave con ID: " + idNave);
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ Error al eliminar nave: " + e.getMessage());
        }
        return eliminado;
    }

    // Operación 5: Carga masiva atómica desde fichero CSV
    public int importarFlotaDesdeCSV(String rutaRecurso) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(rutaRecurso);
        if (is == null) {
            System.err.println("❌ No se encontró el recurso CSV: " + rutaRecurso);
            return -1;
        }

        Transaction tx = null;
        int registrosImportados = 0;

        try (Session session = HibernateUtil.getSessionFactory().openSession();
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            tx = session.beginTransaction();
            String linea = br.readLine(); // Saltamos cabecera

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] datos = linea.split(";");
                String tipo = datos[0].trim();
                String nombre = datos[1].trim();
                String modelo = datos[2].trim();
                String estado = datos[3].trim();
                int idPlaneta = Integer.parseInt(datos[4].trim());
                int valorEspecifico = Integer.parseInt(datos[5].trim());

                // Obtenemos referencia de Planeta mediante proxy para optimizar la FK
                Planeta planetaRef = session.getReference(Planeta.class, idPlaneta);

                Nave nuevaNave;
                if (tipo.equalsIgnoreCase("CAZA")) {
                    nuevaNave = new CazaLigero(nombre, modelo, estado, planetaRef, valorEspecifico);
                } else {
                    nuevaNave = new DestructorEstelar(nombre, modelo, estado, planetaRef, valorEspecifico);
                }

                session.persist(nuevaNave);
                registrosImportados++;
            }

            tx.commit();
            System.out.println("✔ [HoloNet] Importación masiva completada: " + registrosImportados + " naves cargadas.");
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("❌ Fallo en la importación masiva CSV: " + e.getMessage());
            registrosImportados = -1;
        }
        return registrosImportados;
    }
}
