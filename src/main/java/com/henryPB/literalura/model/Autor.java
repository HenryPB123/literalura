package com.henryPB.literalura.model;
import com.henryPB.literalura.dto.DatosAutor;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombreAutor;
    private Integer fechaNacimiento;
    private Integer fechaFallecimiento;

    @ManyToMany(mappedBy = "autores", fetch = FetchType.EAGER)
    private List<Libro> libros;

    public Autor(){}

    public Autor(DatosAutor datosAutor) {
        this.nombreAutor = datosAutor.nombreAutor();
        this.fechaNacimiento = datosAutor.fechaNacimiento();
        this.fechaFallecimiento = datosAutor.fechaFallecimiento();
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaFallecimiento() {
        return fechaFallecimiento;
    }

    public void setFechaFallecimiento(Integer fechaFallecimiento) {
        this.fechaFallecimiento = fechaFallecimiento;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
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

        return """
                {
                nomreAutor: %s
                fechaNacimiento: %d
                fechaFallecimiento: %d
                libros: %s
                }
                """.formatted(
                invertirNombreAutor(nombreAutor),
                fechaNacimiento,
                fechaFallecimiento,
                libros.stream()
                        .map(Libro::getTitulo)
                        .toList()
               );

    }
}
