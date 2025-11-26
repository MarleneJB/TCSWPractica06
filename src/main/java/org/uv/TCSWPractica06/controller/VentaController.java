/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.TCSWPractica06.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.uv.TCSWPractica06.dto.VentaDTO;
import org.uv.TCSWPractica06.entities.DetalleVenta;
import org.uv.TCSWPractica06.repositories.RepositoryVenta;
import org.uv.TCSWPractica06.entities.Venta;
import org.uv.TCSWPractica06.repositories.RepositoryCliente;
import org.uv.TCSWPractica06.repositories.RepositoryDetalleVenta;
import org.uv.TCSWPractica06.repositories.RepositoryProducto;

/**
 *
 * @author meli
 */

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    private final RepositoryVenta ventaRepo;
    private final RepositoryCliente clienteRepo;
    private final RepositoryProducto productoRepo;
    private final RepositoryDetalleVenta detalleRepo;

    public VentaController(RepositoryVenta ventaRepo,
            RepositoryCliente clienteRepo,
            RepositoryProducto productoRepo,
            RepositoryDetalleVenta detalleRepo) {
        this.ventaRepo = ventaRepo;
        this.clienteRepo = clienteRepo;
        this.productoRepo = productoRepo;
        this.detalleRepo = detalleRepo;
    }

    private VentaDTO toDTO(Venta v) {
        VentaDTO dto = new VentaDTO();
        dto.setId(v.getId());
        dto.setFecha(v.getFecha());
        dto.setIdCliente(v.getCliente().getId());
        dto.setMontoTotal(v.getMontoTotal());
        

        dto.setDetalles(
                v.getDetalles()
                        .stream()
                        .collect(Collectors.toList()));

        return dto;
    }

    private Venta toEntity(VentaDTO dto) {
        Venta v = new Venta();
        v.setId(dto.getId());
        v.setFecha(dto.getFecha());

        clienteRepo.findById(dto.getIdCliente()).ifPresent(v::setCliente);

        return v;
    }

    @GetMapping
    public ResponseEntity<List<VentaDTO>> getAll() {
        return ResponseEntity.ok(
                ventaRepo.findAll()
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> getById(@PathVariable Long id) {
        return ventaRepo.findById(id)
                .map(v -> ResponseEntity.ok(toDTO(v)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<VentaDTO> create(@RequestBody VentaDTO dto) {

        Venta venta = toEntity(dto);
        Venta savedVenta = ventaRepo.save(venta);

        List<DetalleVenta> detalles = dto.getDetalles().stream().map(d -> {
            DetalleVenta det = new DetalleVenta();
            det.setVenta(savedVenta);

            productoRepo.findById(d.getProducto().getId()).ifPresent(det::setProducto);

            det.setDescripcion(d.getDescripcion());
            det.setCantidad(d.getCantidad());
            det.setPrecio(d.getPrecio());

            return det;

        }).collect(Collectors.toList());

        detalleRepo.saveAll(detalles);

        savedVenta.setDetalles(detalles);

        return ResponseEntity.ok(toDTO(savedVenta));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> update(@PathVariable Long id, @RequestBody VentaDTO dto) {
        if (!ventaRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        dto.setId(id);
        return create(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!ventaRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        ventaRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
