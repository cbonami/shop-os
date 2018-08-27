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

* Install [Ansible]() in an Ubuntu box

> WIP !!

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
