package com.uniportal.Course;


import com.uniportal.Enums.Day;
import com.uniportal.User.Teacher;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "con_day")
    private Day con_day;

    private String startTime;
    private String endTime;
    private String room;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
