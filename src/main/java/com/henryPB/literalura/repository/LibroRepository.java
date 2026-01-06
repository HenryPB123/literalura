package com.henryPB.literalura.repository;

import com.henryPB.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    List<Libro> findAll();

    @Query(
            value = "SELECT * FROM libros WHERE :idioma = ANY(idiomas)",
            nativeQuery = true
    )
    List<Libro> buscarLibrosPorIdioma(String idioma);


}
