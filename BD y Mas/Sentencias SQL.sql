-- *********************************************
-- IMPLEMENTACIÓN DE LA BASE DE DATOS

-- *************************************************************
-- 1. Authentication - Creación de Tablas, Secuencias y Triggers
-- *************************************************************

-- Creamos el paquete Authentication
CREATE OR REPLACE PACKAGE test.authentication_pkg AS
  -- Procedimientos y funciones
END authentication_pkg;
/

-- Crear Tabla Users
CREATE TABLE test.users (
    id NUMBER PRIMARY KEY,
    username VARCHAR2(255) NOT NULL UNIQUE,
    firstname VARCHAR2(25) NOT NULL,
    lastname VARCHAR2(25) NOT NULL,
    country VARCHAR2(25) NOT NULL,
    password VARCHAR2(255) NOT NULL,
    role VARCHAR2(25) NOT NULL
);

-- Crear Secuencia para Users
CREATE SEQUENCE test.user_seq START WITH 1 INCREMENT BY 1;

-- Crear Trigger para Users
CREATE OR REPLACE TRIGGER test.trg_user_pk
BEFORE INSERT ON test.users
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT test.user_seq.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/


-- **************************************************
-- Authentication - Crear Procedimientos y Funciones
-- **************************************************

-- Especificación del Paquete Authentication
CREATE OR REPLACE PACKAGE test.authentication_pkg AS
  PROCEDURE register_user (p_username IN VARCHAR2, p_firstname IN VARCHAR2, p_lastname IN VARCHAR2, p_country IN VARCHAR2, p_password IN VARCHAR2, p_role IN VARCHAR2);
  FUNCTION login_user (p_username IN VARCHAR2, p_password IN VARCHAR2) RETURN test.users%ROWTYPE;
END authentication_pkg;
/

-- Cuerpo del Paquete Authentication
CREATE OR REPLACE PACKAGE BODY test.authentication_pkg AS

  -- Procedimiento para Registrar Usuario
  PROCEDURE register_user (p_username IN VARCHAR2, p_firstname IN VARCHAR2, p_lastname IN VARCHAR2, p_country IN VARCHAR2, p_password IN VARCHAR2, p_role IN VARCHAR2) AS
  BEGIN
    INSERT INTO test.users (username, firstname, lastname, country, password, role)
    VALUES (p_username, p_firstname, p_lastname, p_country, p_password, p_role);
  END register_user;

  -- Función para Iniciar Sesión de Usuario
  FUNCTION login_user (p_username IN VARCHAR2, p_password IN VARCHAR2) RETURN test.users%ROWTYPE AS
    v_user test.users%ROWTYPE;
    v_result test.users%ROWTYPE;
  BEGIN
    SELECT username, firstname, lastname, country, role INTO v_result.username, v_result.firstname, v_result.lastname, v_result.country, v_result.role
    FROM test.users
    WHERE username = p_username AND password = p_password;
    RETURN v_result;
  EXCEPTION
    WHEN NO_DATA_FOUND THEN
      RETURN NULL;
  END login_user;

END authentication_pkg;
/

-- Insertar Datos Semilla para Users
BEGIN
  test.authentication_pkg.register_user(p_username => 'admin@example.com', p_firstname => 'Usuario', p_lastname => 'Admin', p_country => 'Ciudad 1', p_password => '$2a$10$8dfAQ/s76vbc4e.g4KEqp.0Td5YKfGtCEveJ6DqE9F/207OwAW5tm', p_role => 'Administrador');

  test.authentication_pkg.register_user(p_username => 'auxiliar@example.com', p_firstname => 'Usuario', p_lastname => 'Registro', p_country => 'Ciudad 2', p_password => '$2a$10$5GOIRFfv9FhBN5mhFqJqfOwRq4J6b6jION4yfEOwZG5QkRv6T7//.', p_role => 'Auxiliar');
END;
/



-- ************************************************************
-- 2. Departamentos - Creación de Tablas, Secuencias y Triggers 
-- ************************************************************

