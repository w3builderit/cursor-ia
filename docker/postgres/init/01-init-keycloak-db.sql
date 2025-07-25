-- Create Keycloak database and user
CREATE USER keycloak WITH PASSWORD 'keycloak';
CREATE DATABASE keycloak OWNER keycloak ENCODING 'UTF8';
GRANT ALL PRIVILEGES ON DATABASE keycloak TO keycloak;