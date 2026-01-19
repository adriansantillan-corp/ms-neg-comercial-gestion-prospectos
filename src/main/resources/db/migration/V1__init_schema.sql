-- ============================================================================
-- 1. TABLA PROSPECTO (Existente - Ajustes)
-- ============================================================================
-- Aseguramos que la tabla exista (si corres en H2 o DB vacía)
CREATE TABLE IF NOT EXISTS public.prospecto (
                                                id varchar(36) NOT NULL PRIMARY KEY, -- Ampliado a 36 para UUID
    uuid__c varchar(255),
    salesforce_id__c varchar(18),        -- Nuevo campo para guardar el ID de retorno de SF

-- Campos principales (Legacy)
    name varchar(80),
    nombre_razon_social__c varchar(80),
    nombre_contacto__c varchar(80),
    apellido_paterno__c varchar(80),
    apellido_materno__c varchar(80),
    documento_identificacion__c varchar(255),
    numero_documento__c varchar(25),
    id_fiscal__c varchar(40),
    correo_electronico__c varchar(80),
    telefono__c varchar(40),

    -- Direcciones Billing
    billing_address__c varchar(1300),
    billing_street__c varchar(255),
    billing_numero__c varchar(40),
    billing_interior__c varchar(10),
    billing_lote__c varchar(10),
    billing_manzana__c varchar(10),
    billing_city__c varchar(80),
    billing_state__c varchar(80),
    billing_country__c varchar(80),
    billing_postal_code__c varchar(20),
    billing_referencia__c varchar(80),
    billing_tipo_via__c varchar(255),
    billing_detalle_via__c varchar(80),
    billing_clasificacion__c varchar(255),
    billing_detalle_clasificacion__c varchar(80),

    -- Direcciones Shipping
    shipping_address__c varchar(1300),
    shipping_street__c varchar(255),
    shipping_numero__c varchar(10),
    shipping_interior__c varchar(10),
    shipping_lote__c varchar(10),
    shipping_manzana__c varchar(10),
    shipping_city__c varchar(80),
    shipping_state__c varchar(80),
    shipping_country__c varchar(80),
    shipping_postal_code__c varchar(20),
    shipping_referencia__c varchar(80),
    shipping_tipo_via__c varchar(255),
    shipping_detalle_via__c varchar(80),
    shipping_clasificacion__c varchar(255),
    shipping_detalle_clasificacion__c varchar(80),

    -- Metadata
    giro__c varchar(1300),
    subgiro__c varchar(18),
    descripcion__c text,
    fecha_de_nacimiento__c date,
    latitud__c varchar(255),
    longitud__c varchar(255),
    lista_de_precios__c varchar(18),
    pais__c varchar(1300),
    creado_en_nitro_app__c bool,
    usar_direccion_facturacion__c bool,

    created_date timestamp,
    last_modified_date timestamp
    );

-- AJUSTES SI LA TABLA YA EXISTÍA (Para BD Real)
-- Si la tabla ya existe con varchar(18), esto lo arregla a 36.
ALTER TABLE public.prospecto ALTER COLUMN id TYPE varchar(36);
-- Si no tenías salesforce_id__c separado del id, lo agregamos
ALTER TABLE public.prospecto ADD COLUMN IF NOT EXISTS salesforce_id__c varchar(18);


-- ============================================================================
-- 2. TABLA MODULO_DE_PROSPECTO (Existente - Ajustes)
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.modulo_de_prospecto (
                                                          id varchar(36) NOT NULL PRIMARY KEY,
    prospecto__c varchar(36),
    uuid__c varchar(255),
    dias_visita__c text,
    periodo_de_visita__c varchar(255),
    modulo__c varchar(18), -- ID Modulo Asignado
    is_deleted bool,
    created_date timestamp
    );

-- Ajustes
ALTER TABLE public.modulo_de_prospecto ALTER COLUMN id TYPE varchar(36);
ALTER TABLE public.modulo_de_prospecto ALTER COLUMN prospecto__c TYPE varchar(36);


-- ============================================================================
-- 3. TABLA SOLICITUD (Existente - Ajustes)
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.solicitud (
                                                id varchar(36) NOT NULL PRIMARY KEY,
    prospecto__c varchar(36),
    uuid__c varchar(255),
    estado__c varchar(255),
    tipo__c varchar(255),
    record_type_id varchar(18),
    enviar_para_aprobacion__c bool,
    created_date timestamp
    );

-- Ajustes
ALTER TABLE public.solicitud ALTER COLUMN id TYPE varchar(36);
ALTER TABLE public.solicitud ALTER COLUMN prospecto__c TYPE varchar(36);


-- ============================================================================
-- 4. TABLA PROSPECTO_FOTO (NUEVA - Obligatoria para el Microservicio)
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.prospecto_foto (
                                                     id varchar(36) NOT NULL PRIMARY KEY,
    prospecto_id varchar(36) NOT NULL,
    url_s3 text NOT NULL,
    path_s3 varchar(500) NOT NULL,
    nombre_archivo varchar(255),
    created_date timestamp DEFAULT NOW(),

    CONSTRAINT fk_prospecto_foto_prospecto
    FOREIGN KEY (prospecto_id) REFERENCES public.prospecto(id)
    );