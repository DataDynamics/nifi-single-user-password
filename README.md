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

## NiFi 설정

NiFi를 Single User Login을 가능하도록 하려면 우선 SSL이 먼저 적용되어야 합니다. SSL은 NiFi Toolkit으로 적용할 수 있으며 다음의 스크립트를 활용할 수 있습니다.

```bash
#!/bin/sh

NIFI_TOOLKIT_HOME=/opt/nifi-toolkit
NIFI_SSL_HOME=/opt/nifi/security

CA_HOSTNAME=host1
PASSWD=@123qwe@123qwe
HOSTS=host1,host2

if [ ! -d "${NIFI_SSL_HOME}" ]; then
  echo "${NIFI_SSL_HOME} does not exist."
  mkdir ${NIFI_SSL_HOME}
fi

rm -rf ${NIFI_SSL_HOME}/*


HOST_STRING=""
for HOST in ${HOSTS//,/ }; do
    HOST_STRING=${HOST_STRING}" -n ${HOST}"
done

echo ${HOST_STRING}

sh ${NIFI_TOOLKIT_HOME}/bin/tls-toolkit.sh standalone \
${HOST_STRING} --days 3650 -c $CA_HOSTNAME \
--keyPassword ${PASSWD}  --trustStorePassword ${PASSWD}  --keyStorePassword ${PASSWD} \
-C CN=admin --clientCertPassword ${PASSWD} \
-o ${NIFI_SSL_HOME}

ls -lsa ${NIFI_SSL_HOME}
```

SSL을 설정한 후에는 다음과 같이 `<NIFI_HOME>/conf/authorizers.xml` 파일에 `single-user-authorizer`가 추가되어 있어야 합니다.

```xml
<authorizers>
    <authorizer>
        <identifier>single-user-authorizer</identifier>
        <class>org.apache.nifi.authorization.single.user.SingleUserAuthorizer</class>
    </authorizer>
</authorizers>
```

이제 NiFi의 주 설정파일인 `<NIFI_HOME>/conf/nifi.properties`에 다음과 같이 설정해야 합니다.

```
nifi.security.user.authorizer=single-user-authorizer
nifi.security.user.login.identity.provider=single-user-provider
```

생성한 패스워드는 다음과 같이 `<NIFI_HOME>/conf/login-identity-providers.xml` 파일에 다음과 같이 `single-user-provider`의 `Password`로 설정합니다.

```xml
<loginIdentityProviders>
    <provider>
        <identifier>single-user-provider</identifier>
        <class>org.apache.nifi.authentication.single.user.SingleUserLoginIdentityProvider</class>
        <property name="Username">admin</property>
        <property name="Password">$2b$12$xsEp3InInpNqZyFuo2X0/.elg.fzASkxvJrO27Zl9EIsuQj8.Hcw.</property>
    </provider>
</loginIdentityProviders>
```

## Cloudera CFM 설정

Cloudera CFM에서 설정하려면 Cloudera Manager > NiFi > Configuration에서 다음을 변경하도록 합니다.

|설정|KEY|VALUE|
|---|---|---|
|NiFi Node Advanced Configuration Snippet (Safety Valve) for staging/login-identity-providers.xml|`xml.loginIdentityProviders.provider.single-user-provider.enabled`|`true`|
||`xml.loginIdentityProviders.provider.single-user-provider.class`|`org.apache.nifi.authentication.single.user.SingleUserLoginIdentityProvider`|
||`xml.loginIdentityProviders.provider.single-user-provider.property.Username`|`admin`|
||`xml.loginIdentityProviders.provider.single-user-provider.property.Password`|`생성한 패스워드`|
|Authorizer Identifier||`single-user-authorizer`|
|Login Identity Provider ID||`single-user-provider`|
|NiFi Node Advanced Configuration Snippet (Saftey Valve) for staging/authorizers.xml|`xml.authorizers.authorizer.single-user-authorizer.class`|`org.apache.nifi.authorization.single.user.SingleUserAuthorizer`|
||`xml.authorizers.authorizer.single-user-authorizer.enabled`|`true`|
