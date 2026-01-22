package com.uniportal.User;

import com.uniportal.Enums.AcademicTitle;
import com.uniportal.Enums.DepartmentName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

    @Enumerated(EnumType.STRING)
    private AcademicTitle academicTitle;
    @Enumerated(EnumType.STRING)
    private DepartmentName department;

    public Teacher(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AcademicTitle academicTitle,
                   DepartmentName department) {
        super(firstName, lastName,email, password);
        this.academicTitle = academicTitle;
        this.department = department;
    }
}
