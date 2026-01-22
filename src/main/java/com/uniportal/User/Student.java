package com.uniportal.User;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Student extends User{
    private String indexNumber;

    public Student(String firstName, String lastName,String email, String password, String indexNumber) {
        super(firstName, lastName,email, password);
        this.indexNumber = indexNumber;
    }
}
