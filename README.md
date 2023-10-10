# NiFi Single User Login Password Generator

NiFi의 Single User Login 사용시 `nifi.sh`를 사용하지 않고 직접 패스워드를 생성할 때 사용하는 커맨드

## Build

```
# mvn clean package
```

## Run

다음의 커맨드로 NiFi Single User Login을 위한 패스워드를 설정합니다.

```
# java -jar nifi-single-user-password-1.0.0.jar adminadminadmin
Encoded Password : $2b$12$v8C0b9BMx7KkxRAzxlbu8OkVZjC9vWEv9Ve2gtxetdarSIs0KHKBe
```

NiFi가 설치되어 있는 노드에서는 다음과 같이 패스워드를 생성할 수 있습니다.

```
# <NIFI_HOME>/bin/nifi.sh set-single-user-credentials admin adminadminadmin
```
