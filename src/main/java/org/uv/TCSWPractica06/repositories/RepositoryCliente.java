/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.TCSWPractica06.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.uv.TCSWPractica06.entities.Clientes;

/**
 *
 * @author meli
 */

@Repository
public interface RepositoryCliente extends JpaRepository<Clientes, Long> {

}
