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

# Inspecting containers

We have to provide either the container ID or name to identify the container of which we want to obtain the data:
```
docker container inspect quotes 
```
Sometimes, we need just a tiny bit of the overall information, and to achieve this, we can either use the grep tool or a filter. **The former method does not always result in the expected answer**, so let's look into the latter approach:
```
docker container inspect -f "{{json .State}}" quotes
```
The -f or --filter parameter is used to define the filter. The filter expression itself uses the Go template syntax.

# Exec into a running container

Sometimes, we want to run another process inside an already-running container. A typical reason could be to try to debug a misbehaving container. 
```
docker container exec -i -t quotes /bin/sh
```

The flag **-i** signifies that we want to run the additional process interactively, and **-t** tells Docker that we want it to provide us with a TTY (a terminal emulator) for the command. Finally, the process we run is **/bin/sh**.

We can also execute processes non-interactive:
```
docker container exec quotes ps
```

# Attaching to a running container

We can use the attachcommand to attach our Terminal's standard input, output, and error (or any combination of the three) to a running container using the ID or name of the container. Let's do this for our quotes container:
```
docker container attach quotes
```

To quit the container without stopping or killing it, we can press the key combination **Ctrl+P Ctrl+Q**. This **detaches us from the container while leaving it running** in the background. On the other hand, if we want to **detach and stop the container** at the same time, we can just press **Ctrl+C**.

# Retrieving container logs
If the logging output is directed to STDOUT and STDERR, then Docker can collect this information and keep it ready for consumption by a user or any other external system.
```
docker container logs quotes
```

If we want to only get a few of the latest entries, we can use the -t or --tail parameter, as follows:
```
docker container logs --tail 5 quotes
```
Sometimes, we want to follow the log that is produced by a container. This is possible when using the parameter -f or --follow.
```
docker container logs --tail 5 --follow quotes
```

# Anatomy of containers

Many individuals wrongly compare containers to VMs. However, this is a questionable comparison. **Containers are not just lightweight VMs.**

Containers are specially encapsulated and secured processes running on the host system.

Containers leverage a lot of features and primitives available in the Linux OS. The most important ones are **namespaces and cgroups**. All processes running in containers **share the same Linux kernel** of the underlying host operating system. This is fundamentally different compared with VMs, as **each VM contains its own full-blown operating system.**

### Architecture

---

**Docker Engine**
Rest Interface -> libcontainerd; libnetwork; graph; plugins

**containerd + runc**

**Linux Operating System**
Namespaces (pid, net, ipc, mnt, ufs); Control Groups (cgroups); Layer Capabilities (Union Filesystem: Overlay, AUFS, Device Mapper); Other OS Functionality.

---

We have the Linux operating system with its cgroups, namespaces, and layer capabilities as well as other functionality that we do not need to explicitly mention here. Then, there is an intermediary layer composed of containerd and runc. On top of all that now sits the Docker engine. The Docker engine offers a RESTful interface to the outside world that can be accessed by any tool, such as the Docker CLI, Docker for Mac, and Docker for Windows or Kubernetes to just name a few.

### Namespaces

A namespace is an abstraction of global resources such as filesystems, network access, process tree (also named PID namespace) or the system group IDs, and user IDs.

If we wrap a running process, say, in a filesystem namespace, then this process has the illusion that it owns its own complete filesystem. This of course is not true; it is only a virtual FS. From the perspective of the host, the contained process gets a shielded subsection of the overall FS. It is like a filesystem in a filesystem.

The same applies for all the other global resources for which namespaces exist. The user ID namespace is another example. Having a user namespace, we can now define a user jdoe many times on the system as long at it is living in its own namespace.

**The PID namespace is what keeps processes in one container from seeing or interacting with processes in another container.** A process might have the apparent PID 1 inside a container, but if we examine it from the host system, it would have an ordinary PID, say 334.

### Control groups (cgroups)

