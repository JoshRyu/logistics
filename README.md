# logistics

- 제품, 상점을 관리하는 어플리케이션

# how to start

## Database Setup

```
cd database
docker-compose up -d
```

## Frontend Setup

```
cd frontend
npm i
npm run dev
```

## Backend Setup

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
