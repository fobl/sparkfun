keytool -genkey -keyalg RSA -alias selfsigned -keystore keystore.jks -storepass _Salty29 -validity 360 -keysize 2048

keytool -export -keystore keystore.jks -alias signFiles -file Example.cerkeytool -export -alias selfsigned -keystore keystore.jks -rfc

keytool -export -alias selfsigned -file mydomain.crt -keystore keystore.jks


keytool -importkeystore -srckeystore mycert.jks -destkeystore keystore.p12 -deststoretype PKCS12
concert PKCS12 key to unencrypted PEM:

openssl pkcs12 -in keystore.p12  -nodes -nocerts -out mydomain.key
