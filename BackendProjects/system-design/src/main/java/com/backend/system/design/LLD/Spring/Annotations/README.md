# Spring Annotations — @EnableAutoConfiguration & @Qualifier

---

## Part 1 — @EnableAutoConfiguration

### The Problem First

In a traditional Spring application, you had to manually configure everything:

```xml
<!-- applicationContext.xml — old way -->
<bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource">
    <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
    <property name="url" value="jdbc:mysql://localhost/mydb"/>
</bean>

<bean id="entityManagerFactory" class="...LocalContainerEntityManagerFactoryBean">
    ...
</bean>

<bean id="transactionManager" class="...JpaTransactionManager">
    ...
</bean>
```

Hundreds of lines of XML just to get a DB connection working.
Every new project starts with the same boilerplate. Painful.

---

### What @EnableAutoConfiguration does

`@EnableAutoConfiguration` tells Spring Boot:

> "Look at what's on the classpath, what beans are already defined,
> and **automatically configure** the application based on what you find."

```java
@EnableAutoConfiguration
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

Spring Boot sees `mysql-connector-java` on the classpath
→ automatically creates a `DataSource` bean for you.

Spring Boot sees `spring-data-jpa` on the classpath
→ automatically creates `EntityManagerFactory`, `TransactionManager` etc.

Spring Boot sees `spring-web` on the classpath
→ automatically configures `DispatcherServlet`, `Jackson`, etc.

**You get a fully working application with zero XML and zero manual bean setup.**

---

### How it works internally

`@EnableAutoConfiguration` imports `AutoConfigurationImportSelector` which:

1. Reads a file called `spring.factories` (or `AutoConfiguration.imports` in Spring Boot 3+)
   inside every JAR on the classpath
2. That file lists all auto-configuration classes for that library
3. Each auto-configuration class uses `@Conditional` annotations to decide
   whether to activate itself

```
spring-boot-autoconfigure.jar
└── META-INF/
    └── spring/
        └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
            ├── DataSourceAutoConfiguration        ← activates if DB driver on classpath
            ├── JpaRepositoriesAutoConfiguration   ← activates if JPA on classpath
            ├── WebMvcAutoConfiguration            ← activates if spring-web on classpath
            ├── SecurityAutoConfiguration          ← activates if spring-security on classpath
            └── ... 140+ more auto-configurations
```

Each auto-config class checks conditions before activating:

```java
@AutoConfiguration
@ConditionalOnClass(DataSource.class)           // only if DataSource class exists on classpath
@ConditionalOnMissingBean(DataSource.class)     // only if YOU haven't defined your own DataSource
public class DataSourceAutoConfiguration {

    @Bean
    public DataSource dataSource() {
        // creates DataSource from application.properties values
        return DataSourceBuilder.create()
                .url(env.getProperty("spring.datasource.url"))
                .username(env.getProperty("spring.datasource.username"))
                .build();
    }
}
```

**Key rule:** Auto-configuration BACKS OFF if you define your own bean.
Your explicit beans always take priority. ✅

---

### @SpringBootApplication = 3 annotations in one

In practice, you never write `@EnableAutoConfiguration` alone.
`@SpringBootApplication` already includes it:

```java
// This one annotation:
@SpringBootApplication

// is equivalent to all three:
@SpringBootConfiguration      // marks this as a configuration class
@EnableAutoConfiguration      // enables auto-configuration
@ComponentScan                // scans for @Component, @Service, @Repository etc.
```

So when you write:
```java
@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}
```

`@EnableAutoConfiguration` is already active.

---

### Excluding specific auto-configurations

Sometimes you don't want a particular auto-configuration:

```java
// Exclude specific auto-config
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MyApp { }
```

Or in `application.properties`:
```properties
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
```

**When useful:** Testing without a DB, or when you want full manual control over a bean.

---

### See what was auto-configured

Add this to `application.properties` to see all auto-configuration decisions at startup:

```properties
logging.level.org.springframework.boot.autoconfigure=DEBUG
```

Output shows which auto-configs were applied and which were skipped (and why):
```
DataSourceAutoConfiguration     POSITIVE (matched — mysql driver found)
SecurityAutoConfiguration        POSITIVE (matched — spring-security found)
MongoAutoConfiguration           NEGATIVE (did not match — no MongoDB on classpath)
```

---
---

## Part 2 — @Qualifier

### The Problem First

Spring's dependency injection works by type.
When you `@Autowired` a bean, Spring finds the matching type and injects it.

But what happens when there are **multiple beans of the same type**?

```java
@Component
public class UpiPayment implements PaymentService { ... }

@Component
public class CreditCardPayment implements PaymentService { ... }

@Component
public class OrderService {

    @Autowired
    private PaymentService paymentService;  // ❌ Which one? UPI or CreditCard?
}
```

Spring throws:
```
NoUniqueBeanDefinitionException:
expected single matching bean but found 2: upiPayment, creditCardPayment
```

**`@Qualifier` solves this — it tells Spring exactly which bean to inject.**

---

### What @Qualifier does

`@Qualifier` specifies the **bean name** to inject when multiple candidates exist.

```java
@Component("upiPayment")           // explicit bean name
public class UpiPayment implements PaymentService { ... }

