# idm-app

Applicazione Java 17 minimale usata per dimostrare il processo GitOps CCNO.

## Avvio locale

```bash
mvn verify
java -jar target/idm-app-1.0.0-SNAPSHOT.jar
```

Endpoint disponibili:

- `GET /` restituisce nome applicazione e ambiente;
- `GET /health` restituisce lo stato usato dalle probe Kubernetes.

Variabili runtime:

- `PORT`, default `8080`;
- `APP_ENV`, default `local`.

## Continuous Integration

Il workflow `.github/workflows/ci.yml` viene eseguito sulle pull request verso `main`, sui
push a `main` e manualmente. Usa Java 17, esegue `mvn clean verify` e conserva il JAR come
artefatto GitHub per sette giorni. Il workflow ha accesso al solo contenuto del repository e
non dispone di credenziali di deploy.