Linux cgroups are used to limit, manage, and isolate resource usage of collections of processes running on a system. Resources are CPU time, system memory, network bandwidth, or combinations of these resources, and so on.

### Union filesystem (UnionFS)

The UnionFS forms the backbone of what is known as container images. **UnionFS is mainly used on Linux and allows files and directories of distinct filesystems to be overlaid and with it form a single coherent file system.** In this context, the individual filesystems are called branches. Contents of directories that have the same path within the merged branches will be seen together in a single merged directory, within the new, virtual filesystem. When merging branches, the priority between the branches is specified. In that way, when two branches contain the same file, the one with the higher priority is seen in the final FS.

### Container plumbing

The basement on top of which the Docker engine is built; we can also call it the container plumbing and is formed by the two component **runc and containerd.**

#### Runc
Runc is a lightweight, portable container runtime. It provides full support for Linux namespaces as well as native support for all security features available on Linux, such as SELinux, AppArmor, seccomp, and cgroups.

Runc is a tool for spawning and running containers according to the Open Container Initiative (OCI) specification.

#### Containerd
Runc is a low-level implementation of a container runtime; containerd builds on top of it, and adds higher-level features, such as image transfer and storage, container execution, and supervision, as well as network and storage attachments. With this, it manages the complete life cycle of containers.

# Chapter 4. Creating and Managing Container Images

## What are images?

In Linux, **everything is a file.** The whole operating system is basically a filesystem with files and folders stored on the local disk. This is an important fact to remember when looking at what container images are. As we will see, an **image** is basically a big tarball containing a filesystem. More specifically, it **contains a layered filesystem.**

### The layered filesystem

Container images are templates from which containers are created. These images are not just one monolithic block, but are composed of many layers. The first layer in the image is also called the base layer.

Each individual layer contains files and folders. Each layer only contains the changes to the filesystem with respect to the underlying layers. Docker uses a union filesystem to create a virtual filesystem out of the set of layers. 

As has been said previously, **each image starts with a base image.** **Typically**, this **base image is one of the official images found on Docker Hub, such as a Linux distro, Alpine, Ubuntu, or CentOS.**

### The writable container layer

As we have discussed, a container image is made of a stack of immutable or read-only layers. When the Docker engine creates a container from such an image, it adds a writable container layer on top of this stack of immutable layers. Example:

***ContainerLayer***
**3. Add static files**
**2. Add Nginx**
**1. Apline Linux**

**The container layer is marked as read/write.** Another advantage of the immutability of image layers is that they can be shared among many containers created from this image. **All that is needed is a thin, writable container layer for each container.**

# Creating images

There are three ways to create a new container image on your system:
* Interactively building a container that contains all the additions and changes one desires and then committing those changes into a new image
* Most important way is to use a Dockerfile to describe what's in the new image and then build this image using that Dockerfile as a manifest
* Creating an image by importing it into the system from a tarball

## Interactive image creation

The first way we can create a custom image is by interactively building a container. That is, we start with a base image that we want to use as a template and run a container of it interactively.
```
docker container run -it --name sample alpine /bin/sh
```

By default, the alpine container does not have the ping tool installed. Let's assume we want to create a new custom image that has ping installed. Inside the container, we can then run the following command:
```
/apk update && apk add iputils
```
This uses the Alpine package manager apk to install the iputils library, of which ping is a part. Once we have finished our customization, we can quit the container by typing exit at the prompt.

If we want to see what has changed in our container in relation to the base image, we can use the docker container diff command as follows:
```
docker container diff sample
```
The output should present a list of all modifications done on the filesystem of the container:
```
C /bin
C /bin/ping
C /bin/ping6
A /bin/traceroute
A /root/.ash_history
C /usr/lib
...
C /var/cache/apk
A /var/cache/apk/APKINDEX.5022a8a2.tar.gz
A /var/cache/apk/APKINDEX.70c88391.tar.gz
C /var/cache/misc
```

* **A - added**
* **C - changed**. 
* **D - deleted**

