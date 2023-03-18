package com.example.demo;

import com.example.demo.models.Age;
import com.example.demo.models.Gender;
import com.example.demo.models.Item;
import com.example.demo.models.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JpaTest {

    private static EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;

    @BeforeAll
    static void createEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("demo");
    }

    @AfterAll
    static void closeEntityManagerFactory() {
        entityManagerFactory.close();
    }

    @BeforeEach
    void setUp() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterEach
    void tearDown() {
        entityManager.close();
    }

    @Test
    void query() {
        Person person = entityManager.find(Person.class, "견우");

        System.out.println("*".repeat(80));
        System.out.println(person);
        System.out.println("*".repeat(80));
    }

    @Test
    void crud() {
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        // Entity 생성 및 확인
        Person person = new Person("Mr.Big", new Age(35), Gender.male());
        entityManager.persist(person);
        transaction.commit();

        transaction.begin();
        Person found = entityManager.find(Person.class, "Mr.Big");
        assertEquals(person, found);
        transaction.commit();

        transaction.begin();
        // 다른 트랜잭션에 가서 또 확인!
        person = entityManager.find(Person.class, "Mr.Big");
        assertEquals("Mr.Big", person.getName());
        transaction.commit();

        transaction.begin();
        // Entity의 상태를 바꾸면 commit했을 때 자동으로 UPDATE 수행.
        person = entityManager.find(Person.class, "Mr.Big");
        person.setAge(new Age(30));
        transaction.commit();

        transaction.begin();
        // 다른 트랜잭션에 가서 또 확인!
        person = entityManager.find(Person.class, "Mr.Big");
        assertEquals(new Age(30), person.getAge());
        transaction.commit();

        transaction.begin();
        // Entity 삭제 및 확인
        person = entityManager.find(Person.class, "Mr.Big");
        entityManager.remove(person);
        transaction.commit();

        transaction.begin();
        found = entityManager.find(Person.class, "Mr.Big");
        assertNull(found);
        transaction.commit();

        transaction.begin();
        // 다른 트랜잭션에 가서 또 확인!
        person = entityManager.find(Person.class, "Mr.Big");
        assertNull(person);
        transaction.commit();
    }

    @Test
    void queryAll() {
        String jpql = "SELECT person FROM Person person";

        // 타입을 지정하지 않으면 Query, 지정하면 TypedQuery.
        List<Person> people = entityManager.createQuery(jpql, Person.class)
                .getResultList();

        assertEquals(2, people.size());
    }

    @Test
    void createAndRemoveItem() {
        EntityTransaction transaction = entityManager.getTransaction();

        transaction.begin();
        Person person = entityManager.find(Person.class, "견우");
        person.addItem("Z", "이건 끝이야");

        List<Item> items = person.getItems();
        System.out.println("*".repeat(80));
        items.forEach(System.out::println);
        System.out.println("*".repeat(80));

        transaction.commit();

        transaction.begin();
        person = entityManager.find(Person.class, "견우");
        assertEquals(3, person.getItems().size());
        person.removeItem("Z");

        items = person.getItems();
        System.out.println("*".repeat(80));
        items.forEach(System.out::println);
        System.out.println("*".repeat(80));

        transaction.commit();

        transaction.begin();
        person = entityManager.find(Person.class, "견우");
        assertEquals(2, person.getItems().size());
        transaction.commit();
    }

}
