#!/bin/bash
set -e
cd /app
if [ "$1" = 'app' ]; then
    echo "CentralniSustav"
    nohup java -cp ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.CentralniSustav NWTiS_DZ1_CS.txt &
    sleep 2 
    echo "PoslužiteljKazni"
    nohup java -cp ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.PosluziteljKazni NWTiS_DZ1_PK.txt &
    sleep 3 
    echo "PoslužiteljRadara R1"
    nohup java -cp ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.PosluziteljRadara NWTiS_DZ1_R1.txt &
    sleep 3
    echo "PoslužiteljRadara R2"
    nohup java -cp ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.PosluziteljRadara NWTiS_DZ1_R2.txt &
    sleep 3
    echo "PoslužiteljRadara R3"
    nohup java -cp ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.posluzitelji.PosluziteljRadara NWTiS_DZ1_R3.txt &
	sleep 3
    echo "SimulatorVozila V1"
    nohup java -cp ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar edu.unizg.foi.nwtis.ikustura.vjezba_08_dz_3.klijenti.SimulatorVozila NWTiS_DZ1_SV.txt NWTiS_DZ1_V1.csv 1 &
#    tail -f /dev/null
     exec sleep infinity
else
    exec "$@"
fi