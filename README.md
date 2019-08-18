# Chat Service [![Build Status](https://travis-ci.org/MorrisonCole/chat-service.svg?branch=master)](https://travis-ci.org/MorrisonCole/chat-service)
A scalable chat service built with Java, gRPC, & Docker.

## Configuration
### Production
To deploy on GCP, you'll need a project set up along with a service account having read/write permissions.

In the root of this project, create the two files:
* `.SECRET_CHAT_SERVICE_ACCOUNT.json` -> containing `.JSON` GCP service account credentials.
* `.SECRET_LOGIN_DATASTORE_CONFIG` -> containing custom format as follows:
```
projectId=my-gcp-project-id
useCredentials=true
```

## Developing
### Build
`./gradlew shadowJar`

### Test
`./gradlew test`

### Run
`docker-compose up --build`
