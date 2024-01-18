package com.elhg.inventario.repository;

import com.elhg.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InventarioRepository extends JpaRepository<Inventario,Long> {
    List<Inventario> findByCodigoSkuIn(List<String> codigoSku);

}
