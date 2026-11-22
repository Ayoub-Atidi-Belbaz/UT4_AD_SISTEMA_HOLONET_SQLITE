-- ============================================================================
-- SCRIPT DE INICIALIZACIÓN: SISTEMA HOLONET (SQLITE - ESTRATEGIA JOINED)
-- ============================================================================
-- INSTRUCCIONES:
--   1. Abre DB Browser for SQLite, DBeaver o el plugin SQLite de NetBeans
--   2. Crea/abre el fichero holonet.db en la RAÍZ del proyecto Maven
--   3. Ejecuta este script completo
--   4. Lanza el proyecto Java
-- ============================================================================

-- Desactivamos FK temporalmente para poder borrar las tablas sin errores de dependencia
PRAGMA foreign_keys = OFF;

-- Borramos en orden inverso: primero las tablas hijas, luego la padre
DROP TABLE IF EXISTS destructores_estelares;
DROP TABLE IF EXISTS cazas_ligeros;
DROP TABLE IF EXISTS naves;
DROP TABLE IF EXISTS planetas;

-- Reactivamos integridad referencial
PRAGMA foreign_keys = ON;

-- ============================================================================
-- CREACIÓN DE TABLAS (Estrategia JOINED)
-- ============================================================================

-- 1. Tabla de Planetas (tabla de referencia, no forma parte de la herencia)
CREATE TABLE planetas (
    planeta_id  INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre      TEXT    NOT NULL UNIQUE,
    clima       TEXT,
    afiliacion  TEXT    DEFAULT 'Neutral'
);

-- 2. Tabla PADRE de la jerarquía de naves
--    Contiene los campos COMUNES a cualquier tipo de nave.
--    Con la estrategia JOINED, esta tabla existe siempre y la PK se comparte
--    con las tablas hijas mediante FOREIGN KEY.
CREATE TABLE naves (
    nave_id           INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre            TEXT    NOT NULL,
    modelo            TEXT    NOT NULL,
    estado_sistema    TEXT    NOT NULL,     -- Valores: 'Critico' | 'Estable' | 'Mantenimiento'
    planeta_origen_id INTEGER,
    FOREIGN KEY (planeta_origen_id) REFERENCES planetas(planeta_id) ON DELETE SET NULL
    --                                                                ^ Si borramos el planeta,
    --                                                                  el campo queda en NULL
);

-- 3. Tabla HIJA 1: Cazas Ligeros
--    Solo guarda el campo específico de los cazas: potencia_fuego.
--    Su PK (caza_id) es también FK hacia naves(nave_id).
--    Cuando se borra una nave padre, se borra en cascada el registro del caza.
CREATE TABLE cazas_ligeros (
    caza_id         INTEGER PRIMARY KEY,
    potencia_fuego  INTEGER NOT NULL,
    FOREIGN KEY (caza_id) REFERENCES naves(nave_id) ON DELETE CASCADE
);

-- 4. Tabla HIJA 2: Destructores Estelares
--    Solo guarda el campo específico: capacidad_escudos.
--    Misma mecánica de PK compartida con la tabla padre.
CREATE TABLE destructores_estelares (
    destructor_id       INTEGER PRIMARY KEY,
    capacidad_escudos   INTEGER NOT NULL,
    FOREIGN KEY (destructor_id) REFERENCES naves(nave_id) ON DELETE CASCADE
);

-- ============================================================================
-- DATOS DE CONTROL (precargados para pruebas)
-- ============================================================================

-- Planetas de referencia
INSERT INTO planetas (planeta_id, nombre, clima, afiliacion) VALUES
    (1, 'Coruscant', 'Templado',   'Imperio'),
    (2, 'Yavin IV',  'Jungla',     'Alianza Rebelde'),
    (3, 'Tatooine',  'Árido',      'Neutral');

-- Registros en la tabla padre 'naves'
INSERT INTO naves (nave_id, nombre, modelo, estado_sistema, planeta_origen_id) VALUES
    (1, 'Caza X-Wing',               'T-65B',           'Estable',       2),
    (2, 'Destructor Estelar',         'Clase Imperial I','Estable',       1),
    (3, 'Superdestructor Ejecutor',   'Clase Ejecutor',  'Critico',       1),
    (4, 'Caza TIE Alfa',              'TIE/ln',          'Critico',       1),
    (5, 'Halcón Milenario',           'YT-1300',         'Mantenimiento', 3);

-- Registros hijos en 'cazas_ligeros' (IDs 1, 4 y 5 — comparten nave_id)
INSERT INTO cazas_ligeros (caza_id, potencia_fuego) VALUES
    (1, 85),
    (4, 40),
    (5, 120);

-- Registros hijos en 'destructores_estelares' (IDs 2 y 3)
INSERT INTO destructores_estelares (destructor_id, capacidad_escudos) VALUES
    (2, 9500),
    (3, 45000);
