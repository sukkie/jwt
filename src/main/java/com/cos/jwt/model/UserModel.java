package com.cos.jwt.model;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
public class UserModel {

    @Id //pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) //프로젝트에 연결된 디비의 넘버링 전략을 사용
    private long id; // sequence, auto_increment

//    @Column(nullable = false, length = 100, unique = true)
    private String username;

//    @Column(nullable = false, length = 100)
    private String password;

//    @Enumerated(EnumType.STRING)
    private String roles; // USER, ADMIN

    public List<String> getRoleList() {
        if (this.roles.length() > 0)  {
            return Arrays.asList(this.roles.split(","));
        }

        return new ArrayList<>();
    }

}
