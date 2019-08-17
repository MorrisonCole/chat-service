#!/bin/bash

./gradlew clean shadowJar
docker-compose build