We can now use the ``docker container commit`` command to persist our modifications and create a new image from them:
```
docker container commit sample my-alpine sha256:44bca4141130ee8702e8e8efd1beb3cf4fe5aadb62a0c69a6995afd49c2e7419
```
With the preceding command, we have specified that the new image shall be called my-alpine. To verify that image has been created:
```
docker image ls
```
```
REPOSITORY TAG      IMAGE ID        CREATED               SIZE
my-alpine  latest   44bca4141130    About a minute ago    5.64MB
...
```
If we want to see how our custom image has been built, we can use the history command as follows:
```
docker image history my-alpine
```

## Using Dockerfiles

**Manually creating custom images** as shown in the previous section of this chapter is very **helpful when doing exploration, creating prototypes, or making feasibility studies.** But it has a serious **drawback: it is a manual process and thus is not repeatable or scalable.** It is also as **error-prone** as any task executed manually by humans.

Let's look at a sample Dockerfile:
```
FROM python:2.7
RUN mkdir -p /app
WORKDIR /app
COPY ./requirements.txt /app/
RUN pip install -r requirements.txt
CMD ["python", "main.py"]
```
Each line of the Dockerfile results in a layer in the resulting image.

### The FROM keyword

**Every Dockerfile starts with the ```FROM``` keyword.**

The syntax for the FROM instruction is straightforward. It's just:
```
FROM <image>
```
or
```
FROM <image>:<tag>
```
or 

```
FROM <image>@<digest>
```
The FROM instruction takes a tag or digest as a parameter. **If you decide to skip them, Docker will assume you want to build your image from the *latest* tag**. Be aware that latest will not always be the latest version of the image you want to build upon.

### RUN

The argument for ```RUN``` is any valid Linux command, such as the following:
```
RUN yum install -y wget
```
The preceding command is using the CentOS package manager yum to install the wget package into the running container. Ubuntu:
```
RUN apt-get update && apt-get install -y wget
```
More complex RUN:
```
RUN apt-get update \
  && apt-get install -y --no-install-recommends \
    ca-certificates \
    libexpat1 \
    libffi6 \
    libgdbm3 \
    libreadline7 \
    libsqlite3-0 \
    libssl1.1 \
  && rm -rf /var/lib/apt/lists/*
```

### The COPY and ADD keywords

These two keywords are used to copy files and folders from the host into the image that we're building. The two keywords are very similar, with the exception that the ```ADD``` keyword also lets us copy and unpack TAR files, as well as provide a URL as a source for the files and folders to copy.

Example:
```
COPY . /app
COPY ./web /app/web
COPY sample.txt /data/my-sample.txt
ADD sample.tar /app/bin/
ADD http://example.com/sample.txt /data/
```

* The first line copies all files and folders from the current directory recursively to the /app folder inside the container image
* The second line copies everything in the web subfolder to the target folder, /app/web
* The third line copies a single file, sample.txt, into the target folder, /data, and at the same time, renames it to my-sample.txt
* The fourth statement unpacks the sample.tar file into the target folder, /app/bin
* Finally, the last statement copies the remote file, sample.txt, into the target file, /data

Wildcards and single character symbols ```?``` are allowed in the source path. For example, the following statement copies all files starting with sample to the mydir folder inside the image:
```
COPY ./sample* /mydir/
```

What ADD basically does is copy the files from the source into the container's own filesystem at the desired destination.
```
ADD <source path or URL> <destination path>
```

If you need, **you can specify multiple source paths, and separate them with a comma.** All of them must be relative to the build context.

**If the source path doesn't end with a trailing slash, it will be considered a single file and just copied into the destination. If the source path ends with a trailing slash, it will be considered a directory: its whole contents will then be copied into the destination path, but the directory itself will not be created at the destination path.**

**Note that file archives that were downloaded from the network will not be decompressed.**

The <destination directory> is either an absolute path or a path which is relative to the directory specific by the ```WORKDIR``` instruction:

