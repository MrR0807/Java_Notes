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
