# Shop Microservice Demo

## Prerequisites

* Install [Chocolatey](https://chocolatey.org/)
    * This step is optional, but very handy; I will use it to install various tools with it.

* Install [cmder](https://github.com/cmderdev/cmder)
    * Again, optional, but very handy as it emulates all linux commands on windows.
    
```bash
choco install cmder
```

* Install [Vagrant]()

todo

* Install [MiniShift](https://github.com/minishift/minishift)

```bash
choco install virtualbox
choco install minishift
choco install openshift-cli
minishift config set vm-driver virtualbox
minishift config set cpus 4
minishift config set memory 16g
minishift start
minishift addon apply admin-user
```

Note: use password 'admin' when logging in as admin:

```bash
oc login -u admin
```

* Ansible

> WIP

> Optional - only needed when you want to set up the whole CD/CI pipeline full-automatically with OpenShift Applier (=RedHat's Ansible templates for OCP)

I tried running the ansible client in Windows subsystem for Linux, but ran into weird problems.
So I decided to go for a Ubuntu Xenial (16.04) on virtualbox:

```bash
vagrant init ubuntu/xenial64
vagrant up
vagrant ssh
```

Inside the Ubuntu box:

```bash
sudo apt-add-repository ppa:ansible/ansible
sudo apt-get update
sudo apt-get install ansible -y
sudo apt install python3-pip python-dev libffi-dev libssl-dev -y
sudo pip install requests 
wget https://github.com/openshift/origin/releases/ download/v3.10.0/openshift-origin-client-tools-v3.10.0-dd10d17-linux-64bit.tar.gz
tar -xvf openshift-origin-client-tools-v3.10.0-dd10d17-linux-64bit.tar.gz
cd openshift-origin-client-tools-v3.10.0-dd10d17-linux-64bit
sudo mv oc /usr/local/bin
oc 
cd
git clone https://github.com/redhat-cop/openshift-applier
git clone https://github.com/redhat-cop/container-pipelines
cd container-pipelines/basic-spring-boot
```

## Manual Deployment Instructions

### 1. Create Lifecycle Stages

```bash
$ oc login -u developer
$ oc create -f stockmanager-os/applier/projects/projects.yml
```

_Projects_ are isolated kubernetes _Namespaces_.

### 2. Stand up Jenkins master in ldv

The OpenShift *default* template gets jenkins up and running in the 'build' namespace.
This is the ephemeral setup of jenkins, i.e. no persistent volumes are used.

```
$ oc login -u system:admin
$ oc create -f https://raw.githubusercontent.com/openshift/origin/master/examples/jenkins/jenkins-ephemeral-template.json -n openshift
$ oc login -u developer
$ oc process openshift//jenkins-ephemeral | oc apply -f- -n stockmanager-os-build
$ oc process openshift//jenkins-ephemeral | oc apply -f- -n productcatalogue-os-build
$ oc process openshift//jenkins-ephemeral | oc apply -f- -n shopfront-os-build
```

### 3. Instantiate Pipeline

#### _Deploy_ template

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
```

#### _Build_ template

A _build template_ is provided at

* [stockmanager-os/applier/templates/build.yml](stockmanager-os/applier/templates/build.yml) 
* [productcatalogue-os/applier/templates/build.yml](productcatalogue-os/applier/templates/build.yml)
* [shopfront-os/applier/templates/build.yml](shopfront-os/applier/templates/build.yml)

that defines all the resources required to _build_ our java app. 
The template includes:

* A `BuildConfig` that defines a `JenkinsPipelineStrategy` build, which will be used to define our pipeline.
* A `BuildConfig` that defines a `Source` build with `Binary` input (the jar that will be built). This will build our image.

Deploy the build template in LDV only, as it is there that Jenkins runs.

```
$ oc process -f stockmanager-os/applier/templates/build.yml --param-file stockmanager-os/applier/params/build | oc apply -f-
```

On Minishift I also had to create the builderimage's imagestream in the _openshift_ namespace:

```
$ oc login -u system:admin
$ oc create -f stockmanager-os\applier\templates\redhat-openjdk18-openshift.json -n openshift
```

#### Links

* [Jenkins Template Sample](https://github.com/openshift/origin/tree/master/examples/jenkins)

## Deprecated: Build & Run

```bash
$ oc login https://192.168.99.104:8443 --token=...
$ oc new-project shop
```

We make use of the [codecenric/springboot-maven3-centos S2I image](https://github.com/codecentric/springboot-maven3-centos) to build a docker image containing the spring boot app.
We make an app based on the S2I method/image, and hand our source-code to it, with the [oc new-app](https://docs.openshift.com/container-platform/3.7/dev_guide/application_lifecycle/new_app.html):

```bash
oc new-app codecentric/springboot-maven3-centos~https://github.com/cbonami/shop-os.git --context-dir=stockmanager-os --name=stockmanager
```

This will output:
```
--> Found Docker image 0274d1a (6 weeks old) from Docker Hub for "codecentric/springboot-maven3-centos"

    Spring Boot Maven 3
    -------------------
    Platform for building and running Spring Boot applications

    Tags: builder, java, java8, maven, maven3, springboot

    * An image stream will be created as "springboot-maven3-centos:latest" that will track the source image
    * A source build using source code from https://github.com/cbonami/shop-os.git will be created
      * The resulting image will be pushed to image stream "stockmanager:latest"
      * Every time "springboot-maven3-centos:latest" changes a new build will be triggered
    * This image will be deployed in deployment config "stockmanager"
    * Port 8080/tcp will be load balanced by service "stockmanager"
      * Other containers can access this service through the hostname "stockmanager"

--> Creating resources ...
    imagestream "springboot-maven3-centos" created
    imagestream "stockmanager" created
    buildconfig "stockmanager" created
    deploymentconfig "stockmanager" created
    service "stockmanager" created
--> Success
    Build scheduled, use 'oc logs -f bc/stockmanager' to track its progress.
    Application is not exposed. You can expose services to the outside world by executing one or more of the commands below:
     'oc expose svc/stockmanager'
    Run 'oc status' to view your app.
```

Then make a route:

```bash
oc expose service stockmanager
```

## Links

* https://github.com/redhat-cop/container-pipelines/tree/master/basic-spring-boot
* https://middlewareblog.redhat.com/2017/05/05/red-hat-openshift-application-runtimes-and-spring-boot-details-you-want-to-know/
* [RHOAR](https://developers.redhat.com/products/rhoar/overview/)