@Component("creditCardPayment")    // explicit bean name
public class CreditCardPayment implements PaymentService { ... }

@Component
public class OrderService {

    @Autowired
    @Qualifier("upiPayment")       // inject specifically this one ✅
    private PaymentService paymentService;
}
```

---

### Using @Qualifier in constructor injection (recommended)

Constructor injection is preferred over field injection:

```java
@Component
public class OrderService {

    private final PaymentService paymentService;

    @Autowired
    public OrderService(@Qualifier("creditCardPayment") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

---

### Using @Qualifier with @Bean

When defining beans in a `@Configuration` class:

```java
@Configuration
public class PaymentConfig {

    @Bean("upiPayment")
    public PaymentService upiPayment() {
        return new UpiPayment();
    }

    @Bean("creditCardPayment")
    public PaymentService creditCardPayment() {
        return new CreditCardPayment();
    }
}

@Service
public class CheckoutService {

    @Autowired
    @Qualifier("upiPayment")
    private PaymentService paymentService;
}
```

---

### @Qualifier vs @Primary

These two solve the same problem differently:

```java
// @Primary — marks one bean as the DEFAULT when no qualifier specified
@Component
@Primary
public class UpiPayment implements PaymentService { }

@Component
public class CreditCardPayment implements PaymentService { }

@Autowired
PaymentService paymentService;  // gets UpiPayment (primary) ✅

@Autowired
@Qualifier("creditCardPayment")
PaymentService paymentService;  // gets CreditCardPayment (qualifier overrides primary) ✅
```

| | `@Primary` | `@Qualifier` |
|--|------------|-------------|
| **What it does** | Marks one bean as default | Picks a specific bean by name |
| **Where it goes** | On the bean definition | On the injection point |
| **When to use** | One bean is used 80% of the time | Need precise control at each injection |
| **Overrides** | Used when no qualifier specified | Always wins over @Primary |

**Rule of thumb:**
- Use `@Primary` when one implementation is the standard choice
- Use `@Qualifier` when different parts of the app need different implementations

---

### Custom @Qualifier annotation (cleaner approach)

Using string names like `@Qualifier("upiPayment")` is fragile — typos cause runtime errors.
A custom qualifier annotation is type-safe:

```java
// Define custom qualifier
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface UpiPaymentQualifier { }

// Apply on the bean
@Component
@UpiPaymentQualifier
public class UpiPayment implements PaymentService { }

// Use at injection point — no strings, compile-time safe ✅
@Autowired
@UpiPaymentQualifier
private PaymentService paymentService;
```

If you rename the bean, the compiler catches it immediately.
With `@Qualifier("upiPayment")`, a typo fails only at runtime.

---

### @Qualifier with @Value — Different use

`@Qualifier` also works alongside `@Value` when injecting from properties:

```java
// Not commonly needed, but @Qualifier can also distinguish
// between multiple instances of the same config type
@Bean
@Qualifier("primaryDb")
public DataSource primaryDataSource() { ... }

@Bean
@Qualifier("replicaDb")
public DataSource replicaDataSource() { ... }

@Service
public class ReportService {

    @Autowired
    @Qualifier("replicaDb")   // use read replica for reports
    private DataSource dataSource;
}
```

---

## Summary

### @EnableAutoConfiguration

```
What:   Activates Spring Boot's auto-configuration mechanism.

How:    Scans spring.factories / AutoConfiguration.imports in all JARs
        → checks @Conditional annotations
        → creates beans automatically if conditions match
        → backs off if you define your own bean

In practice:
        @SpringBootApplication already includes it — you never write it alone.

Key points:
  - Your beans always override auto-configured beans
  - Use exclude = {} to turn off specific auto-configs
  - Enable DEBUG logging to see what was auto-configured and why
```

### @Qualifier

```
What:   Resolves ambiguity when multiple beans of the same type exist.
        Tells Spring exactly which bean to inject.

When:   NoUniqueBeanDefinitionException — multiple beans, same type

Options:
  @Qualifier("beanName")       → inject by name (string — fragile)
  @Primary                     → mark one as default (no qualifier needed)
  Custom @Qualifier annotation → type-safe, compile-time checked (best)

Injection styles (all work):
  Field injection      → @Autowired @Qualifier("x")
  Constructor injection → @Autowired MyService(@Qualifier("x") Dep dep)
  Setter injection     → @Autowired @Qualifier("x") void setDep(Dep dep)
```

---

## Quick Reference

```java
// @EnableAutoConfiguration — already inside @SpringBootApplication
@SpringBootApplication
public class MyApp {
    public static void main(String[] args) {
        SpringApplication.run(MyApp.class, args);
    }
}

// Exclude something you don't want auto-configured
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)

// @Qualifier — resolve multiple beans of same type
@Component("upiPayment")
public class UpiPayment implements PaymentService { }

@Component("creditCardPayment")
public class CreditCardPayment implements PaymentService { }

@Service
public class OrderService {
    @Autowired
    @Qualifier("upiPayment")
    private PaymentService paymentService;
}

// @Primary — simpler when one bean is the default
@Component
@Primary
public class UpiPayment implements PaymentService { }
```