-- Crear Tabla Departments
CREATE TABLE test.departments (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(255) NOT NULL
);

-- Crear Secuencia para Departments
CREATE SEQUENCE test.department_seq START WITH 1 INCREMENT BY 1;

-- Crear Trigger para Departments
CREATE OR REPLACE TRIGGER test.trg_department_pk
BEFORE INSERT ON test.departments
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT test.department_seq.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

-- Crear Índice para la columna name en la tabla departments
CREATE INDEX test.idx_departments_name ON test.departments (name);



-- **********************************************************
-- 3. Municipios - Creación de Tablas, Secuencias y Triggers
-- **********************************************************

-- Crear Tabla Municipalities
CREATE TABLE test.municipalities (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(255) NOT NULL,
    department_id NUMBER NOT NULL,
    CONSTRAINT fk_department
        FOREIGN KEY (department_id)
        REFERENCES test.departments(id)
);

-- Crear Secuencia para Municipalities
CREATE SEQUENCE test.municipality_seq START WITH 1 INCREMENT BY 1;

-- Crear Trigger para Municipalities
CREATE OR REPLACE TRIGGER test.trg_municipality_pk
BEFORE INSERT ON test.municipalities
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT test.municipality_seq.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

-- Crear Índice para la columna name en la tabla municipalities
CREATE INDEX test.idx_municipalities_name ON test.municipalities (name);

-- Crear Índice para la columna department_id en la tabla municipalities
CREATE INDEX test.idx_municipalities_dpto_id ON test.municipalities (department_id);



-- *******************************************
-- 4. Datos semilla Depatamentos y Municipios
-- *******************************************

-- Insertar datos de departamentos
INSERT INTO test.departments (id, name) VALUES (1, 'Cundinamarca');
INSERT INTO test.departments (id, name) VALUES (2, 'Antioquia');
INSERT INTO test.departments (id, name) VALUES (3, 'Valle del Cauca');
INSERT INTO test.departments (id, name) VALUES (4, 'Atlántico');
INSERT INTO test.departments (id, name) VALUES (5, 'Bolívar');
INSERT INTO test.departments (id, name) VALUES (6, 'Santander');
INSERT INTO test.departments (id, name) VALUES (7, 'Caldas');
INSERT INTO test.departments (id, name) VALUES (8, 'Caquetá');
INSERT INTO test.departments (id, name) VALUES (9, 'Nariño');
INSERT INTO test.departments (id, name) VALUES (10, 'Cauca');

-- Insertar datos de municipios
INSERT INTO test.municipalities (id, name, department_id) VALUES (1, 'Bogotá', 1);
INSERT INTO test.municipalities (id, name, department_id) VALUES (2, 'Medellín', 2);
INSERT INTO test.municipalities (id, name, department_id) VALUES (3, 'Cali', 3);
INSERT INTO test.municipalities (id, name, department_id) VALUES (4, 'Barranquilla', 4);
INSERT INTO test.municipalities (id, name, department_id) VALUES (5, 'Cartagena', 5);
INSERT INTO test.municipalities (id, name, department_id) VALUES (6, 'Bucaramanga', 6);
INSERT INTO test.municipalities (id, name, department_id) VALUES (7, 'Manizales', 7);
INSERT INTO test.municipalities (id, name, department_id) VALUES (8, 'Florencia', 8);
INSERT INTO test.municipalities (id, name, department_id) VALUES (9, 'Pasto', 9);
INSERT INTO test.municipalities (id, name, department_id) VALUES (10, 'Popayán', 10);
INSERT INTO test.municipalities (id, name, department_id) VALUES (11, 'Soacha', 1);
INSERT INTO test.municipalities (id, name, department_id) VALUES (12, 'Bello', 2);
INSERT INTO test.municipalities (id, name, department_id) VALUES (13, 'Palmira', 3);
INSERT INTO test.municipalities (id, name, department_id) VALUES (14, 'Soledad', 4);
INSERT INTO test.municipalities (id, name, department_id) VALUES (15, 'Magangué', 5);
INSERT INTO test.municipalities (id, name, department_id) VALUES (16, 'Vélez', 6);
INSERT INTO test.municipalities (id, name, department_id) VALUES (17, 'La Dorada', 7);
INSERT INTO test.municipalities (id, name, department_id) VALUES (18, 'San Vicente', 8);
INSERT INTO test.municipalities (id, name, department_id) VALUES (19, 'Ipiales', 9);
INSERT INTO test.municipalities (id, name, department_id) VALUES (20, 'Caloto', 10);




