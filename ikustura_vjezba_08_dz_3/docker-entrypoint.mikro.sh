#!/bin/bash
set -e
cd /helidon
if [ "$1" = 'mikroservisi' ]; then

    echo "Mikroservisi"
    CLASSPATH="/helidon/libs/*"
    java -cp $CLASSPATH -jar ikustura_vjezba_08_dz_3_mikroservisi.jar
     exec sleep infinity
else
    exec "$@"
fi