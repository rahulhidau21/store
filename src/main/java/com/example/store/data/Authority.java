package com.example.store.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "authority")
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    @Id
    private String name;
}
