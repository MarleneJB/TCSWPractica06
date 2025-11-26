/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.TCSWPractica06.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.uv.TCSWPractica06.entities.DetalleVenta;

/**
 *
 * @author meli
 */

public interface RepositoryDetalleVenta extends JpaRepository<DetalleVenta, Long> {

}
