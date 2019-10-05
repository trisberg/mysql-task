# mysql-task
Sample task app reading from mysql table

## Build docker image

```[shell]
./mvnw compile com.google.cloud.tools:jib-maven-plugin:build -Dimage=trisberg/mysql-task:0.0.1-SNAPSHOT
```

## Create database

```[shell]
helm install --name messages --set mysqlDatabase=testdb --set mysqlRootPassword=projectr1ff stable/mysql
```

## Configure database

### Connect to database from local system

```[shell]
# Execute the following command to route the connection:
kubectl port-forward svc/messages-mysql 3306

mysql -h 127.0.0.1 -P 3306 -u root -p -D testdb
```

### Create table

```[shell]
CREATE TABLE data (
  id INT(4)  UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  created    TIMESTAMP,
  processed  CHAR(1),
  message    VARCHAR(255)
);
```

### Insert messages

```[shell]
INSERT INTO data (message,processed) VALUES('Hello', NULL);
```
