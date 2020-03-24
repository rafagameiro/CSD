#!/bin/bash

echo "Creating Client keys and Certs"

mkdir Client 

keytool -genkeypair -alias Client -keyalg RSA -keystore Client/clientks.jks -dname "CN=Client" -storetype pkcs12 -storepass csd1920 -keypass csd1920

keytool -certreq -keystore Client/clientks.jks -storetype pkcs12 -storepass csd1920 -alias Client -file client.csr

keytool -gencert -keystore caks.jks -storetype pkcs12 -storepass csd1920 -alias ca -infile client.csr -outfile Client/client.cert

cat caroot.cert > Client/clientchain.cert

cat Client/client.cert >> Client/clientchain.cert

rm client.csr

keytool -import -alias Client -file Client/clientchain.cert -keystore Client/clientks.jks -storetype pkcs12 -storepass csd1920

keytool -import -alias CA -file caroot.cert -keystore Client/clientts.jks -storetype pkcs12 -storepass csd1920 -keypass csd1920
