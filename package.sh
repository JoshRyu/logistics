#!/bin/bash

cd backend
./mvnw clean

cd ../frontend
npm run build

mkdir -p ../backend/target/classes/static

cp -r ./dist/* ../backend/target/classes/static

cd ../backend
./mvnw package -DskipTests
