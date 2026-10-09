# idm-app

Applicazione Spring Boot su Java 25 usata per dimostrare il processo GitOps CCNO.

## Avvio locale

```bash
mvn verify
java -jar target/idm-app-ccno.jar
```

Endpoint disponibili:

- `GET /`, `GET /api/idm` e `GET /prova-browser` restituiscono nome applicazione e ambiente;
- `GET /actuator/health/readiness` espone la readiness probe Kubernetes;
- `GET /actuator/health/liveness` espone la liveness probe Kubernetes.

Durante la migrazione progressiva degli ambienti, `GET /health` resta disponibile come
endpoint di compatibilita' per le vecchie probe di qual e prod. In `local-test` Kubernetes
usa gia' esclusivamente gli endpoint Actuator dedicati.

Il codice applicativo e' separato in Controller, Service e DTO. Un filtro HTTP registra le
richieste applicative, ma omette dai normali access log le probe Actuator concluse con
successo. Le probe fallite vengono invece registrate.

Variabili runtime:

- `PORT`, default `8080`;
- `APP_ENV`, default `local`;
- `SECURITY_ENABLED`, default `false`;
- `SPRING_PROFILES_ACTIVE=database` abilita JPA quando MariaDB e' disponibile;
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME` e
  `SPRING_DATASOURCE_PASSWORD` configurano MariaDB;
- `SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI` configura Keycloak quando
  `SECURITY_ENABLED=true`.

Il profilo predefinito `no-database` disabilita l'autoconfigurazione JPA perche' nel cluster
locale non e' ancora presente MariaDB. Le dipendenze JPA e MariaDB sono gia' incluse e
diventano operative attivando il profilo `database` con le relative credenziali.

## Continuous Integration

Il workflow `.github/workflows/ci.yml` viene eseguito sulle pull request verso `main` e
manualmente. Usa Java 25 ed esegue `mvn clean verify`. Il workflow ha accesso al solo
contenuto del repository e non dispone di credenziali di deploy.

## Pubblicazione dell'immagine

Dopo un push a `main`, `.github/workflows/publish-image.yml` esegue due job sequenziali. Il
primo usa un runner GitHub, esegue test e packaging una sola volta e pubblica il JAR con il
relativo checksum SHA-256. Il secondo job parte solo se il primo termina correttamente, usa il
runner self-hosted con label `idm-local` e costruisce l'immagine scaricando esattamente quel
JAR, senza ricompilarlo. L'immagine è identificata dal commit completo:

```text
localhost:5001/idm-app:<git-sha>
```

La pipeline richiede poi un token temporaneo alla GitHub App
`stefanomartinenghi-idm-gitops-ci`, aggiorna esclusivamente l'overlay `local-test` in
`idm-gitops` e pubblica il relativo commit. La chiave privata dell'App non viene trasferita al
repository GitOps e il token di installazione viene revocato automaticamente a fine job.