* ```ADD config.json projectRoot/``` will add the config.json file to <WORKDIR>/projectRoot/

* ```ADD config.json /absoluteDirectory/``` will add the config.json file to the /absoluteDirectory/

**Note that ADD shouldn't be used if you don't need its special features, such as unpacking archives, you should use COPY instead.**

COPY is almost the same as the ADD instruction, with one difference. COPY supports only the basic copying of local files into the container.

### The WORKDIR keyword

The WORKDIR keyword defines the working directory or context that is used when a container is run from our custom image. The WORKDIR instruction adds a working directory for any CMD, RUN, ENTRYPOINT, COPY, and ADD instructions that comes after it in the Dockerfile.

So, if I want to set the context to the /app/bin folder inside the image, my expression in the Dockerfile would have to look as follows:
```
WORKDIR /app/bin
```
All activity that happens inside the image after the preceding line will use this directory as the working directory. It is very important to note that the following two snippets from a Dockerfile are not the same:
```
RUN cd /app/bin
RUN touch sample.txt
```
Compare the preceding code with the following code:
```
WORKDIR /app/bin
RUN touch sample.txt
```
The former will create the file in the root of the image filesystem, while the latter will create the file at the expected location in the /app/bin folder. Only the WORKDIR keyword sets the context across the layers of the image. **The cd command alone is not persisted across layers.**

### The CMD and ENTRYPOINT keywords

The CMD and ENTRYPOINT keywords are special. **While all other keywords defined for a Dockerfile are executed at the time the image is built by the Docker builder, these two are actually definitions of what will happen when a container is started from the image we define.**

To better understand how to use the two keywords, let's analyze what a typical Linux command or expression looks like—for example, let's take the ping utility as an example, as follows:
```
ping 8.8.8.8 -c 3
```
In the preceding expression, ping is the command and 8.8.8.8 -c 3 are the parameters to this command. Let's look at another expression:
```
wget -O - http://example.com/downloads/script.sh
```
Again, in the preceding expression, wget is the command and -O - http://example.com/downloads/script.sh are the parameters.

Now that we have dealt with this, we can get back to CMD and ENTRYPOINT. **ENTRYPOINT is used to define the command of the expression while CMD is used to define the parameters for the command.**
```
FROM alpine:latest
ENTRYPOINT ["ping"]
CMD ["8.8.8.8", "-c", "3"]
```

The beauty of this is that I can now override the CMD part that I have defined in the Dockerfile (remember, it was ["8.8.8.8", "-c", "3"]) when I create a new container by adding the new values at the end of the docker container run expression:
```
docker container run --rm -it pinger -w 5 127.0.0.1
```
If we want to override what's defined in the ENTRYPOINT in the Dockerfile, we need to use the --entrypoint parameter in the docker container run expression.

Alternatively, one can also use what's called the **shell form**, for example:
```
CMD command param1 param2
```
Example:
```
FROM alpine:latest
CMD wget -O - http://www.google.com
```

#### CMD

```CMD ["executable","parameter1","parameter2"]``` - This is a so called exec form. It's also the preferred and recommended form. The parameters are JSON array, and they need to be enclosed in square brackets.

**Because CMD is the same as a starting point for the Docker engine when running a container, there can only be one single CMD instruction in a Dockerfile.**

CMD vs RUN. CMD is executed at runtime while RUN is executed at build time.

#### ENTRYPOINT

The syntax for the ENTRYPOINT instruction can have two forms, similar to CMD.

`ENTRYPOINT ["executable", "parameter1", "parameter2"]` - is the exec form, preferred and recommended.

`ENTRYPOINT command parameter1 parameter2` is a a shell form. Normal shell processing will occur. This form will also ignore any CMD or docker run command line arguments.

ENTRYPOINT can be also overridden when starting the container using the --entrypoint parameter for the docker run command. Note that you can override the ENTRYPOINT setting using --entrypoint, but this can only set the binary to execute (no sh -c will be used). 

