package com.elhg.usuario.repository;

import com.elhg.usuario.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,String> {
}