-- ***********************************************************
-- 5. Comerciales - Creación de Tablas, Secuencias y Triggers
-- ***********************************************************

-- Crear Tabla Businessman
CREATE TABLE test.businessman (
    businessman_id NUMBER PRIMARY KEY,
    nombre_razon_social VARCHAR2(255),
    department_id NUMBER NOT NULL,
    municipalitie_id NUMBER NOT NULL,
    telefono VARCHAR2(20),
    correo_electronico VARCHAR2(255),
    fecha_registro DATE,
    estado VARCHAR2(20),
    fecha_actualizacion DATE,
    usuario VARCHAR2(50),
    CONSTRAINT fk_businessman_department FOREIGN KEY (department_id) REFERENCES test.departments(id),
    CONSTRAINT fk_businessman_municipalitie FOREIGN KEY (municipalitie_id) REFERENCES test.municipalities(id)
);

-- Crear Secuencia para Businessman
CREATE SEQUENCE test.businessman_seq
    START WITH 1
    INCREMENT BY 1;
    
-- Crear Trigger para Businessman
CREATE OR REPLACE TRIGGER test.trg_businessman_pk
BEFORE INSERT ON test.businessman
FOR EACH ROW
BEGIN
    IF :NEW.businessman_id IS NULL THEN
        SELECT test.businessman_seq.NEXTVAL INTO :NEW.businessman_id FROM dual;
    END IF;
END;
/

-- Crear Trigger de Auditoría para Businessman
CREATE OR REPLACE TRIGGER test.trg_businessman_audit
BEFORE INSERT OR UPDATE ON test.businessman
FOR EACH ROW
BEGIN
    :NEW.fecha_actualizacion := SYSDATE;
    :NEW.usuario := USER;
END;
/

-- Índices para la Tabla Businessman (Comerciante)
CREATE INDEX test.idx_busin_nombre ON test.businessman(nombre_razon_social);
CREATE INDEX test.idx_busin_depto ON test.businessman(department_id);
CREATE INDEX test.idx_busin_muni ON test.businessman(municipalitie_id);




-- ***************************************************************
-- 6. Establecimientos - Creación de Tablas, Secuencias y Triggers
-- ***************************************************************

CREATE TABLE test.establishment (
    establishment_id NUMBER PRIMARY KEY,
    businessman_id NUMBER,
    name VARCHAR2(255),
    revenue NUMBER(12,2),
    number_of_employees NUMBER,
    update_date DATE,
    created_by VARCHAR2(50),
    CONSTRAINT fk_estab_businessman
        FOREIGN KEY (businessman_id)
        REFERENCES test.businessman(businessman_id)
);

-- Crear secuencia para establishment
CREATE SEQUENCE test.establishment_seq START WITH 1 INCREMENT BY 1;

-- Crear trigger para establishment
CREATE OR REPLACE TRIGGER test.trg_establishment_pk
BEFORE INSERT ON test.establishment
FOR EACH ROW
BEGIN
    IF :NEW.establishment_id IS NULL THEN
        SELECT test.establishment_seq.NEXTVAL INTO :NEW.establishment_id FROM dual;
    END IF;
END;

-- Crear Trigger de Auditoría para establishment
CREATE OR REPLACE TRIGGER test.trg_establishment_audit
BEFORE INSERT OR UPDATE ON test.establishment
FOR EACH ROW
BEGIN
    :NEW.update_date := SYSDATE;
    :NEW.created_by := USER;
