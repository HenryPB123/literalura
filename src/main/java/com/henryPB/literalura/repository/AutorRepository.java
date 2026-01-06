package com.henryPB.literalura.repository;

import com.henryPB.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNombreAutor(String nombreAutor);

    List<Autor> findAll();

    @Query("""
            SELECT a FROM Autor a
            WHERE a.fechaNacimiento <= :anio
            AND (a.fechaFallecimiento IS NULL OR a.fechaFallecimiento >= :anio)
            """)
    List<Autor> autoresVivosEnAnio(int anio);
}
