-- ============================================================================
-- SCRIPT DE LIMPIEZA Y REINICIO DE DATOS (SQLITE - ESTRATEGIA JOINED)
-- ============================================================================
-- Usar ANTES de probar la importación CSV (Operación 5).
-- Vacía todas las naves y planetas, reinicia los IDs automáticos,
-- y vuelve a insertar los 3 planetas base que necesita el CSV.
-- ============================================================================

-- 1. Desactivamos FK para borrar sin bloqueos de integridad referencial
PRAGMA foreign_keys = OFF;

-- 2. Borrado en orden jerárquico: primero tablas hijas, luego la padre
DELETE FROM destructores_estelares;
DELETE FROM cazas_ligeros;
DELETE FROM naves;
DELETE FROM planetas;

-- 3. Reinicio de los contadores AUTOINCREMENT
--    sqlite_sequence es una tabla interna de SQLite que guarda el último ID
--    usado por cada tabla con AUTOINCREMENT. Al borrar su entrada, el contador
--    vuelve a empezar desde 1.
DELETE FROM sqlite_sequence WHERE name IN ('naves', 'planetas');

-- 4. Reactivamos integridad referencial
PRAGMA foreign_keys = ON;

-- 5. Reinsertamos solo los planetas necesarios para resolver las FK del CSV
INSERT INTO planetas (planeta_id, nombre, clima, afiliacion) VALUES
    (1, 'Coruscant', 'Templado',   'Imperio'),
    (2, 'Yavin IV',  'Jungla',     'Alianza Rebelde'),
    (3, 'Tatooine',  'Árido',      'Neutral');

-- Verificación: las tablas de naves deben devolver 0
SELECT COUNT(*) AS total_naves    FROM naves;
SELECT COUNT(*) AS total_planetas FROM planetas;