END;
/

-- Crear índices para la tabla establishment
CREATE INDEX test.idx_estab_name ON test.establishment(name);
CREATE INDEX test.idx_estab_busin_id ON test.establishment(businessman_id);



-- *************************************************
-- 7. Comerciales - Crear Procedimientos y Funciones
-- *************************************************

CREATE OR REPLACE PACKAGE test.businessman_pkg IS
    -- Función para consultar businessman por ID
    FUNCTION get_businessman_by_id(p_businessman_id NUMBER) RETURN SYS_REFCURSOR;

    -- Función para consultar businessman con filtros y paginación
    TYPE ref_cursor IS REF CURSOR;
    FUNCTION get_businessman(p_name VARCHAR2, p_municipalitie_id NUMBER, p_registration_date DATE,
                             p_status VARCHAR2, p_page NUMBER, p_page_size NUMBER,
                             p_total_records OUT NUMBER) RETURN ref_cursor;

    -- Procedimiento para insertar un nuevo businessman
    PROCEDURE insert_businessman(
        p_name VARCHAR2,
        p_department_id NUMBER,
        p_municipalitie_id NUMBER,
        p_phone VARCHAR2,
        p_email VARCHAR2,
        p_registration_date DATE,
        p_status VARCHAR2,
        p_user VARCHAR2,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    );

    -- Procedimiento para actualizar un businessman existente
    PROCEDURE update_businessman(
        p_businessman_id NUMBER,
        p_name VARCHAR2,
        p_department_id NUMBER,
        p_municipalitie_id NUMBER,
        p_phone VARCHAR2,
        p_email VARCHAR2,
        p_registration_date DATE,
        p_status VARCHAR2,
        p_user VARCHAR2,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    );

    -- Procedimiento para actualizar el estado del businessman
    PROCEDURE update_businessman_status(
        p_businessman_id NUMBER,
        p_status VARCHAR2,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    );

    -- Procedimiento para eliminar un businessman
    PROCEDURE delete_businessman(
        p_businessman_id NUMBER,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    );

    -- Función para validar el formato del correo electrónico
    FUNCTION validate_email(p_email VARCHAR2) RETURN BOOLEAN;

    -- Función para obtener businessman registrados y activos
    FUNCTION get_active_businessman RETURN SYS_REFCURSOR;

    -- Procedimientos para insertar datos semilla
    PROCEDURE seed_businessman_data;
END businessman_pkg;
/


-- *****************************************************
-- 7.1 Implementación Cuerpo del Paquete businessman_pkg
-- *****************************************************

