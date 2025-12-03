/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.uv.TCSWPractica06.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.uv.TCSWPractica06.dto.ProductoDTO;
import org.uv.TCSWPractica06.entities.Producto;
import org.uv.TCSWPractica06.repositories.RepositoryProducto;

/**
 *
 * @author meli
 */

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final RepositoryProducto productoRepository;

    public ProductoController(RepositoryProducto productoRepository) {
        this.productoRepository = productoRepository;
    }

    private ProductoDTO toDTO(Producto p) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(p.getId());
        dto.setDescripcion(p.getDescripcion());
        dto.setPrecio(p.getPrecio());
        return dto;
    }

    private Producto toEntity(ProductoDTO dto) {
        Producto p = new Producto();
        p.setId(dto.getId());
        p.setDescripcion(dto.getDescripcion());
        p.setPrecio(dto.getPrecio());
        return p;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> getAll() {
        return ResponseEntity.ok(
                productoRepository.findAll()
                        .stream()
                        .map(this::toDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> getById(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(p -> ResponseEntity.ok(toDTO(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> create(@RequestBody ProductoDTO dto) {
        Producto saved = productoRepository.save(toEntity(dto));
        return ResponseEntity.ok(toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> update(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        dto.setId(id);
        Producto saved = productoRepository.save(toEntity(dto));
        return ResponseEntity.ok(toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!productoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}