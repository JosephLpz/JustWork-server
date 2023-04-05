CREATE TABLE usuario (
    id                  BIGSERIAL PRIMARY KEY,
    uuid                uuid DEFAULT NULL,
    rut                 character varying(20) NOT NULL,
    nombre              character varying(255) DEFAULT NULL,
    email               character varying(255) NOT NULL,
    telefono            character varying(255) NOT NULL,
    eliminado           boolean NOT NULL DEFAULT false,
    fecha_creacion      timestamp without time zone NOT NULL,
    fecha_actualizacion timestamp without time zone,
    password_sha512     character varying(128) NOT NULL,
    firma               character varying(128) DEFAULT NULL,
    apellido   character varying(255) DEFAULT NULL,
    profesion   character varying(255) DEFAULT NULL,
    tipo       character varying(250) DEFAULT NULL
);

CREATE UNIQUE INDEX usuario_uuid_key ON usuario(uuid uuid_ops);

CREATE TABLE token (
    id                  BIGSERIAL PRIMARY KEY,
    fecha_creacion      timestamp without time zone,
    fecha_expiracion    timestamp without time zone,
    sso_fecha_expiracion    timestamp without time zone,
    sha_token           character varying(128) DEFAULT NULL,
    firma               character varying(128) DEFAULT NULL,
    usuario_id          bigint NOT NULL,
    sso_access_token    character varying(255) DEFAULT NULL
    sso_refresh_token    character varying(255) DEFAULT NULL
);


CREATE TABLE rol(
    id                  BIGSERIAL PRIMARY KEY,
    uuid                uuid DEFAULT NULL,
    nombre              character varying(100) NOT NULL
);

CREATE TABLE usuario_roles(
 usuario_id         bigserial not null,
 rol_id             bigserial not null,
 PRIMARY KEY (usuario_id,rol_id)
);
create index ix_usuario_roles_usuario_id on usuario_roles (usuario_id);
create index ix_usuario_roles_rol_id on usuario_roles (rol_id);
alter table usuario_roles add constraint fk_usuario_roles_usuario_id foreign key (usuario_id) references usuario (id);
alter table usuario_roles add constraint fk_usuario_roles_rol_id foreign key (rol_id) references rol (id);