-- Funciones y Procedimientos de CRUD
CREATE OR REPLACE PACKAGE BODY test.businessman_pkg IS

    -- Función para consultar businessman por ID
    FUNCTION get_businessman_by_id(p_businessman_id NUMBER) RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT c.businessman_id, c.nombre_razon_social, d.id AS department_id, d.name AS departamento,
                   d.id AS municipalitie_id, m.name AS municipio, c.telefono, c.correo_electronico,
                   c.fecha_registro, c.estado, NVL(SUM(e.revenue), 0) AS total_activos,
                   NVL(SUM(e.number_of_employees), 0) AS cantidad_empleados
            FROM test.businessman c
            LEFT JOIN test.establishment e ON c.businessman_id = e.businessman_id
            LEFT JOIN test.departments d ON c.department_id = d.id
            LEFT JOIN test.municipalities m ON c.municipalitie_id = m.id
            WHERE c.businessman_id = p_businessman_id
            GROUP BY c.businessman_id, c.nombre_razon_social, d.id, d.name, m.id, m.name,
                     c.telefono, c.correo_electronico, c.fecha_registro, c.estado;
        RETURN v_cursor;
    END get_businessman_by_id;

    -- Función para consultar businessman con filtros y paginación
    FUNCTION get_businessman(p_name VARCHAR2, p_municipalitie_id NUMBER, p_registration_date DATE,
                             p_status VARCHAR2, p_page NUMBER, p_page_size NUMBER,
                             p_total_records OUT NUMBER) RETURN ref_cursor IS
        v_cursor ref_cursor;
        v_offset NUMBER := (p_page - 1) * p_page_size;
    BEGIN
        OPEN v_cursor FOR
            SELECT businessman_id, nombre_razon_social, department_id, departamento, municipalitie_id,
                   municipio, telefono, correo_electronico, fecha_registro, estado, total_activos,
                   cantidad_empleados, cantidad_establecimientos, rnum, total_records
            FROM (
                SELECT c.businessman_id, c.nombre_razon_social, d.id AS department_id, d.name AS departamento,
                       m.id AS municipalitie_id, m.name AS municipio, c.telefono, c.correo_electronico,
                       c.fecha_registro, c.estado, NVL(SUM(e.revenue), 0) AS total_activos,
                       NVL(SUM(e.number_of_employees), 0) AS cantidad_empleados,
                       COUNT(e.businessman_id) AS cantidad_establecimientos,
                       ROW_NUMBER() OVER (ORDER BY c.businessman_id) AS rnum,
                       COUNT(*) OVER () AS total_records
                FROM test.businessman c
                LEFT JOIN test.establishment e ON c.businessman_id = e.businessman_id
                LEFT JOIN test.departments d ON c.department_id = d.id
                LEFT JOIN test.municipalities m ON c.municipalitie_id = m.id
                WHERE (p_name IS NULL OR UPPER(c.nombre_razon_social) LIKE '%' || UPPER(p_name) || '%')
                   AND (p_municipalitie_id IS NULL OR c.municipalitie_id = p_municipalitie_id)
                   AND (p_registration_date IS NULL OR c.fecha_registro = p_registration_date)
                   AND (p_status IS NULL OR UPPER(c.estado) LIKE '%' || UPPER(p_status) || '%')
                GROUP BY c.businessman_id, c.nombre_razon_social, d.id, d.name, m.id, m.name,
                         c.telefono, c.correo_electronico, c.fecha_registro, c.estado
            )
            WHERE rnum > v_offset AND rnum <= v_offset + p_page_size;
    
        RETURN v_cursor;
    END get_businessman;

    -- Procedimiento para insertar un nuevo businessman
    PROCEDURE insert_businessman(p_name VARCHAR2, p_department_id NUMBER, p_municipalitie_id NUMBER,
                                 p_phone VARCHAR2, p_email VARCHAR2, p_registration_date DATE,
                                 p_status VARCHAR2, p_user VARCHAR2, p_error_code OUT NUMBER,
                                 p_error_message OUT VARCHAR2) IS
    BEGIN
        INSERT INTO test.businessman (businessman_id, nombre_razon_social, department_id, municipalitie_id,
                                      telefono, correo_electronico, fecha_registro, estado, usuario)
        VALUES (test.businessman_seq.NEXTVAL, p_name, p_department_id, p_municipalitie_id, p_phone,
                p_email, p_registration_date, p_status, p_user);
        COMMIT;
        p_error_code := 0;
        p_error_message := 'Success';
    EXCEPTION
        WHEN OTHERS THEN
            p_error_code := SQLCODE;
            p_error_message := SQLERRM;
    END insert_businessman;

    -- Procedimiento para actualizar un businessman existente
    PROCEDURE update_businessman(p_businessman_id NUMBER, p_name VARCHAR2, p_department_id NUMBER,
                                 p_municipalitie_id NUMBER, p_phone VARCHAR2, p_email VARCHAR2,
                                 p_registration_date DATE, p_status VARCHAR2, p_user VARCHAR2,
                                 p_error_code OUT NUMBER, p_error_message OUT VARCHAR2) IS
    BEGIN
        UPDATE test.businessman
        SET nombre_razon_social = p_name, department_id = p_department_id, municipalitie_id = p_municipalitie_id,
            telefono = p_phone, correo_electronico = p_email, fecha_registro = p_registration_date,
            estado = p_status, usuario = p_user, fecha_actualizacion = SYSDATE
        WHERE businessman_id = p_businessman_id;
        COMMIT;
        p_error_code := 0;
        p_error_message := 'Success';
    EXCEPTION
        WHEN OTHERS THEN
            p_error_code := SQLCODE;
            p_error_message := SQLERRM;
    END update_businessman;

    -- Procedimiento para actualizar el estado del businessman
    PROCEDURE update_businessman_status(p_businessman_id NUMBER, p_status VARCHAR2,
                                        p_error_code OUT NUMBER, p_error_message OUT VARCHAR2) IS
    BEGIN
        UPDATE test.businessman
        SET estado = p_status, fecha_actualizacion = SYSDATE
        WHERE businessman_id = p_businessman_id;
        COMMIT;
        p_error_code := 0;
        p_error_message := 'Success';
    EXCEPTION
        WHEN OTHERS THEN
            p_error_code := SQLCODE;
            p_error_message := SQLERRM;
    END update_businessman_status;

    -- Procedimiento para eliminar un businessman
    PROCEDURE delete_businessman(p_businessman_id NUMBER, p_error_code OUT NUMBER,
                                 p_error_message OUT VARCHAR2) IS
    BEGIN
        DELETE FROM test.businessman WHERE businessman_id = p_businessman_id;
        COMMIT;
        p_error_code := 0;
        p_error_message := 'Success';
    EXCEPTION
        WHEN OTHERS THEN
            p_error_code := SQLCODE;
            p_error_message := SQLERRM;
    END delete_businessman;

    -- Función para validar el formato del correo electrónico
    FUNCTION validate_email(p_email VARCHAR2) RETURN BOOLEAN IS
    BEGIN
        RETURN REGEXP_LIKE(p_email, '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,4}$');
    END validate_email;

    -- Función para obtener businessman registrados y activos
    FUNCTION get_active_businessman RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
            SELECT c.businessman_id, c.nombre_razon_social, d.name AS departamento, m.name AS municipio,
                   c.telefono, c.correo_electronico, c.fecha_registro, c.estado, NVL(SUM(e.revenue), 0) AS total_activos,
                   NVL(SUM(e.number_of_employees), 0) AS cantidad_empleados
            FROM test.businessman c
            LEFT JOIN test.establishment e ON c.businessman_id = e.businessman_id
            LEFT JOIN test.departments d ON c.department_id = d.id
            LEFT JOIN test.municipalities m ON c.municipalitie_id = m.id
            WHERE c.estado = 'Activo'
            GROUP BY c.businessman_id, c.nombre_razon_social, d.name, m.name, c.telefono,
                     c.correo_electronico, c.fecha_registro, c.estado;
        RETURN v_cursor;
    END get_active_businessman;

    -- Procedimiento para insertar datos semilla en businessman
    PROCEDURE seed_businessman_data IS
    BEGIN
        INSERT INTO test.businessman (businessman_id, nombre_razon_social, department_id, municipalitie_id, telefono, correo_electronico, fecha_registro, estado, fecha_actualizacion, usuario) 
        VALUES (test.businessman_seq.NEXTVAL, 'Comerciante 1', 1, 1, '555-0101', 'comerciante1@example.com', SYSDATE, 'Activo', SYSDATE, USER);
    
        INSERT INTO test.businessman (businessman_id, nombre_razon_social, department_id, municipalitie_id, telefono, correo_electronico, fecha_registro, estado, fecha_actualizacion, usuario ) 
        VALUES (test.businessman_seq.NEXTVAL, 'Comerciante 2', 2, 2, '555-0202', 'comerciante2@example.com', SYSDATE, 'Activo', SYSDATE, USER);
    
        INSERT INTO test.businessman (businessman_id, nombre_razon_social, department_id, municipalitie_id, telefono, correo_electronico, fecha_registro, estado, fecha_actualizacion, usuario) 
        VALUES (test.businessman_seq.NEXTVAL, 'Comerciante 3', 3, 3, '555-0303', 'comerciante3@example.com', SYSDATE, 'Activo', SYSDATE, USER );
    END seed_businessman_data;

