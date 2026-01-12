BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "alerta" (
	"id_alerta"	INTEGER,
	"id_equipo"	INTEGER NOT NULL,
	"tipo"	TEXT NOT NULL CHECK("tipo" IN ('VENCIMIENTO', 'MANTENIMIENTO', 'CRITICO')),
	"titulo"	TEXT NOT NULL,
	"mensaje"	TEXT,
	"fecha_alerta"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"fecha_vencimiento"	TEXT,
	"atendida"	INTEGER DEFAULT 0,
	"fecha_atencion"	TEXT,
	"id_usuario_atencion"	INTEGER,
	PRIMARY KEY("id_alerta" AUTOINCREMENT),
	FOREIGN KEY("id_equipo") REFERENCES "equipo"("id_equipo"),
	FOREIGN KEY("id_usuario_atencion") REFERENCES "usuarios"("id_usuario")
);
CREATE TABLE IF NOT EXISTS "auditoria" (
	"id_auditoria"	INTEGER,
	"tabla_afectada"	TEXT NOT NULL,
	"id_registro"	INTEGER NOT NULL,
	"accion"	TEXT NOT NULL CHECK("accion" IN ('INSERT', 'UPDATE', 'DELETE')),
	"datos_anteriores"	TEXT,
	"datos_nuevos"	TEXT,
	"fecha"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"id_usuario"	INTEGER,
	"ip_origen"	TEXT,
	PRIMARY KEY("id_auditoria" AUTOINCREMENT),
	FOREIGN KEY("id_usuario") REFERENCES "usuarios"("id_usuario")
);
CREATE TABLE IF NOT EXISTS "calibracion" (
	"id_calibracion"	INTEGER,
	"id_equipo"	INTEGER NOT NULL,
	"fecha_calibracion"	TEXT NOT NULL,
	"periodo_meses"	INTEGER NOT NULL DEFAULT 12,
	"fecha_vencimiento"	TEXT NOT NULL,
	"resultado"	TEXT DEFAULT 'APROBADO' CHECK("resultado" IN ('APROBADO', 'RECHAZADO', 'CONDICIONAL')),
	"observaciones"	TEXT,
	"id_usuario"	INTEGER,
	"tecnico_responsable"	TEXT,
	"numero_certificado"	TEXT,
	"metodo_utilizado"	TEXT,
	"fecha_registro"	TEXT DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("id_calibracion" AUTOINCREMENT),
	FOREIGN KEY("id_equipo") REFERENCES "equipo"("id_equipo"),
	FOREIGN KEY("id_usuario") REFERENCES "usuarios"("id_usuario")
);
CREATE TABLE IF NOT EXISTS "db_version" (
	"version"	INTEGER,
	"descripcion"	TEXT,
	"fecha_actualizacion"	TEXT DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("version")
);
CREATE TABLE IF NOT EXISTS "documento" (
	"id_documento"	INTEGER,
	"tipo"	TEXT NOT NULL CHECK("tipo" IN ('CERTIFICADO', 'MANUAL', 'FOTO', 'REPORTE', 'OTRO')),
	"nombre_archivo"	TEXT NOT NULL,
	"ruta_archivo"	TEXT NOT NULL,
	"id_equipo"	INTEGER,
	"id_calibracion"	INTEGER,
	"descripcion"	TEXT,
	"fecha_subida"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"id_usuario"	INTEGER,
	PRIMARY KEY("id_documento" AUTOINCREMENT),
	FOREIGN KEY("id_calibracion") REFERENCES "calibracion"("id_calibracion"),
	FOREIGN KEY("id_equipo") REFERENCES "equipo"("id_equipo"),
	FOREIGN KEY("id_usuario") REFERENCES "usuarios"("id_usuario")
);
CREATE TABLE IF NOT EXISTS "empresa" (
	"id_empresa"	INTEGER,
	"nombre"	TEXT NOT NULL,
	"nit"	TEXT UNIQUE,
	"direccion"	TEXT,
	"telefono"	TEXT,
	"email"	TEXT,
	"logo_path"	TEXT,
	"activo"	INTEGER DEFAULT 1,
	"fecha_creacion"	TEXT DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("id_empresa" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "equipo" (
	"id_equipo"	INTEGER,
	"codigo_interno"	TEXT NOT NULL UNIQUE,
	"nombre"	TEXT NOT NULL,
	"marca"	TEXT,
	"modelo"	TEXT,
	"serie"	TEXT,
	"ubicacion"	TEXT,
	"estado"	TEXT DEFAULT 'OPERATIVO' CHECK("estado" IN ('OPERATIVO', 'MANTENIMIENTO', 'FUERA_SERVICIO', 'BAJA')),
	"id_laboratorio"	INTEGER,
	"id_empresa"	INTEGER,
	"fecha_adquisicion"	TEXT,
	"fecha_registro"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"observaciones"	TEXT,
	"activo"	INTEGER DEFAULT 1,
	PRIMARY KEY("id_equipo" AUTOINCREMENT),
	FOREIGN KEY("id_empresa") REFERENCES "empresa"("id_empresa"),
	FOREIGN KEY("id_laboratorio") REFERENCES "laboratorio"("id_laboratorio")
);
CREATE TABLE IF NOT EXISTS "laboratorio" (
	"id_laboratorio"	INTEGER,
	"nombre"	TEXT NOT NULL,
	"codigo"	TEXT UNIQUE,
	"acreditado_iso17025"	INTEGER DEFAULT 0,
	"numero_acreditacion"	TEXT,
	"direccion"	TEXT,
	"responsable"	TEXT,
	"activo"	INTEGER DEFAULT 1,
	"fecha_creacion"	TEXT DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("id_laboratorio" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "licencia" (
	"id_licencia"	INTEGER,
	"tipo_plan"	TEXT NOT NULL CHECK("tipo_plan" IN ('FREE', 'PRO', 'ENTERPRISE')),
	"fecha_inicio"	TEXT NOT NULL,
	"fecha_fin"	TEXT NOT NULL,
	"hardware_id"	TEXT NOT NULL UNIQUE,
	"firma_digital"	TEXT,
	"max_equipos"	INTEGER DEFAULT -1,
	"max_usuarios"	INTEGER DEFAULT 1,
	"funciones_habilitadas"	TEXT,
	"activa"	INTEGER DEFAULT 1,
	"fecha_creacion"	TEXT DEFAULT CURRENT_TIMESTAMP,
	PRIMARY KEY("id_licencia" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "sync_estado" (
	"id_sync"	INTEGER,
	"tabla"	TEXT NOT NULL,
	"id_registro"	INTEGER NOT NULL,
	"estado_sync"	TEXT DEFAULT 'PENDIENTE' CHECK("estado_sync" IN ('PENDIENTE', 'ENVIADO', 'ERROR')),
	"fecha_modificacion"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"fecha_sync"	TEXT,
	"intentos"	INTEGER DEFAULT 0,
	"error_mensaje"	TEXT,
	PRIMARY KEY("id_sync" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "usuarios" (
	"id_usuario"	INTEGER,
	"nombre"	TEXT NOT NULL,
	"email"	TEXT NOT NULL UNIQUE,
	"password_hash"	TEXT NOT NULL,
	"rol"	TEXT NOT NULL CHECK("rol" IN ('ADMIN', 'TECNICO', 'AUDITOR')),
	"activo"	INTEGER DEFAULT 1,
	"fecha_creacion"	TEXT DEFAULT CURRENT_TIMESTAMP,
	"fecha_ultimo_acceso"	TEXT,
	PRIMARY KEY("id_usuario" AUTOINCREMENT)
);
INSERT INTO "db_version" VALUES (1,'Modelo Enterprise inicial','2026-01-11 23:18:45');
INSERT INTO "empresa" VALUES (1,'Mi Empresa','000000000-0','Dirección no configurada',NULL,NULL,NULL,1,'2026-01-11 23:18:45');
INSERT INTO "laboratorio" VALUES (1,'Laboratorio Principal','LAB-001',0,NULL,NULL,NULL,1,'2026-01-11 23:18:45');
INSERT INTO "licencia" VALUES (1,'FREE','2026-01-11','2027-01-11','DEFAULT-HW-ID',NULL,10,1,NULL,1,'2026-01-11 23:18:45');
INSERT INTO "usuarios" VALUES (1,'Administrador','admin@calibrador.local','240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9','ADMIN',1,'2026-01-11 23:18:45',NULL);
CREATE VIEW v_equipos_proximos_vencer AS
SELECT 
    e.id_equipo,
    e.codigo_interno,
    e.nombre,
    e.marca,
    e.modelo,
    c.fecha_vencimiento,
    CAST((julianday(c.fecha_vencimiento) - julianday('now')) AS INTEGER) AS dias_restantes,
    c.resultado AS ultimo_resultado,
    l.nombre AS laboratorio
FROM equipo e
LEFT JOIN (
    SELECT id_equipo, MAX(fecha_calibracion) AS ultima_fecha
    FROM calibracion
    GROUP BY id_equipo
) uc ON e.id_equipo = uc.id_equipo
LEFT JOIN calibracion c ON e.id_equipo = c.id_equipo AND c.fecha_calibracion = uc.ultima_fecha
LEFT JOIN laboratorio l ON e.id_laboratorio = l.id_laboratorio
WHERE e.activo = 1 AND e.estado = 'OPERATIVO';
CREATE VIEW v_resumen_equipos AS
SELECT 
    e.id_equipo,
    e.codigo_interno,
    e.nombre,
    COUNT(c.id_calibracion) AS total_calibraciones,
    MAX(c.fecha_calibracion) AS ultima_calibracion,
    MIN(c.fecha_vencimiento) AS proxima_vencimiento
FROM equipo e
LEFT JOIN calibracion c ON e.id_equipo = c.id_equipo
WHERE e.activo = 1
GROUP BY e.id_equipo;
CREATE INDEX IF NOT EXISTS "idx_alerta_atendida" ON "alerta" (
	"atendida"
);
CREATE INDEX IF NOT EXISTS "idx_alerta_equipo" ON "alerta" (
	"id_equipo"
);
CREATE INDEX IF NOT EXISTS "idx_alerta_tipo" ON "alerta" (
	"tipo"
);
CREATE INDEX IF NOT EXISTS "idx_auditoria_fecha" ON "auditoria" (
	"fecha"
);
CREATE INDEX IF NOT EXISTS "idx_auditoria_tabla" ON "auditoria" (
	"tabla_afectada"
);
CREATE INDEX IF NOT EXISTS "idx_auditoria_usuario" ON "auditoria" (
	"id_usuario"
);
CREATE INDEX IF NOT EXISTS "idx_calibracion_equipo" ON "calibracion" (
	"id_equipo"
);
CREATE INDEX IF NOT EXISTS "idx_calibracion_fecha" ON "calibracion" (
	"fecha_calibracion"
);
CREATE INDEX IF NOT EXISTS "idx_calibracion_resultado" ON "calibracion" (
	"resultado"
);
CREATE INDEX IF NOT EXISTS "idx_calibracion_vencimiento" ON "calibracion" (
	"fecha_vencimiento"
);
CREATE INDEX IF NOT EXISTS "idx_documento_calibracion" ON "documento" (
	"id_calibracion"
);
CREATE INDEX IF NOT EXISTS "idx_documento_equipo" ON "documento" (
	"id_equipo"
);
CREATE INDEX IF NOT EXISTS "idx_documento_tipo" ON "documento" (
	"tipo"
);
CREATE INDEX IF NOT EXISTS "idx_equipo_activo" ON "equipo" (
	"activo"
);
CREATE INDEX IF NOT EXISTS "idx_equipo_codigo" ON "equipo" (
	"codigo_interno"
);
CREATE INDEX IF NOT EXISTS "idx_equipo_estado" ON "equipo" (
	"estado"
);
CREATE INDEX IF NOT EXISTS "idx_equipo_laboratorio" ON "equipo" (
	"id_laboratorio"
);
CREATE INDEX IF NOT EXISTS "idx_laboratorio_activo" ON "laboratorio" (
	"activo"
);
CREATE INDEX IF NOT EXISTS "idx_licencia_activa" ON "licencia" (
	"activa"
);
CREATE INDEX IF NOT EXISTS "idx_licencia_hardware" ON "licencia" (
	"hardware_id"
);
CREATE INDEX IF NOT EXISTS "idx_sync_estado" ON "sync_estado" (
	"estado_sync"
);
CREATE INDEX IF NOT EXISTS "idx_sync_tabla" ON "sync_estado" (
	"tabla"
);
CREATE INDEX IF NOT EXISTS "idx_usuarios_activo" ON "usuarios" (
	"activo"
);
CREATE INDEX IF NOT EXISTS "idx_usuarios_email" ON "usuarios" (
	"email"
);
CREATE TRIGGER trg_audit_equipo_delete
AFTER DELETE ON equipo
BEGIN
    INSERT INTO auditoria (tabla_afectada, id_registro, accion, datos_anteriores)
    VALUES ('equipo', OLD.id_equipo, 'DELETE',
            json_object('codigo', OLD.codigo_interno, 'nombre', OLD.nombre));
END;
CREATE TRIGGER trg_audit_equipo_insert
AFTER INSERT ON equipo
BEGIN
    INSERT INTO auditoria (tabla_afectada, id_registro, accion, datos_nuevos)
    VALUES ('equipo', NEW.id_equipo, 'INSERT', 
            json_object('codigo', NEW.codigo_interno, 'nombre', NEW.nombre));
END;
CREATE TRIGGER trg_audit_equipo_update
AFTER UPDATE ON equipo
BEGIN
    INSERT INTO auditoria (tabla_afectada, id_registro, accion, datos_anteriores, datos_nuevos)
    VALUES ('equipo', NEW.id_equipo, 'UPDATE',
            json_object('codigo', OLD.codigo_interno, 'nombre', OLD.nombre, 'estado', OLD.estado),
            json_object('codigo', NEW.codigo_interno, 'nombre', NEW.nombre, 'estado', NEW.estado));
END;
COMMIT;
