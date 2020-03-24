#!/bin/bash

echo "Creating Server keys and Certs"

mkdir Server

keytool -genkeypair -alias Server -keyalg RSA -keystore Server/serverks.jks -dname "CN=Server" -storetype pkcs12 -storepass csd1920 -keypass csd1920

keytool -certreq -keystore Server/serverks.jks -storetype pkcs12 -storepass csd1920 -alias Server -file server.csr

keytool -gencert -keystore caks.jks -storetype pkcs12 -storepass csd1920 -alias ca -infile server.csr -outfile Server/server.cert

cat caroot.cert > Server/serverchain.cert

cat Server/server.cert >> Server/serverchain.cert

rm server.csr

keytool -import -alias Server -file Server/serverchain.cert -keystore Server/serverks.jks -storetype pkcs12 -storepass csd1920

keytool -import -alias CA -file caroot.cert -keystore Server/serverts.jks -storetype pkcs12 -storepass csd1920 -keypass csd1920
