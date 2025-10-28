package com.moviestats.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.moviestats.dto.PeliculaDTO;
import com.moviestats.model.Genero;
import com.moviestats.model.Pelicula;
import com.moviestats.model.business.IPeliculaBusiness;
import com.moviestats.model.persistence.GeneroRepository;
import com.moviestats.util.IStandardResponseBusiness;
import com.moviestats.util.PeliculaMapper;
import com.moviestats.model.business.exceptions.*;

/**
 * Controller orientado a la vista Admin (endpoints que consume AdminView.vue).
 * Ruta: /api/v1/peliculas
 */
@RestController
@RequestMapping("/api/v1/peliculas")
public class AdminPeliculaController {

    @Autowired
    private IPeliculaBusiness peliculaBusiness;

    @Autowired
    private GeneroRepository generoRepository;

    @Autowired
    private IStandardResponseBusiness response;

    @GetMapping("")
    public ResponseEntity<?> list() {
        try {
            List<Pelicula> list = peliculaBusiness.list();
            List<PeliculaDTO> dto = new ArrayList<>();
            for (Pelicula p : list) {
                dto.add(PeliculaMapper.toDto(p));
            }
            return ResponseEntity.ok(dto);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody PeliculaDTO dto) {
        try {
            Pelicula p = PeliculaMapper.toEntity(dto);

            // resolver/crear genero (si viene)
            if (dto.getGenre() != null && !dto.getGenre().isBlank()) {
                Optional<Genero> gOp = generoRepository.findByNombre(dto.getGenre());
                Genero g;
                if (gOp.isPresent()) {
                    g = gOp.get();
                } else {
                    g = new Genero();
                    g.setNombre(dto.getGenre());
                    generoRepository.save(g);
                }
                p.getGenero().add(g);
            }

            Pelicula created = peliculaBusiness.add(p);
            PeliculaDTO out = PeliculaMapper.toDto(created);
            return ResponseEntity.status(HttpStatus.CREATED).body(out);
        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody PeliculaDTO dto) {
        try {
            Pelicula p = PeliculaMapper.toEntity(dto);

            if (dto.getGenre() != null && !dto.getGenre().isBlank()) {
                Optional<Genero> gOp = generoRepository.findByNombre(dto.getGenre());
                Genero g;
                if (gOp.isPresent()) {
                    g = gOp.get();
                } else {
                    g = new Genero();
                    g.setNombre(dto.getGenre());
                    generoRepository.save(g);
                }
                p.getGenero().add(g);
            }

            Pelicula updated = peliculaBusiness.update(p);
            PeliculaDTO out = PeliculaMapper.toDto(updated);
            return ResponseEntity.ok(out);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (FoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.FOUND, e, e.getMessage()), HttpStatus.FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            peliculaBusiness.delete(id);
            return ResponseEntity.ok().build();
        } catch (NotFoundException e) {
            return new ResponseEntity<>(response.build(HttpStatus.NOT_FOUND, e, e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (BusinessException e) {
            return new ResponseEntity<>(response.build(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}