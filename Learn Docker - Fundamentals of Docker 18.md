# What are containers?

A first approach was to use virtual machines (VMs). Instead of running multiple applications all on the same server, companies would package and run a single application per VM. With it, the compatibility problems were gone and life seemed good again. Unfortunately, the happiness didn't last for long. VMs are pretty heavy beasts on their own since they all contain a full-blown OS such as Linux or Windows Server and all that for just a single application. This is as if in the  transportation industry you would use a gigantic ship just to transport a truck load of bananas.

The ultimate solution to the problem was to provide something much more lightweight than VMs but also able to perfectly encapsulate the goods it needed to transport.

# Why are containers important?

First of all, Gartner has found in a recent report that **applications running in a container are more secure than their counterparts not running in a container.** Containers use Linux security primitives such as Linux kernel namespaces to sandbox different applications running on the same computers and **control groups (cgroups)**, to avoid the noisy neighbor problem where one bad application is using all available resources of a server and starving all other applications.

Containers make it easy to simulate a production-like environment, even on a developer's laptop.

A third reason is that operators can finally concentrate on what they are really good at, provisioning infrastructure, and running and monitoring applications in production. No special libraries of frameworks need to be installed on those servers, just an OS and a container runtime such as Docker.

# The container ecosystem

Initially, Docker didn't have a solution for **container orchestration** thus other companies or projects, open source or not, tried to close this gap. The most prominent one is **Kubernetes** which was initiated by Google and then later donated to the CNCF. Other container orchestration products are **Apache Mesos, Rancher, Red Hat's Open Shift, Docker's own Swarm**, and more.

More recently, the trend goes towards a **service mesh.** As we containerize more and more applications, and as we refactor those applications into more microservice-oriented applications, we run into problems that simple orchestration software cannot solve anymore in a reliable and scalable way. **Topics in this area are service discovery, monitoring, tracing, and log aggregation.** Many new projects have emerged in this area, the most popular one at this time being **Istio**, which is also part of the CNCF.

# Container architecture

**Docker Engine**
Rest Interface -> libcontainerd; libnetwork; graph; plugins

**containerd + runc**

**Linux Operating System**
Namespaces (pid, net, ipc, mnt, ufs); Control Groups (cgroups); Layer Capabilities (Union Filesystem: Overlay, AUFS, Device Mapper); Other OS Functionality.

In the preceding diagram, we see three essential parts:
* On the bottom, we have the Linux operating system
* In the middle dark gray, we have the container runtime
* On the top, we have the Docker engine

Containers are only possible due to the fact that the Linux OS provides some primitives, such as **namespaces, control groups, layer capabilities**, and more which are leveraged in a very specific way by the container runtime and the Docker engine. Linux kernel namespaces such as **process ID (pid) namespaces or network(net) namespaces** allow Docker to encapsulate or sandbox processes that run inside the container. Control groups make sure that containers cannot suffer from the noisy neighbor syndrome, where a single application running in a container can consume most or all of the available resources of the whole Docker host.

The container runtime on a Docker host consists of:
* runc - low-level functionality of the container runtime
* containerd - based on runc, provides the higher-level functionality

# Chapter 2. Setting up a Working Environment

Containers must run on a Linux host. Neither Windows or Mac can run containers natively. Thus, we need to run a Linux VM on our laptop, where we can then run our containers. This is essentially what is ```docker-machine```. It enables to set up environment in which Docker (the Engine) can actually work.

More information: https://docs.docker.com/v17.09/machine/overview/

# Chapter 3. Working with Containers

Test docker ```docker -v```. Then try to run:
```
docker container run alpine echo "Hello World"
```
Result similar to this:
```
Unable to find image 'alpine:latest' locally
latest: Pulling from library/alpine
2fdfe1cd78c2: Pull complete
Digest: sha256:ccba511b...
Status: Downloaded newer image for alpine:latest
Hello World
```
# Starting, stopping, and removing containers

```
docker container run alpine echo "Hello World"
```
This command contains multiple parts. First and foremost, we have the word **docker**. This is the name of the Docker command-line interface (CLI), which we are using to interact with the Docker engine that is responsible to run containers. Next, we have the word **container**, which indicates the context we are working with. As we want to run a container, our context is the word container. Next is the actual **command we want to execute in the given context, which is run**.

Now we also need to tell Docker which container to run. In this case, this is the so-called **alpine** container. Finally, we need to define what kind of process or task shall be executed inside the container when it is running. In our case, this is the last part of the command, echo "Hello World".

Now, let's run this in an alpine container as a daemon in the background.

```
$ docker container run -d --name quotes alpine /bin/sh -c "while :; do wget -qO- https://talaikis.com/api/quotes/random; printf '\n'; sleep 5; done"
```
In the preceding expression, we have used two new command-line parameters, **-d** and **--name**. 
The -d tells Docker to run the process running in the container as a Linux daemon. 
The --name parameter in turn can be used to give the container an explicit name. In the preceding sample, the name we chose is quotes.

**One important takeaway is that the container name has to be unique on the system.**

### Listing containers

**docker container ls** - List of all containers running on the system
**docker container ls -a** - List not only the currently running containers but all containers that are defined on our system
**docker container ls -q** - List the IDs of all containers
**docker container rm -f $(docker container ls -a -q)** - Remove all containers
**docker container ls -h** - Invoke help for the list command

### Stopping and starting containers

**docker container stop quotes**. When you try to stop the quotes container, you will probably note that it takes a while until this command is executed. To be precise, it takes about 10 seconds. Why is this the case?

Docker sends a Linux SIGTERM signal to the main process running inside the container. If the process doesn't react to this signal and terminate itself, Docker waits for 10 seconds and then sends SIGKILL, which will kill the process forcefully and terminate the container.

**How do we get the ID of a container?**
```
export CONTAINER_ID = $(docker container ls | grep quotes | awk '{print $1}')
docker container stop $CONTAINER_ID
```

### Removing containers

```
docker container rm <container ID>
#Or
docker container rm <container name>
```
Sometimes, removing a container will not work as it is still running. If we want to force a removal, no matter what the condition of the container currently is, we can use the command-line parameter -f or --force.



















































