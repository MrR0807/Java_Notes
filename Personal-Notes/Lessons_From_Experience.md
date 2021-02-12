# 2019.12.18
**Hibernate, JPQL, CriteriaBuilder, Criteria, JOIN FETCH, FETCH**

## Defining a JOIN FETCH clause with CriteriaBuilder

Working with a JOIN FETCH clause in a CriteriaQuery is a little special.

First of all, you can’t create it using the join method. You need to call the fetch method instead. If you want to define a LEFT JOIN FETCH or a RIGHT JOIN FETCH clause, you need to provide a JoinType enum value as the second parameter.

The second big difference is the return type of the fetch method. The returned Fetch interface is very similar to the Join interface. But it doesn’t define a get method. So, you can’t use it to access any attributes of the JOIN FETCHed entity.

As a workaround, you can cast the Fetch interface to a Join interface. Even so, the interfaces are independent of each other; this approach works with Hibernate and EclipseLink.

Here you can see an example of such a query.

```
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Author> cq = cb.createQuery(Author.class);
Root<Author> root = cq.from(Author.class);
Join<Object, Object> book = (Join<Object, Object>) root.fetch(Author_.BOOKS);
 
ParameterExpression<String> pTitle = cb.parameter(String.class);
cq.where(cb.like(book.get(Book_.TITLE), pTitle));
 
TypedQuery<Author> q = em.createQuery(cq);
q.setParameter(pTitle, "%Hibernate%");
List<Author> authors = q.getResultList();
```

## The better alternative to JOIN FETCH clauses

If you don’t like to cast the Fetch to a Join interface, you can use an EntityGraph instead. It’s another way to tell Hibernate to initialize the association.

