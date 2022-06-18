DROP INDEX IF EXISTS devices_uuid_unique;
DROP INDEX IF EXISTS devices_ieee_address_unique;
DROP TABLE IF EXISTS devices;
DROP INDEX IF EXISTS zones_uuid_unique;
DROP TABLE IF EXISTS zones;
DROP TABLE IF EXISTS temperature_reports;

CREATE TABLE zones (
   id bigserial PRIMARY KEY,
   uuid uuid not null,
   date_created timestamp with time zone NOT NULL,
   date_modified timestamp with time zone NOT NULL,
   name character varying(255) NOT NULL
);

CREATE UNIQUE INDEX zones_uuid_unique ON zones(uuid);

CREATE TABLE devices (
    id bigserial PRIMARY KEY,
    uuid uuid not null,
    ieee_address character varying(24) NOT NULL,
    date_created timestamp with time zone NOT NULL,
    date_modified timestamp with time zone NOT NULL,
    date_code character varying(24),
    friendly_name character varying(255) NOT NULL,
    zone_id bigint REFERENCES zones(id),
    manufacturer text,
    model_id text,
    description text,
    last_seen timestamp with time zone,
    type character varying(64) NOT NULL,
    battery numeric,
    active boolean NOT NULL
);

CREATE UNIQUE INDEX devices_uuid_unique ON devices(uuid);
CREATE UNIQUE INDEX devices_ieee_address_unique ON devices(ieee_address);

CREATE TABLE temperature_reports
(
    id bigserial PRIMARY KEY,
    device_id bigint REFERENCES devices (id),
    zone_id bigint REFERENCES zones (id),
    date timestamp with time zone NOT NULL,
    value NUMERIC NOT NULL
)