END businessman_pkg;
/


-- ********************************************
-- 7.2 Comerciales - Ejecución de Datos Semilla
-- ********************************************

-- Ejecutar insertar_businessman
BEGIN
    businessman_pkg.seed_businessman_data;
END;
/




-- *******************************************************
-- 8. Establecimientos - Crear Procedimientos y Funciones
-- *******************************************************

CREATE OR REPLACE PACKAGE test.establishment_pkg IS
    -- Función para consultar establecimientos por businessman
    FUNCTION get_estab_by_businessman(p_businessman_id NUMBER) RETURN SYS_REFCURSOR;

    -- Procedimiento para insertar un nuevo establecimiento
    PROCEDURE insert_establishment(p_businessman_id NUMBER, p_name VARCHAR2, p_revenue NUMBER, p_number_of_employees NUMBER, p_created_by VARCHAR2, p_error_code OUT NUMBER, p_error_message OUT VARCHAR2);

    -- Procedimiento para actualizar un establecimiento existente
    PROCEDURE update_establishment(p_establishment_id NUMBER, p_name VARCHAR2, p_revenue NUMBER, p_number_of_employees NUMBER, p_created_by VARCHAR2, p_error_code OUT NUMBER, p_error_message OUT VARCHAR2);

    -- Procedimiento para eliminar un establecimiento
    PROCEDURE delete_establishment(p_establishment_id NUMBER, p_error_code OUT NUMBER, p_error_message OUT VARCHAR2);
    
    -- Procedimientos para insertar datos semilla
    PROCEDURE seed_establishments;
