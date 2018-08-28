# Shop Microservice Demo

## Prerequisites

[Pre-requisites](./PREREQ.md)

## Manual Deployment Instructions

### 1. Create Lifecycle Stages

```bash
$ oc login -u developer
$ oc create -f stockmanager-os/applier/projects/projects.yml
$ oc create -f productcatalogue-os/applier/projects/projects.yml
$ oc create -f shopfront-os/applier/projects/projects.yml
```

_Projects_ are isolated kubernetes _Namespaces_.

### 2. Stand up Jenkins master in build-namespace

> Note: on Minishift I first had to install the _Jenkins (Ephemeral)_ template: 
>```
>$ oc login -u system:admin
>$ oc create -f https://raw.githubusercontent.com/openshift/origin/master/examples/jenkins/jenkins-ephemeral-template.json -n openshift
>```

The OpenShift _Jenkins (Ephemeral)_ template gets jenkins up and running in the _xxx-build_ namespace.
As this is the ephemeral setup of jenkins, no persistent volumes are used.

```
$ oc login -u developer
$ oc process openshift//jenkins-ephemeral | oc apply -f- -n stockmanager-os-build
$ oc process openshift//jenkins-ephemeral | oc apply -f- -n productcatalogue-os-build
$ oc process openshift//jenkins-ephemeral | oc apply -f- -n shopfront-os-build
```

This also creates a Service Account 'jenkins' that will need to get access to the various microservices in all namespaces (ldv, abt, etc).

### 3. Instantiate Pipeline

#### Deploy-template

A _deploy template_ is provided for every microservice:

* [stockmanager-os/applier/templates/deployment.yml](stockmanager-os/applier/templates/deployment.yml) 
* [productcatalogue-os/applier/templates/deployment.yml](productcatalogue-os/applier/templates/deployment.yml) 
* [shopfront-os/applier/templates/deployment.yml](shopfront-os/applier/templates/deployment.yml) 

that defines all of the basic OpenShift resources required to run our Spring Boot apps on Tomcat. 
The templates will create for each microservice the following:

* A `DeploymentConfig`
* An `ImageStream`
* A `Service`
* A `Route`
* A `RoleBinding` to allow Jenkins to deploy in each namespace.

We need the resources in all 'environments' (the projects dedicated to ldv, abt, etc):

```
$ oc login -u developer
$ oc process -f stockmanager-os/applier/templates/deployment.yml --param-file=stockmanager-os/applier/params/deployment-ldv | oc apply -f-
$ oc process -f stockmanager-os/applier/templates/deployment.yml --param-file=stockmanager-os/applier/params/deployment-abt | oc apply -f-
$ oc process -f stockmanager-os/applier/templates/deployment.yml --param-file=stockmanager-os/applier/params/deployment-cbt | oc apply -f-
$ oc process -f stockmanager-os/applier/templates/deployment.yml --param-file=stockmanager-os/applier/params/deployment-prd | oc apply -f-
$ oc process -f productcatalogue-os/applier/templates/deployment.yml --param-file=productcatalogue-os/applier/params/deployment-ldv | oc apply -f-
$ oc process -f productcatalogue-os/applier/templates/deployment.yml --param-file=productcatalogue-os/applier/params/deployment-abt | oc apply -f-
$ oc process -f productcatalogue-os/applier/templates/deployment.yml --param-file=productcatalogue-os/applier/params/deployment-cbt | oc apply -f-
$ oc process -f productcatalogue-os/applier/templates/deployment.yml --param-file=productcatalogue-os/applier/params/deployment-prd | oc apply -f-
$ oc process -f shopfront-os/applier/templates/deployment.yml --param-file=shopfront-os/applier/params/deployment-ldv | oc apply -f-
$ oc process -f shopfront-os/applier/templates/deployment.yml --param-file=shopfront-os/applier/params/deployment-abt | oc apply -f-
$ oc process -f shopfront-os/applier/templates/deployment.yml --param-file=shopfront-os/applier/params/deployment-cbt | oc apply -f-
$ oc process -f shopfront-os/applier/templates/deployment.yml --param-file=shopfront-os/applier/params/deployment-prd | oc apply -f-
```

#### Build-template

> Note: On Minishift I had to first create the S2I builderimage's imagestream in the _openshift_ namespace:
>```
>$ oc login -u system:admin
>$ oc create -f https://raw.githubusercontent.com/jboss-openshift/application-templates/master/openjdk/openjdk18-image-stream.json -n openshift
>```


A _build template_ is provided at

* [stockmanager-os/applier/templates/build.yml](stockmanager-os/applier/templates/build.yml) 
* [productcatalogue-os/applier/templates/build.yml](productcatalogue-os/applier/templates/build.yml)
* [shopfront-os/applier/templates/build.yml](shopfront-os/applier/templates/build.yml)

that defines all the resources required to _build_ our java app. 
The template includes all resources for an [OpenShift Pipeline Build](https://docs.openshift.com/container-platform/3.9/dev_guide/dev_tutorials/openshift_pipeline.html):

* A `BuildConfig` that defines a `JenkinsPipelineStrategy` build, which will be used to define our pipeline.
* A `BuildConfig` that defines a `Source` build with `Binary` input (the jar that will be built). This will build our image.

Deploy the build template in _xxx-build_ namespace, as it is there that Jenkins runs.

```
$ oc process -f stockmanager-os/applier/templates/build.yml --param-file stockmanager-os/applier/params/build | oc apply -f-
$ oc process -f productcatalogue-os/applier/templates/build.yml --param-file productcatalogue-os/applier/params/build | oc apply -f-
$ oc process -f shopfront-os/applier/templates/build.yml --param-file shopfront-os/applier/params/build | oc apply -f-
```

## Links

* [RedHat Spring Boot Reference Architecture - code](https://github.com/RHsyseng/spring-boot-msa-ocp)
* [Getting started with Java S2I image](https://developers.redhat.com/blog/2017/02/23/getting-started-with-openshift-java-s2i/)
* [Cucumber on OpenShift](https://eleanordare.com/blog/2017/6/15/running-cucumber-tests-in-openshift-from-a-jenkins-pipeline)
* [RedHat Helloworld MSA](https://github.com/redhat-helloworld-msa)