[Source](https://thoughts-on-java.org/hibernate-tip-left-join-fetch-join-criteriaquery/)

-----------

# 2019.12.31

**Spring, Spring Fox, Swagger, Optional**

To show correct representation in Swagger of Optional type:
```
  return new Docket(DocumentationType.SWAGGER_2)
    .useDefaultResponseMessages(false)
    .groupName("sample-api")
    .apiInfo(apiInfo())
    .select()
    .paths(regex("/api.*"))
    .build()
    .genericModelSubstitutes(Optional.class); // add this
```

-----------
# 2020.01.06

**Spring, Spring Boot, Controller, Type Convertion, Casting, RequetParams, RequestBody**

https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#core-convert

```
import org.springframework.core.convert.converter.Converter;

@Component
public class TradeIdToTradeConverter implements Converter<String, Trade> {

    private TradeService tradeService;

    public TradeIdToTradeConverter (TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @Override
    public Trade convert (String id) {
        try {
            Long tradeId = Long.valueOf(id);
            return tradeService.getTradeById(tradeId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    

@RestController
@RequestMapping
public class Controller {

    @GetMapping
    public void doSomething(@RequestParam Trade trade) {
        //access trade which is converter from String
    }

}
    

```
# 2020.01.08

**Jackson, Deserialize, JSON, @JsonCreator, Spring, Spring Boot, RestTemplate, RestTemplateBuilder, HttpMessageConverter**

Problem:
When using Spring's RestTemplate and @JsonCreator (Jackson Annotation) to fetch and deserialize JSON, exception is thrown: Could not extract response: no suitable HttpMessageConverter found for response type.

Code:

Not working.
```
@RestController
@RequestMapping("test1")
public class TestOne {

    @PostMapping
    public Person postPerson(@RequestBody Person person) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Person> response = restTemplate.getForEntity("http://localhost:8080/test1", Person.class);

        return new Person(response.getBody());
    }

    @GetMapping
    public Person getPerson() {
        return new Person("first", "last");
    }
}
```

Person.class
```
public class Person {

    private String firstName;
    private String lastName;

    public Person(Person person) {
        this.firstName = person.firstName + " modified";
        this.lastName = person.lastName + " modified";
    }

    @JsonCreator
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
```

Solution:

https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-resttemplate-customization

> To make the scope of any customizations as narrow as possible, inject the auto-configured RestTemplateBuilder and then call its methods as required. Each method call returns a new RestTemplateBuilder instance, so the customizations only affect this use of the builder.

```
 @RequestMapping("test1")
 public class TestOne {
 
       private final RestTemplate restTemplate;

       public TestOne(RestTemplateBuilder restTemplateBuilder) {
               this.restTemplate = restTemplateBuilder.build();
       }

     @PostMapping
     public Person postPerson(@RequestBody Person person) {
         ResponseEntity<Person> response = restTemplate.getForEntity("http://localhost:8080/test1", Person.class);
         return new Person(response.getBody());
     }
```

**ALWAYS USE RestTemplateBuilder.**
https://medium.com/@TimvanBaarsen/spring-boot-why-you-should-always-use-the-resttemplatebuilder-to-create-a-resttemplate-instance-d5a44ebad9e9

# 2020.01.17

**Hibernate, JPQL, JOIN FETCH, FETCH, DISTINCT, SQL**

**Taken from [Vlad Mihalcea blog](https://vladmihalcea.com/jpql-distinct-jpa-hibernate/)**.

The DISTINCT keyword has a different purpose when it comes to entity queries. Without using DISTINCT, the JPA specification states that the returning entities resulting from a parent-child JOIN might contain object reference duplicates.

To visualize this behavior, consider the following JPQL query:
```
List<Post> posts = entityManager
.createQuery(
    "select p " +
    "from Post p " +
    "left join fetch p.comments " +
    "where p.title = :title", Post.class)
.setParameter(
    "title",
    "High-Performance Java Persistence eBook has been released!"
)
.getResultList();
 
LOGGER.info(
    "Fetched the following Post entity identifiers: {}",
    posts.stream().map(Post::getId).collect(Collectors.toList())
);
```

When running the JPQL query above, Hibernate generate the following output:
```
SELECT p.id AS id1_0_0_,
       pc.id AS id1_1_1_,
       p.created_on AS created_2_0_0_,
       p.title AS title3_0_0_,
       pc.post_id AS post_id3_1_1_,
       pc.review AS review2_1_1_,
       pc.post_id AS post_id3_1_0__
FROM   post p
LEFT OUTER JOIN
       post_comment pc ON p.id=pc.post_id
WHERE
       p.title='High-Performance Java Persistence eBook has been released!'

-- Fetched the following Post entity identifiers: [1, 1]
```
As illustrated by the log message, the returned posts List contains two references of the same Post entity object. This is because the JOIN duplicates the parent record for every child row that’s going to be fetched.

To remove the entity reference duplicates, we need to use the DISTINCT JPQL keyword:
```
List<Post> posts = entityManager
.createQuery(
    "select distinct p " +
    "from Post p " +
    "left join fetch p.comments " +
    "where p.title = :title", Post.class)
.setParameter(
    "title",
    "High-Performance Java Persistence eBook has been released!"
)
.getResultList();
 
LOGGER.info(
    "Fetched the following Post entity identifiers: {}",
    posts.stream().map(Post::getId).collect(Collectors.toList())
);
```

When executing the JPQL query above, Hibernate will now generate the following output:
```
SELECT DISTINCT
       p.id AS id1_0_0_,
       pc.id AS id1_1_1_,
       p.created_on AS created_2_0_0_,
       p.title AS title3_0_0_,
       pc.post_id AS post_id3_1_1_,
       pc.review AS review2_1_1_,
       pc.post_id AS post_id3_1_0__
FROM   post p
LEFT OUTER JOIN
       post_comment pc ON p.id=pc.post_id
WHERE
       p.title='High-Performance Java Persistence eBook has been released!'
 
-- Fetched the following Post entity identifiers: [1]
```

So, the duplicates were removed from the posts List, but the DISTINCT keyword was also passed to the underlying SQL statement. For this SQL query, the DISTINCT keyword serves no purpose since the result set will contain unique parent-child records.

If we analyze the execution plan for the previous SQL statement, we can see that a quicksort execution is being added to the plan:
```
Unique  (cost=23.71..23.72 rows=1 width=1068) (actual time=0.131..0.132 rows=2 loops=1)
  ->  Sort  (cost=23.71..23.71 rows=1 width=1068) (actual time=0.131..0.131 rows=2 loops=1)
        Sort Key: p.id, pc.id, p.created_on, pc.post_id, pc.review
        Sort Method: quicksort  Memory: 25kB
        ->  Hash Right Join  (cost=11.76..23.70 rows=1 width=1068) (actual time=0.054..0.058 rows=2 loops=1)
              Hash Cond: (pc.post_id = p.id)
              ->  Seq Scan on post_comment pc  (cost=0.00..11.40 rows=140 width=532) (actual time=0.010..0.010 rows=2 loops=1)
              ->  Hash  (cost=11.75..11.75 rows=1 width=528) (actual time=0.027..0.027 rows=1 loops=1)
                    Buckets: 1024  Batches: 1  Memory Usage: 9kB
                    ->  Seq Scan on post p  (cost=0.00..11.75 rows=1 width=528) (actual time=0.017..0.018 rows=1 loops=1)
                          Filter: ((title)::text = 'High-Performance Java Persistence eBook has been released!'::text)
                          Rows Removed by Filter: 3
Planning time: 0.227 ms
Execution time: 0.179 ms
```
The quicksort execution adds an unneeded overhead to our statement execution since we don’t need to eliminate any duplicates since the result set contains unique parent-child row combinations.

Using the hibernate.query.passDistinctThrough JPQL query hint
**To avoid passing the DISTINCT keyword to the underlying SQL statement, we need to activate the hibernate.query.passDistinctThrough JPQL query hint as illustrated by the following example:**
```
List<Post> posts = entityManager
.createQuery(
    "select distinct p " +
    "from Post p " +
    "left join fetch p.comments " +
    "where p.title = :title", Post.class)
.setParameter(
    "title",
    "High-Performance Java Persistence eBook has been released!"
)
.setHint("hibernate.query.passDistinctThrough", false)
.getResultList();
 
LOGGER.info(
    "Fetched the following Post entity identifiers: {}",
    posts.stream().map(Post::getId).collect(Collectors.toList())
);
```
When running the JPQL with the hibernate.query.passDistinctThrough hint activated, Hibernate executes the following SQL query:
```
SELECT
       p.id AS id1_0_0_,
       pc.id AS id1_1_1_,
       p.created_on AS created_2_0_0_,
       p.title AS title3_0_0_,
       pc.post_id AS post_id3_1_1_,
       pc.review AS review2_1_1_,
       pc.post_id AS post_id3_1_0__
FROM   post p
LEFT OUTER JOIN
       post_comment pc ON p.id=pc.post_id
WHERE
       p.title='High-Performance Java Persistence eBook has been released!'
 
-- Fetched the following Post entity identifiers: [1]
```

Therefore, the DISTINCT keyword is no longer passed to the SQL query, but entity duplicates are removed from the returning posts List.

# 2020.03.01

**Spring, @Async, ThreadPoolTaskExecutor, setRejectedExecutionHandler, ThreadPool, Thread**

```
@Configuration
public class CustomAsyncConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(2);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
```

When ThreadPoolTaskExecutor is overloaded by default it will stop taking tasks into queue. However, you can configure that with setRejectedExecutionHandler. More information: [The executor Element](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#scheduling-task-namespace-executor).


# 2020.03.20

**Maven, Surefire, Tests, UTF-8, Encoding, Plugin**

Some tests will fail, because Surefire does not handle UTF-8 letters well. To solve:
```
 <!-- Some tests assert on UTF-8 letters, thus without defined encoding, tests will fail.
            However they fail only when running with `mvn test`, but they succeed when launched through IntelliJ UI-->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>2.22.2</version>
                <configuration>
                    <argLine>@{argLine} -Dfile.encoding=UTF-8</argLine>
                </configuration>
            </plugin>

```

# 2020.04.02

**Maven, Swagger, Generate, Code to Swagger**

### Spring + Spring Fox

Solution.

Create an integration Test Suite, define where you want to place generated swagger.json (in code example it will be next to pom.xml). Everytime integration tests are run, new swagger file will be generated. It is possible to omnit this test in maven-surefire plugin.

GenereateSwagger test suite:
```
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GenerateSwagger {

    @Autowired
    WebApplicationContext context;

    @Test
    public void generateSwagger() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
                .andDo((result) -> {
                    Files.writeString(Path.of("swagger.json"), result.getResponse().getContentAsString());
                });
    }
}
```
If it is required to generate YAML formatted swagger. Then you will have to add additional maven dependency:
```
<dependency>
	<groupId>com.fasterxml.jackson.dataformat</groupId>
	<artifactId>jackson-dataformat-yaml</artifactId>
	<version>2.9.8</version>
</dependency>
```
And then GenerateSwagger test suite:
```
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class GenerateSwagger {

    @Autowired
    WebApplicationContext context;

    @Test
    public void generateSwagger() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
                .andDo((result) -> {
                    JsonNode jsonNodeTree = new ObjectMapper().readTree(result.getResponse().getContentAsString());
                    Files.writeString(Path.of("swagger.yaml"), new YAMLMapper().writeValueAsString(jsonNodeTree));
                });
    }
}
```

**NOTE!** 1) Use Spring Profile according to your requirements. If you do not have *test* Spring profile configured in your application, then the generator won't work. 2) Always try to use latest maven dependencies.

**Why we choose this way**

As it turns out, there aren't straightforward solutions to this problem. Most of time the advice is just to use swagger generated by Spring Fox from already running services. By default you can find it under /v2/api-docs url (example: http://some-service/v2/api-docs.).

However, this is not a solution for us, thus the alternatives we considered:

* **swagger-codegen**. [Github Issue](https://github.com/swagger-api/swagger-codegen/issues/6303). Their answer was: "In my understanding this maven plugin (as the word "codegen" implies) is focused on generating code from an OpenAPI specification (e.g. from a swagger.yaml". And they point towards Swagger Maven Plugin (kongchen) and SpringFox.
* **SpringFox**. [Github Issue #1](https://github.com/springfox/springfox/issues/2267), [Github Issue #2](https://github.com/springfox/springfox/issues/1959). Their answer was: "You can generate the swagger file in an integration test to a swagger json file, adding support for it in this project is not that easy." Furthermore, they add that "[integration test generation] is totally a valid technique. Infact it is the basis for how swagger2markup generates documentation and how http://www.jhipster.tech uses it."
* **Swagger Maven Plugin**. [Github Link](https://github.com/kongchen/swagger-maven-plugin). The documentation is quite vague and there are numerious configuration options. It is really hard to make it work and when it does not, maven just throws errors and aren't too many guidelines how to solve them. Also, it seems you are required to annotate everything with swagger annotations, and the plugin cannot infer method types/response/request models from simple Spring Controllers. Thus it was decided not to include it.
* **swagger-core**. [Github Link](https://github.com/swagger-api/swagger-core). Current version supports only JAX-RS2, thus it is not possible for Spring.
* Lastly, there was a proposed solution to not have it as an integration test, but rather utilize exec-maven-plugin. Find bellow how to setup it.

### Generated swagger with exec-maven-plugin

exec-maven-plugin is only for launching Java classes at particular maven phase. In this example it is required to generate swagger.json on compile phase.

Add plugin to pom.xml.
```
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <version>1.6.0</version>
    <executions>
        <execution>
            <phase>compile</phase>                                 //Maven phase. You can change to package,test etc.
            <goals>
                <goal>java</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <mainClass>lt.northstar.util.SwaggerGenerator</mainClass> //DEFINE your class with main method
    </configuration>
</plugin>
```
And the class:
```
public class SwaggerGenerator {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(Main.class)
                .profiles("test")
                .run(args);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup((WebApplicationContext) context).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs").accept(MediaType.APPLICATION_JSON))
                .andDo(result -> Files.writeString(Path.of("swagger.json"), result.getResponse().getContentAsString()));

        context.close();
    }
}
```
However, the drawbacks of this setup is:

* You have to add additional exec-maven-plugin;
* You have to change pom.xml spring-boot-starter-test maven dependency scope from test to default. So the test package can be included into main JAR. Otherwise you will not be able to use MockMvc class.
* No matter which maven phase you'll choose, you will have to boot up whole Spring Context during that phase. However, if swagger was generated via Integration test, then, most likely, Spring Context will be reused from previous test suites as it is cached in between tests.

### JAX-RS
Use swagger-core. Swagger resolver mechanism is able to analyze resource classes structure and various annotations (e.g JAX-RS, Jackson, etc.).

# 2020.06.02

**Spring, RestTemplate, REST, HttpClient, HTTP**

### Spring's RestTemplate and added headers for String class


```

/**
     * When using restTemplate.xxxForEntity(responseType = String.class) then Spring uses StringHttpMessageConverter to convert message from
     * incoming response to String. However, StringHttpMessageConverter will add all the available charsets available to the JVM in the Accept-Charset
     * header when used to call apis with RestTemplate. This has two problems:
     * - The outgoing request size can be huge
     * - The external system may not recognize all the charsets and throw errors
     * <p>
     * That is why it is required to turn off setting all available charsets.
     *
     * @param restTemplate restTemplate for which accepted charset is set off
     * @see <a href=https://github.com/spring-projects/spring-framework/issues/22506>Github issue</a>
     * @see <a href=https://stackoverflow.com/questions/44762794/java-spring-resttemplate-sets-unwanted-headers>Solution to the problem</a>
     */
    private static void removeAcceptCharsetFromHeaders(RestTemplate restTemplate) {
        for (HttpMessageConverter<?> converter : restTemplate.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
    }
```

### Always use @NotNull on collection element

This will allow to pass: ``ints: [null]``.
```
@NotNull
private final Set<Integer> ints;
```
While this - won't:

```
@NotNull
private final Set<@NotNull Integer> ints;
```
# 2021.01.11

## Reading files from JAR using Spring

```
@Component
public class PathMatchingLogFileWalker {

    public List<FileNameAndContent> walkLogs(String path) {
        var scanner = new PathMatchingResourcePatternResolver();
        try {
            var resources = scanner.getResources(path);
            return mapTo(resources);
        } catch (IOException e) {
            throw new RuntimeException("Could not parse jato log files", e);
        }
    }

    private List<FileNameAndContent> mapTo(Resource[] resources) throws IOException {
        var result = new ArrayList<FileNameAndContent>();
        for (var resource : resources) {
            result.add(new FileNameAndContent(resource.getFilename(), resource.getInputStream()));
        }
        return result;
    }
}

```

# 2021.02.12

## Mocking Amqp ConnectionFactory

```
@TestConfiguration
public class TestRabbitConfiguration {

    @Bean
    public ConnectionFactory connectionFactory() {
        var factory = mock(ConnectionFactory.class);
        var connection = mock(Connection.class);
        var channel = mock(Channel.class);
        willReturn(connection).given(factory).createConnection();
        willReturn(channel).given(connection).createChannel(anyBoolean());
        given(channel.isOpen()).willReturn(true);
        return factory;
    }
}
```

# 2021.02.12

## TestRestTemplate throws errors `` Failed to evaluate Jackson deserialization``

```
@TestConfiguration
public class TestRestTemplateConfiguration {

    /**
     * Problem overview
     * When test are run, a warning is generated (type information is missing for brevity):
     *```
     * c.j.MappingJackson2HttpMessageConverter : Failed to evaluate Jackson deserialization for type <type>:
     * com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Invalid type definition for type <type>:
     * Argument #0 has no property name, is not Injectable: can not use as Creator
     *```
     * Explanation:
     * When we test, we use TestRestTemplate. TestRestTemplate underneath registers MessageConverters to delegate message conversion according to their type.
     * In this case, we are interested in MappingJackson2HttpMessageConverter. Spring has a bug, which makes it register two MappingJackson2HttpMessageConverters.
     * One is configured correctly, and includes Jackson ``ParameterNamesModule`` module, the other is not. When RestTemplate tries to parse response, it goes
     * through a list of registered message converters and check whether it can convert that type:
     * ```
     * List<MediaType> allSupportedMediaTypes = getMessageConverters().stream()
     * 						.filter(converter -> canReadResponse(this.responseType, converter))
     * 						.flatMap(this::getSupportedMediaTypes)
     * 						.distinct()
     * 						.sorted(MediaType.SPECIFICITY_COMPARATOR)
     * 						.collect(Collectors.toList());
     * ```
     * Badly configured MappingJackson2HttpMessageConverter does not have ParameterNamesModule module, which essentially leads to Converter saying that it can
     * read the response, however, then it fails, due to misconfiguration. The solution is to remove badly configured MappingJackson2HttpMessageConverter
     * from RestTemplate.
     *
     * More resources:
     * In depth explanation: https://blog.trifork.com/2020/05/26/i-used-springs-resttemplate-to-fetch-some-json-and-you-wont-believe-what-happened-next/
     *
     * @see RestTemplateCustomizer
     * @see org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter::canRead
     * @see org.springframework.web.client.RestTemplate.AcceptHeaderRequestCallback::doWithRequest
     * @read @JsonCreator
     * @read javac -parameters
     */
    @Bean
    RestTemplateCustomizer unwantedConvertersRemovingCustomizer() {
        return restTemplate -> {
            boolean foundCorrectJackson2MappingMessageConverter = false;
            for (var iter = restTemplate.getMessageConverters().listIterator(); iter.hasNext(); ) {
                HttpMessageConverter<?> converter = iter.next();
                if (converter instanceof MappingJackson2HttpMessageConverter) {
                    if (foundCorrectJackson2MappingMessageConverter) {
                        iter.remove();
                    } else {
                        var jacksonObjectMapper = ((MappingJackson2HttpMessageConverter) converter).getObjectMapper();
                        var containsParameterNamesModule = jacksonObjectMapper.getRegisteredModuleIds()
                                .contains("com.fasterxml.jackson.module.paramnames.ParameterNamesModule");
                        if (containsParameterNamesModule) {
                            foundCorrectJackson2MappingMessageConverter = true;
                        }
                    }
                }
            }
        };
    }
}
```













