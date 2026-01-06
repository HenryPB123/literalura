package com.henryPB.literalura.model;
import com.henryPB.literalura.dto.DatosLibro;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;

    @ManyToMany( fetch = FetchType.EAGER)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;
    private List<String> idiomas;
    private Integer totalDescargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();

        this.autores = new ArrayList<>();
        this.idiomas = datosLibro.idiomas();
        this.totalDescargas = datosLibro.totalDescargas();
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getTotalDescargas() {
        return totalDescargas;
    }

    public void setTotalDescargas(Integer totalDescargas) {
        this.totalDescargas = totalDescargas;
    }

    private String invertirNombreAutor(String nombre){
        if(nombre == null || !nombre.contains(",")){
            return nombre;
        }

        String[] partes = nombre.split(",", 2);
        String apellidos = partes[0].trim();
        String nombres = partes[1].trim();

        return nombres + " " + apellidos;
    }


    @Override
    public String toString() {
        String autoresString = autores.stream()
                .map(a -> invertirNombreAutor(a.getNombreAutor()))
                .collect(Collectors.joining(", "));

        return """
                {
                titulo: %s
                autores: [%s]
                idiomas: %s
                totalDescargas: %d
                }
                """.formatted(
                titulo,
                autoresString,
                idiomas,
                totalDescargas
        );

    }


}
