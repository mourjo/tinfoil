# tinfoil - A Visit Tracker build on Domain Driven Design 

[![Maven Build with Docker](https://github.com/mourjo/tinfoil/actions/workflows/maven.yml/badge.svg)](https://github.com/mourjo/tinfoil/actions/workflows/maven.yml)

## Start docker services

```bash
docker compose up
```

## Compile the project

```bash
mvn clean package
```

## Start the server

```bash
java -cp tinfoil-web/target/tinfoil-web-1.0-SNAPSHOT.jar me.mourjo.main.Server
```

## Example use

Add a visit:

```bash
curl -s -X 'POST' 'http://localhost:7002/visit/albert-heijn/mourjo' | jq
{
  "message": "Your visit has been recorded"
}
```

Get visits for a user:

```bash
curl -s -X 'GET' 'http://localhost:7002/visits/all/mourjo' | jq
[
  {
    "storeName": "jumbo",
    "visits": [
      "1 minute, 26 seconds ago",
      "26 seconds ago"
    ]
  },
  {
    "storeName": "albert-heijn",
    "visits": [
      "2 minutes, 3 seconds ago"
    ]
  }
]
```