END establishment_pkg;



-- ***************************************************
-- 8.1 Implementación del Paquete establishment_pkg
-- ***************************************************

-- Funciones y Procedimientos de CRUD
CREATE OR REPLACE PACKAGE BODY test.establishment_pkg IS

    -- Función para consultar establecimientos por businessman
    FUNCTION get_estab_by_businessman(p_businessman_id NUMBER) RETURN SYS_REFCURSOR IS
        v_cursor SYS_REFCURSOR;
    BEGIN
        OPEN v_cursor FOR
        SELECT 
            e.establishment_id,
            e.name,
            e.revenue,
            e.number_of_employees,
            e.update_date,
            e.created_by
        FROM test.establishment e
        JOIN test.businessman b ON e.businessman_id = b.businessman_id
        WHERE e.businessman_id = p_businessman_id;
        RETURN v_cursor;
    END get_estab_by_businessman;

    -- Procedimiento para insertar un nuevo establecimiento
    PROCEDURE insert_establishment(
        p_businessman_id NUMBER,
        p_name VARCHAR2,
        p_revenue NUMBER,
        p_number_of_employees NUMBER,
        p_created_by VARCHAR2,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    ) IS
    BEGIN
        BEGIN
            -- Validaciones de parámetros de entrada
            IF p_businessman_id IS NULL OR p_name IS NULL OR p_revenue IS NULL OR p_number_of_employees IS NULL THEN
                RAISE_APPLICATION_ERROR(-20001, 'Todos los campos son obligatorios');
            END IF;
    
            INSERT INTO test.establishment (businessman_id, name, revenue, number_of_employees, update_date, created_by)
            VALUES (p_businessman_id, p_name, p_revenue, p_number_of_employees, SYSDATE, p_created_by);
            p_error_code := 0;
            p_error_message := 'Inserción exitosa';
        EXCEPTION
            WHEN OTHERS THEN
                p_error_code := SQLCODE;
                p_error_message := SQLERRM;
        END;
    END insert_establishment;

    -- Procedimiento para actualizar un establecimiento existente
    PROCEDURE update_establishment(
        p_establishment_id NUMBER,
        p_name VARCHAR2,
        p_revenue NUMBER,
        p_number_of_employees NUMBER,
        p_created_by VARCHAR2,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    ) IS
    BEGIN
        BEGIN
            -- Validaciones de parámetros de entrada
            IF p_establishment_id IS NULL OR p_name IS NULL OR p_revenue IS NULL OR p_number_of_employees IS NULL THEN
                RAISE_APPLICATION_ERROR(-20001, 'Todos los campos son obligatorios');
            END IF;
    
            UPDATE test.establishment
            SET name = p_name,
                revenue = p_revenue,
                number_of_employees = p_number_of_employees,
                update_date = SYSDATE,
                created_by = p_created_by
            WHERE establishment_id = p_establishment_id;
            p_error_code := 0;
            p_error_message := 'Actualización exitosa';
        EXCEPTION
            WHEN OTHERS THEN
                p_error_code := SQLCODE;
                p_error_message := SQLERRM;
        END;
    END update_establishment;

    -- Procedimiento para eliminar un establecimiento
    PROCEDURE delete_establishment(
        p_establishment_id NUMBER,
        p_error_code OUT NUMBER,
        p_error_message OUT VARCHAR2
    ) IS
    BEGIN
        BEGIN
            DELETE FROM test.establishment WHERE establishment_id = p_establishment_id;
            IF SQL%ROWCOUNT = 0 THEN
                p_error_code := -1;
                p_error_message := 'No se encontró el establecimiento con ID ' || p_establishment_id;
            ELSE
                p_error_code := 0;
                p_error_message := 'Eliminación exitosa';
            END IF;
        EXCEPTION
            WHEN OTHERS THEN
                p_error_code := SQLCODE;
                p_error_message := SQLERRM;
        END;
    END delete_establishment;
    
    -- Procedimiento para insertar datos semilla en establishment
    PROCEDURE seed_establishments IS
    BEGIN
        INSERT INTO test.establishment (establishment_id, businessman_id, name, revenue, number_of_employees, update_date, created_by)
        VALUES (test.establishment_seq.NEXTVAL, 1, 'Tienda Andina 1', 1000000.00, 5, SYSDATE, USER);
        INSERT INTO test.establishment (establishment_id, businessman_id, name, revenue, number_of_employees, update_date, created_by)
        VALUES (test.establishment_seq.NEXTVAL, 1, 'Tienda Andina 2', 2000000.00, 10, SYSDATE, USER);
        INSERT INTO test.establishment (establishment_id, businessman_id, name, revenue, number_of_employees, update_date, created_by)
        VALUES (test.establishment_seq.NEXTVAL, 2, 'Sucursal Caribe 1', 1500000.00, 7, SYSDATE, USER);
        INSERT INTO test.establishment (establishment_id, businessman_id, name, revenue, number_of_employees, update_date, created_by)
        VALUES (test.establishment_seq.NEXTVAL, 2, 'Sucursal Caribe 2', 1800000.00, 8, SYSDATE, USER);
        INSERT INTO test.establishment (establishment_id, businessman_id, name, revenue, number_of_employees, update_date, created_by)
        VALUES (test.establishment_seq.NEXTVAL, 3, 'Oficina Norte 1', 1200000.00, 6, SYSDATE, USER);
        INSERT INTO test.establishment (establishment_id, businessman_id, name, revenue, number_of_employees, update_date, created_by)
        VALUES (test.establishment_seq.NEXTVAL, 3, 'Oficina Norte 2', 1400000.00, 7, SYSDATE, USER);
    END seed_establishments;
 
END establishment_pkg;
/


-- ************************************************
-- 8.2 Establecimiento - Ejecución de Datos Semilla
-- ************************************************

-- Ejecutar insertar_businessman
BEGIN
    establishment_pkg.seed_establishments;
END;
/

