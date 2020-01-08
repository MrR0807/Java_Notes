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

