#### CMD vs ENTRYPOINT

-- No ENTRYPOINT

```
╔════════════════════════════╦═════════════════════════════╗
║ No CMD                     ║ error, not allowed          ║
╟────────────────────────────╫─────────────────────────────╢
║ CMD [“exec_cmd”, “p1_cmd”] ║ exec_cmd p1_cmd             ║
╟────────────────────────────╫─────────────────────────────╢
║ CMD [“p1_cmd”, “p2_cmd”]   ║ p1_cmd p2_cmd               ║
╟────────────────────────────╫─────────────────────────────╢
║ CMD exec_cmd p1_cmd        ║ /bin/sh -c exec_cmd p1_cmd  ║
╚════════════════════════════╩═════════════════════════════╝
```

-- ENTRYPOINT exec_entry p1_entry

```
╔════════════════════════════╦══════════════════════════════════╗
║ No CMD                     ║ /bin/sh -c exec_entry p1_entry   ║
╟────────────────────────────╫──────────────────────────────────╢
║ CMD [“exec_cmd”, “p1_cmd”] ║ /bin/sh -c exec_entry p1_entry   ║
╟────────────────────────────╫──────────────────────────────────╢
║ CMD [“p1_cmd”, “p2_cmd”]   ║ /bin/sh -c exec_entry p1_entry   ║
╟────────────────────────────╫──────────────────────────────────╢
║ CMD exec_cmd p1_cmd        ║ /bin/sh -c exec_entry p1_entry   ║
╚════════════════════════════╩══════════════════════════════════╝
```

-- ENTRYPOINT [“exec_entry”, “p1_entry”]

```
╔════════════════════════════╦═════════════════════════════════════════════════╗
║ No CMD                     ║ exec_entry p1_entry                             ║
╟────────────────────────────╫─────────────────────────────────────────────────╢
║ CMD [“exec_cmd”, “p1_cmd”] ║ exec_entry p1_entry exec_cmd p1_cmd             ║
╟────────────────────────────╫─────────────────────────────────────────────────╢
║ CMD [“p1_cmd”, “p2_cmd”]   ║ exec_entry p1_entry p1_cmd p2_cmd               ║
╟────────────────────────────╫─────────────────────────────────────────────────╢
║ CMD exec_cmd p1_cmd        ║ exec_entry p1_entry /bin/sh -c exec_cmd p1_cmd  ║
╚════════════════════════════╩═════════════════════════════════════════════════╝
```

For example, if your Dockerfile is:

```
FROM debian:wheezy
ENTRYPOINT ["/bin/ping"]
CMD ["localhost"]
```
Running the image without any argument will ping the localhost:
```
docker run -it test
PING localhost (127.0.0.1): 48 data bytes
56 bytes from 127.0.0.1: icmp_seq=0 ttl=64 time=0.096 ms
56 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.088 ms
56 bytes from 127.0.0.1: icmp_seq=2 ttl=64 time=0.088 ms
^C--- localhost ping statistics ---
3 packets transmitted, 3 packets received, 0% packet loss
round-trip min/avg/max/stddev = 0.088/0.091/0.096/0.000 ms
```
Now, running the image with an argument will ping the argument:
```
docker run -it test google.com
PING google.com (173.194.45.70): 48 data bytes
56 bytes from 173.194.45.70: icmp_seq=0 ttl=55 time=32.583 ms
56 bytes from 173.194.45.70: icmp_seq=2 ttl=55 time=30.327 ms
56 bytes from 173.194.45.70: icmp_seq=4 ttl=55 time=46.379 ms
^C--- google.com ping statistics ---
5 packets transmitted, 3 packets received, 40% packet loss
round-trip min/avg/max/stddev = 30.327/36.430/46.379/7.095 ms
```
For comparison, if your Dockerfile is:
```
FROM debian:wheezy
CMD ["/bin/ping", "localhost"]
```
Running the image without any argument will ping the localhost:
```
docker run -it test
PING localhost (127.0.0.1): 48 data bytes
56 bytes from 127.0.0.1: icmp_seq=0 ttl=64 time=0.076 ms
56 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.087 ms
56 bytes from 127.0.0.1: icmp_seq=2 ttl=64 time=0.090 ms
^C--- localhost ping statistics ---
3 packets transmitted, 3 packets received, 0% packet loss
round-trip min/avg/max/stddev = 0.076/0.084/0.090/0.000 ms
```
But running the image with an argument will run the argument:
```
docker run -it test bash
root@e8bb7249b843:/#
```

