package com.example.demo.models;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode(of = "value")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Age {

    @Column(name = "age")
    private Integer value;

    public Age(int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }

        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
