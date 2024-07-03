#!/bin/bash
set -e
cd /rest
if [ "$1" = 'rest' ]; then
    echo "Servisi"
    nohup java -jar ikustura_vjezba_08_dz_3_servisi-1.1.0.war &

    tail -f /dev/null
    
else
    exec "$@"
fi