## Building an image

Dockerfile content:
```
FROM centos:7
RUN yum install -y wget
```

To build a new container image using the preceding Dockerfile as a manifest or construction plan:
```
docker image build -t my-centos .
```
Please note that there is a period at the end of the preceding command. This command means that the Docker builder is creating a new image called my-centos using the Dockerfile that is present in the current directory.

## Multistep builds

Let's take a Hello World application written in C. Here is the code found inside the hello.c file:
```
#include <stdio.h>
int main (void)
{
  printf ("Hello, world!\n");
  return 0;
}
```

Now, we want to containerize this application and write this Dockerfile:
```
FROM alpine:3.7
RUN apk update &&
apk add --update alpine-sdk
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN mkdir bin
RUN gcc -Wall hello.c -o bin/hello
CMD /app/bin/hello
```
Now, let's build this image:
```
docker image build -t hello-world .
```

Once the build is done we can list the image and see its size shown as follows:
```
$ docker image ls | grep hello-world
hello-world      latest      e9b...     2 minutes ago     176MB
```
The reason for it being so big is that the image not only contains the Hello World binary, but also all the tools to compile and link the application from the source code.
It is precisely for this reason that we should define Dockerfiles as multistage. We have some stages that are used to build the final artifacts and then a final stage where we use the minimal necessary base image and copy the artifacts into it. This results in very small images. Have a look at this revised Dockerfile:
```
FROM alpine:3.7 AS build
RUN apk update && \
    apk add --update alpine-sdk
RUN mkdir /app
WORKDIR /app
COPY . /app
RUN mkdir bin
RUN gcc hello.c -o bin/hello

FROM alpine:3.7
COPY --from=build /app/bin/hello /app/hello
CMD /app/hello
```
Here, we have a first stage with an alias build that is used to compile the application, and then the second stage uses the same base image alpine:3.7, but does not install the SDK, and only copies the binary from the build stage, using the --from parameter, into this final image.

Let's build the image again as follows:
```
$ docker image ls | grep hello-world
hello-world-small   latest    f98...    20 seconds ago     4.16MB
hello-world         latest    469...    10 minutes ago     176MB
```

## Dockerfile best practices

Keep the number of layers that make up your image relatively small.
Reduce the image size is to use a .dockerignore file. We want to avoid copying unnecessary files and folders into an image to keep it as lean as possible.

# Chapter 5. Data Volumes and System Management
TODO

# Chapter 6. Distributed Application Architecture

## Defining the terminology

* **VM** - Acronym for virtual machine. This is a virtual computer.
* **Node** - Individual server used to run applications. This can be a physical server, often called bare metal, or a VM. A node can be a mainframe, supercomputer, standard business server, or even a Raspberry Pi. Nodes can be computers in a company's own data center or in the cloud. Normally, a node is part of a cluster.
* **Cluster** - Group of nodes connected by a network used to run distributed applications.
* **Network** - Physical and software-defined communication paths between individual nodes of a cluster and programs running on those nodes.

## Patterns and best practices

### Loosely coupled components

### Stateful versus stateless

In a distributed application architecture, stateless components are much simpler to handle than stateful components. Stateless components can be easily scaled up and scaled down.

### Service discovery

In the preceding figure, we see how Service A wants to communicate with Service B. But it can't do this directly; it has to first query the external authority, a registry service, here called a DNS Service, about the whereabouts of Service B. The registry service will answer with the requested information and hand out the IP address and port number with which Service A can reach Service B.

