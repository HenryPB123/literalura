package com.henryPB.literalura.util;

import com.henryPB.literalura.dto.DatosApi;
import com.henryPB.literalura.dto.DatosAutor;
import com.henryPB.literalura.dto.DatosLibro;
import com.henryPB.literalura.model.Autor;
import com.henryPB.literalura.model.Libro;
import com.henryPB.literalura.repository.AutorRepository;
import com.henryPB.literalura.repository.LibroRepository;
import com.henryPB.literalura.service.ConsumoApi;
import com.henryPB.literalura.service.ConvierteDatos;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class EjecutaOpcionesDeMenu {

    // Cargar .env
    private static final Dotenv dotenv = Dotenv.configure()
            .directory("./") // Ajusta esto si tu .env no está en la raíz
            .filename(".env")
            .load();
    private final String URL_BASE = dotenv.get("URL_BASE");
    private final ConvierteDatos conversor = new ConvierteDatos();
    @Autowired
    private final AutorRepository autorRepository;
    private final LibroRepository libroRepository;
    private final Scanner scanner = new Scanner(System.in);
    private final ValidarInput validador = new ValidarInput();
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final Menus menu = new Menus();

    @Autowired
    public EjecutaOpcionesDeMenu(AutorRepository autorRepository, LibroRepository libroRepository){
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }


    public void ejecutarOpcion(int opcion){
        switch (opcion){
            case 1 -> {
                libroPorNombre();
            }
            case 2 -> {
                mostrarTodosLosLibros();
            }
            case 3 -> {
                mostrarTodosLosAutores();
            }
            case 4 -> {
                mostrarActoresVivosEnAnio();
            }
            case 5 -> {
                mostrarLibrosPorIdioma();
            }
            case 0 -> System.out.println("Cerrando la aplicación...");
            default -> System.out.println("Opción inválida");
        }
    }

    private void libroPorNombre() {

        System.out.println("Escribe el nombre del libro que deseas buscar");
        String nombreLibro = validador.validarInputVacio(scanner);

        var json = consumoApi.obtenerDatos(
                URL_BASE + "?search=" + nombreLibro.replace(" ", "%20")
        );

        var datos = conversor.convertirDatos(json, DatosApi.class);

        if (datos == null || datos.libros().isEmpty()) {
            System.out.println("Libro no encontrado!\n");
            return;
        }

        //Tomamos el primer resultado de la API
        DatosLibro datosLibro = datos.libros().get(0);
        // VALIDACIÓN CLAVE: ¿YA EXISTE EL LIBRO?
        if(libroExiste(datosLibro)) return;

       // Creamos el libro SOLO PARA MOSTRAR (NO se guarda aún)
        crearLibro(datosLibro);

       // Mostrar menú confirmación
        menu.menuConfirmacionIngresoDB();

        String respuesta = validador.validarInputVacio(scanner);
        int opcion = validador.validarInputNumero(respuesta);

        // SOLO si el usuario escogió que SÍ en menuConfirmacionIngresoDB → se guarda
        guardaDataEnDb(opcion, datosLibro);
    }

    private boolean libroExiste(DatosLibro datosLibro){
        // VALIDACIÓN CLAVE: ¿YA EXISTE EL LIBRO?
        var libroExistente = libroRepository.findByTituloIgnoreCase(datosLibro.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("\nEl libro ya existe en la base de datos:");
            System.out.println(libroExistente.get());
            return true; //  No continúa, no guarda nada
        }
        return false;
    }

    private void crearLibro(DatosLibro datosLibro){
        // Creamos el libro SOLO PARA MOSTRAR (NO se guarda aún)
        Libro libroPreview = new Libro(datosLibro);

        // Creamos autores TEMPORALES solo para visualización
        List<Autor> autoresPreview = new ArrayList<>();
        for (DatosAutor datosAutor : datosLibro.autores()) {
            autoresPreview.add(new Autor(datosAutor));
        }

        libroPreview.setAutores(autoresPreview);

        // Mostramos la información al usuario
        System.out.println("\nLibro encontrado: " + libroPreview);
    }

    private void guardaDataEnDb(int opcion, DatosLibro datosLibro){
        if (opcion == 1) {

            // Lista FINAL de autores gestionados por JPA
            List<Autor> autoresPersistidos = new ArrayList<>();

            for (DatosAutor datosAutor : datosLibro.autores()) {
                // Buscamos el autor en BD
                Autor autor = autorRepository
                        .findByNombreAutor(datosAutor.nombreAutor())
                        .orElseGet(() -> {
                            // Solo aquí se crea y guarda si NO existe
                            Autor nuevoAutor = new Autor(datosAutor);
                            return autorRepository.save(nuevoAutor);
                        });

                autoresPersistidos.add(autor);
            }

            // Creamos el libro REAL que sí se persistirá
            Libro libro = new Libro(datosLibro);
            libro.setAutores(autoresPersistidos);

            libroRepository.save(libro);

            System.out.println("\nLibro guardado con éxito.\n");

        } else {
            System.out.println("\nOperación cancelada. No se guardó nada.\n");
        }
    }

    public void mostrarTodosLosLibros(){
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()){
            System.out.println("\nNo hay libros disponibles");
            return;
        }
        libros.forEach(libro -> {
            System.out.println("********** LIBRO **********");
            System.out.println(libro);
        });
    }

    public void mostrarTodosLosAutores(){
        List<Autor> autores = autorRepository.findAll();
        if (autores.isEmpty()){
            System.out.println("\nNo hay autores disponibles");
            return;
        }
        autores.forEach(autor -> {
            System.out.println("********** AUTOR **********");
            System.out.println(autor);
        });
    }

    private void mostrarActoresVivosEnAnio(){
        System.out.println("Ingresa el año que deseas consultar:");
        int anio = validador.validarInputNumero(validador.validarInputVacio(scanner));

        List<Autor> autores = autorRepository.autoresVivosEnAnio(anio);
        if (autores.isEmpty()){
            System.out.println("\nNo se encontraron autores vivos en el año " + anio + "!\n");
            return;
        }
        System.out.println("\n**** AUTORES VIVOS DEL AÑO " + anio + " ****");
        autores.forEach(autor -> {
            System.out.println("********** AUTOR **********");
            System.out.println(autor);
        });
    }

    public void mostrarLibrosPorIdioma(){
        menu.menuIdiomas();
        int opcion = validador.validarInputNumero(validador.validarInputVacio(scanner));
        String idioma = "";
        switch (opcion){
            case 1 -> idioma = "es";
            case 2 -> idioma = "en";
            case 3 -> idioma = "fr";
            case 4 -> idioma = "pt";
            case 5 -> idioma = "fi";
            case 0 -> {
                return;
            }
            default -> System.out.println("Esta opción no está disponible!\n");
        }

        List<Libro> libros = libroRepository.buscarLibrosPorIdioma(idioma);
        if (libros.isEmpty()){
            System.out.println("\nNo hay libros disponibles en este idioma!\n");
        }
        libros.forEach(libro -> {
            System.out.println("********** LIBRO **********");
            System.out.println(libro);
        });
    }

}
