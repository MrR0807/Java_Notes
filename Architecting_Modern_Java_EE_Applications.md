# Chapter 2. Designing and Structuring Java Enterprise Applications

In enterprise projects the deployment artifacts, the WAR or JAR files, are either deployed to an application container or already ship the container themselves.

## One project per artifact

It is advisable to package the enterprise project into a single deployment artifact that emerges from a single project module.

### Apache Maven

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <failOnMissingWebXml>false</failOnMissingWebXml>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
The WAR file doesn't need to ship a web.xml deployment descriptor; this is why we instruct Maven not to fail the build on a missing descriptor. **In the past, the Servlet API required deployment descriptors in order to configure and map the application's Servlets. Since the advent of Servlet API version 3, web.xml descriptors are not necessarily required anymore; Servlets are configurable using annotations.**

## Enterprise project code structure