### Load balancing

If we have multiple instances of a service such as Service B running in our system, we want to make sure that every, of those instances gets an equal amount of workload assigned to it. 

## Running in production

### Application updates

#### Blue-green deployments

Once green is installed, one can execute smoke tests against this new service and, if those succeed, the router can be configured to funnel all traffic that previously went to blue to the new service, green. The behavior of green is then observed closely and, if all success criteria are met, blue can be decommissioned.

#### Canary releases

Route 1% of traffic to new version. Monitor. Increase traffic. Monitor. Up until 100%.

# Chapter 7. Single-Host Networking

## The container network model

Docker has defined a very simple networking model, the so-called **container network model (CNM)**. 
The CNM has three elements—sandbox, endpoint, and network:

* **Sandbox**: The sandbox perfectly isolates a container from the outside world. No inbound network connection is allowed into the sandboxed container.
* **Endpoint**: An endpoint is a controlled gateway from the outside world into the network's sandbox that shields the container. The endpoint connects the network sandbox (but not the container) to the third element of the model, which is the network.
* **Network**: The network is the pathway that transports the data packets of an instance of communication from endpoint to endpoint, or ultimately from container to container.

## Network firewalling

**Software-defined networks (SDN)** are easy and cheap to create.

Example: Application consisting of three services: webAPI, productCatalog, and database. We want webAPI to be able to communicate with productCatalog, but not with the database, and we want productCatalog to be able to communicate with the database service. We can solve this situation by placing webAPI and the database on different networks and attach productCatalog to both of these networks.

Since creating SDNs is cheap, and each network provides added security by isolating resources from unauthorized access, it is highly recommended that you design and run applications so that they use multiple networks and run only services on the same network that absolutely need to communicate with each other.

## The bridge network

List all networks on the host:
```
docker network ls
```

The **scope** being **local** just means that this type of network is **restricted to a single host and cannot span across multiple hosts.** 

IP address management (IPAM) - software that is used to track IP addresses that are used on a computer. The important part in the IPAM block is the **Config node with its values for Subnet and Gateway.** The subnet for the bridge network is defined by default as 172.17.0.0/16. This means that all containers attached to this network will get an IP address assigned by Docker that is taken from the given range, which is 172.17.0.2 to 172.17.255.255. **The 172.17.0.1 address is reserved for the router of this network whose role in this type of network is taken by the Linux bridge.**

```
"IPAM": {
    "Driver": "default",
    "Options": null,
    "Config": [
        {
            "Subnet": "172.17.0.0/16",
            "Gateway": "172.17.0.1"
        }
    ]
}
```

**By default, only traffic from the egress is allowed, and all ingress is blocked. What this means is that while containerized applications can reach the internet, they cannot be reached by any outside traffic.**

* **ingress:** traffic entering or uploaded into container
* **egress:** traffic exiting or downloaded from container

We are not limited to just the bridge network, as Docker allows us to define our own custom bridge networks. **This is not just a feature that is nice to have, but it is a recommended best practice to not run all containers on the same network, but to use additional bridge networks to further isolate containers that have no need to communicate with each other.** To create a custom bridge network calledsample-net, use the following command:
```
docker network create --driver bridge sample-net
```
**On user-defined networks like alpine-net, containers can not only communicate by IP address, but can also resolve a container name to an IP address.**

### Use the default bridge network

https://docs.docker.com/network/network-tutorial-standalone/

Start two alpine containers running ash, which is Alpine’s default shell rather than bash. The -dit flags mean to start the container detached (in the background), interactive (with the ability to type into it), and with a TTY (so you can see the input and output). Since you are starting it detached, you won’t be connected to the container right away. Instead, the container’s ID will be printed. Because you have not specified any --network flags, the containers connect to the default bridge network.

```
docker run -dit --name alpine1 alpine ash
docker run -dit --name alpine2 alpine ash
```

