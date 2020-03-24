
keytool -genkeypair -alias CA -keyalg RSA -keystore caks.jks -dname "CN=CA" -storetype pkcs12 -storepass csd1920 -keypass csd1920 -ext bc=ca:true

keytool -export -alias CA -keystore caks.jks -storetype pkcs12 -storepass csd1920 -file caroot.cert



