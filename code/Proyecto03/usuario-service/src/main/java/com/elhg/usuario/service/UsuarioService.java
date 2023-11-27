package com.elhg.usuario.service;

import com.elhg.usuario.entities.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario saveUsuario(Usuario usuario);

    List<Usuario> getAllUsuarios();

    Usuario getUsuario(String usuarioId);
}
