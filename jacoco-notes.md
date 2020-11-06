Links:
* https://www.jacoco.org/jacoco/trunk/doc/maven.html
* https://www.jacoco.org/jacoco/trunk/doc/prepare-agent-mojo.html - if running with surefire
* https://www.baeldung.com/jacoco


```
<plugin>
  <groupId>org.jacoco</groupId>
  <artifactId>jacoco-maven-plugin</artifactId>
  <version>0.8.6</version>
  <executions>
      <execution>
          <goals>
              <goal>prepare-agent</goal>
          </goals>
      </execution>
      <execution>
          <id>report</id>
          <phase>prepare-package</phase>
          <goals>
              <goal>report</goal>
          </goals>
      </execution>
      <execution>
          <id>jacoco-check</id>
          <goals>
              <goal>check</goal>
          </goals>
          <configuration>
              <rules>
                  <rule>
                      <element>BUNDLE</element>
                      <limits>
                          <limit>
                              <counter>LINE</counter>
                              <value>COVEREDRATIO</value>
                              <minimum>100%</minimum>
                          </limit>
                      </limits>
                  </rule>
              </rules>
          </configuration>
      </execution>
  </executions>
</plugin>
```
