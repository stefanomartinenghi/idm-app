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

Il workflow `.github/workflows/ci.yml` viene eseguito sulle pull request verso `main` e
manualmente. Usa Java 17 ed esegue `mvn clean verify`. Il workflow ha accesso al solo
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