Inspect the bridge network to see what containers are connected to it.
```
"Containers": {
    "602dbf1edc81813304b6cf0a647e65333dc6fe6ee6ed572dc0f686a3307c6a2c": {
        "Name": "alpine2",
        "EndpointID": "03b6aafb7ca4d7e531e292901b43719c0e34cc7eef565b38a6bf84acf50f38cd",
        "MacAddress": "02:42:ac:11:00:03",
        "IPv4Address": "172.17.0.3/16",
        "IPv6Address": ""
    },
    "da33b7aa74b0bf3bda3ebd502d404320ca112a268aafe05b4851d1e3312ed168": {
        "Name": "alpine1",
        "EndpointID": "46c044a645d6afc42ddd7857d19e9dcfb89ad790afb5c239a35ac0af5e8a5bc5",
        "MacAddress": "02:42:ac:11:00:02",
        "IPv4Address": "172.17.0.2/16",
        "IPv6Address": ""
    }
}
```

From within alpine1, make sure you can connect to the internet by pinging google.com. The -c 2 flag limits the command to two ping attempts.
```
ping -c 2 google.com

PING google.com (172.217.3.174): 56 data bytes
64 bytes from 172.217.3.174: seq=0 ttl=41 time=9.841 ms
64 bytes from 172.217.3.174: seq=1 ttl=41 time=9.897 ms
```
Now try to ping the second container. First, ping it by its IP address, 172.17.0.3:
```
# ping -c 2 172.17.0.3

PING 172.17.0.3 (172.17.0.3): 56 data bytes
64 bytes from 172.17.0.3: seq=0 ttl=64 time=0.086 ms
64 bytes from 172.17.0.3: seq=1 ttl=64 time=0.094 ms
```
This succeeds. Next, try pinging the alpine2 container by container name. This will fail.
```
# ping -c 2 alpine2

ping: bad address 'alpine2'
```

### Use user-defined bridge networks
Create the alpine-net network. You do not need the --driver bridge flag since it’s the default, but this example shows how to specify it.
```
docker network create --driver bridge alpine-net
```
Inspect the alpine-net network. Notice that this network’s gateway is 172.18.0.1, as opposed to the default bridge network, whose gateway is 172.17.0.1.

Create your four containers. Notice the --network flags. You can only connect to one network during the docker run command, so you need to use docker network connect afterward to connect alpine4 to the bridge network as well.
```
docker run -dit --name alpine1 --network alpine-net alpine ash
docker run -dit --name alpine2 --network alpine-net alpine ash
docker run -dit --name alpine3 alpine ash
docker run -dit --name alpine4 --network alpine-net alpine ash
docker network connect bridge alpine4
```
Check if you can call from one to another.

Remove all containers.
```
docker container rm -f $(docker container ls -aq)
```
## The host network

There exist occasions where we want to run a container in the network namespace of the host. This can be necessary when we need to run some software in a container that is used to analyze or debug the host network's traffic. **But keep in mind that these are very specific scenarios. When running business software in containers, there is no good reason to ever run the respective containers attached to the host's network.**

## The null network

Sometimes, we need to run a few application services or jobs that do not need any network connection at all to execute the task. It is strongly advised that you run those applications in a container that is attached to the none network. This container will be completely isolated, and thus safe from any outside access. Let's run such a container:
```
docker container run --rm -it --network none alpine:latest /bin/sh
```
## Running in an existing network namespace

Normally, Docker creates a new network namespace for each container we run.

Alternatively, we can define several containers in same network namespace.

```
                           Network Namespace
                           /               \
╔════════════════════════════════╗     ╔══════════════╗
  ╔════════════╗  ╔════════════╗        ╔════════════╗
  ║Container #1║  ║Container #2║        ║Container #3║
  ╚════════════╝  ╚════════════╝        ╚════════════╝
╚════════════════════════════════╝     ╚══════════════╝
```









































