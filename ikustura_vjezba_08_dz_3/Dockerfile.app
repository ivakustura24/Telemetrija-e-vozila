FROM eclipse-temurin:21-jre

COPY ./ikustura_vjezba_08_dz_3_app/target/ikustura_vjezba_08_dz_3_app-1.2.0-jar-with-dependencies.jar /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_CS.txt /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_PK.txt /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_R1.txt /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_R2.txt /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_R3.txt /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_SV.txt /app/
COPY ./ikustura_vjezba_08_dz_3_app/NWTiS_DZ1_V1.csv /app/

WORKDIR /app

EXPOSE 8000-8099

COPY ./docker-entrypoint.app.sh /docker-entrypoint.sh
RUN chmod -R 777 /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]

CMD ["app"]