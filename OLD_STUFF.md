# Simply Build & Run

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

#### Links

* [Jenkins Template Sample](https://github.com/openshift/origin/tree/master/examples/jenkins)

## Links

* https://github.com/redhat-cop/container-pipelines/tree/master/basic-spring-boot
* https://middlewareblog.redhat.com/2017/05/05/red-hat-openshift-application-runtimes-and-spring-boot-details-you-want-to-know/
* [RHOAR](https://developers.redhat.com/products/rhoar/overview/)