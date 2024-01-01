# logistics

- 제품, 상점을 관리하는 어플리케이션

# how to start

## Database Setup

```
cd database
docker-compose up -d
```

## Frontend Setup

- Node Version: v18.12.0

  - nvm 사용시 아래 명령어로 버전 변경 가능

  ```
    nvm install 18.12.0
    nvm use 18.12.0
  ```

- Frontend 설치 및 실행

```
cd frontend
npm i
npm run dev
```

## Backend Setup

- Java Version: v17

- Spring Boot 실행 전 application.yml 수정

```
  sql:
    init:
      mode: always -> never
```

- Spring Boot 실행 후 (JPA Entity 생성 후)

```
  sql:
    init:
      mode: never -> always
```

## Swagger URL

http://localhost:11101/swagger-ui/index.html

## Deploy

### Jar Build

```sh
sh package.sh
```

### Execute Jar

```cmd
java -jar ./backend/target/{jarFileName}.jar

```
