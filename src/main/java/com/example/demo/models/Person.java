package com.example.demo.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "people")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Person {

    @Id
    private String name;

    @Embedded
    private Age age;

    @Embedded
    private Gender gender;

    @OneToMany(mappedBy = "personName", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "person_name")
    @OrderBy("name")
    private List<Item> items = new ArrayList<>();

    public Person(String name, Age age, Gender gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public void addItem(String name, String usage) {
        Item item = new Item(name, usage, this.name);
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public void removeItem(String name) {
        Optional<Item> item = items.stream()
                .filter(i -> i.getName().equals(name))
                .findFirst();

        if (item.isEmpty()) {
            return;
        }

        items.remove(item.get());
    }

}
