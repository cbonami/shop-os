# Shop Microservice Demo

## Prerequisites

* Install [Chocolatey](https://chocolatey.org/)
    * This step is optional, but very handy; I will use it to install various tools with it.

* Install [cmder](https://github.com/cmderdev/cmder)
    * Again, optional, but very handy as it emulates all linux commands on windows.
    
```bash
choco install cmder
```

* Install [MiniShift](https://github.com/minishift/minishift)

```bash
choco install virtualbox
choco install minishift
choco install openshift-cli
minishift config set vm-driver virtualbox
minishift config set cpus 4
minishift config set memory 16g
minishift start
```

## Build & Run

```bash
oc login https://192.168.99.104:8443 --token=iudz2GAflO9TZEzdd3SYeRDM9hHIUKhzEn6OOUtlbRI
oc new-project shop
```

We make use of the [codecenric/springboot-maven3-centos S2I image](https://github.com/codecentric/springboot-maven3-centos) to build a docker image containing the spring boot app.
We make an app based on the S2I method/image, and hand our source-code to it like this:

```bash
oc new-app codecentric/springboot-maven3-centos~https://github.com/cbonami/shop-os.git --context-dir=stockmanager-os
```

## Links

* https://medium.com/@pablo127/deploy-spring-boot-application-to-openshift-3-next-gen-2b311f55f0c5
* http://www.mastertheboss.com/jboss-frameworks/spring/deploy-your-springboot-applications-on-openshift