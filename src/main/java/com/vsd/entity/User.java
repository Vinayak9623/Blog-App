package com.vsd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.vsd.entity.Role.ROLE_GUEST;

@Entity
@Table(name = "user")
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;
    private boolean isActive=true;
    @Enumerated(EnumType.STRING)
    private Role role=ROLE_GUEST;
    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Article> articles=new ArrayList<>();

    @Override
    public boolean equals(Object o){
        if(this==o) return  true;
        if(o==null || getClass()!=o.getClass()) return false;
        User user=(User) o;
        return Objects.equals(id,user.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
}
