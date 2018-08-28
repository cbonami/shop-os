# Keycloak SSO Server

## General
This Repository creates a container that starts KeyCloak. 

During the startup, the file `shop.json` that contains the shop Realm is imported.

The script register-clients.sh is also executed to create a client for the frontend. 

You can customize the frontend URL using the environent variables `$OS_SUBDOMAIN='rhel-cdk.10.1.2.2.xip.io' `and  `$OS_PROJECT='helloworld-msa'`

With `$KEYCLOAK_USER` and `$KEYCLOAK_PASSWORD` environment variables, we can set the admin credentials.

## Create a project for the SSO

```bash
$ oc new-project sso
```

#### Deploy a custom Keycloak instance

```bash
$ git clone https://github.com/redhat-helloworld-msa/sso
$ cd sso/
$ oc new-build --binary --name keycloak
$ oc start-build keycloak --from-dir=. --follow
$ oc new-app keycloak
$ oc expose svc/keycloak
```

##### (Optional) Enable Readiness probe

```bash
$ oc set probe dc/keycloak --readiness --get-url=http://:8080/auth
```

##### Specify the OpenShift domain

```bash
$ oc env dc/keycloak OS_SUBDOMAIN=<OPENSHIFT-DOMAIN>

#Using CDK3
$ oc env dc/keycloak OS_SUBDOMAIN=app.$(minishift ip).nip.io

#Example: OS_SUBDOMAIN=app.192.168.64.11.nip.io
$ oc env dc/keycloak OS_SUBDOMAIN=app.192.168.64.11.nip.io
```

#### Tell microservices where to find the Keycloak server

```bash
$ oc project shopfront-os

# Using CDK3
$ oc env dc KEYCLOAK_AUTH_SERVER_URL=http://keycloak-sso.`minishift ip`.nip.io/auth -l app

# Example: OS_SUBDOMAIN=192.168.64.11.nip.io
$ oc env dc KEYCLOAK_AUTH_SERVER_URL=http://keycloak-sso.192.168.64.11.nip.io/auth -l app

$ oc env dc/frontend ENABLE_SSO=true 
```

