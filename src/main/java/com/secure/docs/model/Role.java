package com.secure.docs.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private AppRole roleName;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude
    private Set<User> users= new HashSet<>();

}
