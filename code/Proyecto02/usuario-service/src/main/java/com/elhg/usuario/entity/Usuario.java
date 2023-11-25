package com.elhg.usuario.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @Column(name = "id")
    private String usuarioId;

    @Column(name = "nombre",length = 20)
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "informacion")
    private String informacion;


}
