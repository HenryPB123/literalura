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
            case 2 -> System.out.println("Listar Libros");
            case 3 -> System.out.println("Listar Autores");
            case 4 -> System.out.println("Listar autores vivos");
            case 5 -> System.out.println("Listar libros por idioma");
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

        // 1️⃣ Tomamos el primer resultado de la API
        DatosLibro datosLibro = datos.libros().get(0);

        //FALLANDO
        // 🔴 VALIDACIÓN CLAVE: ¿YA EXISTE EL LIBRO?
        var libroExistente = libroRepository.findByTituloIgnoreCase(datosLibro.titulo());

        if (libroExistente.isPresent()) {
            System.out.println("⚠️ El libro ya existe en la base de datos:");
            System.out.println(libroExistente.get());
            return; // 🚫 No continúa, no guarda nada
        }

        // 2️⃣ Creamos el libro SOLO PARA MOSTRAR (NO se guarda aún)
        Libro libroPreview = new Libro(datosLibro);

        // 3️⃣ Creamos autores TEMPORALES solo para visualización
        List<Autor> autoresPreview = new ArrayList<>();
        for (DatosAutor datosAutor : datosLibro.autores()) {
            autoresPreview.add(new Autor(datosAutor));
        }

        libroPreview.setAutores(autoresPreview);

        // 4️⃣ Mostramos la información al usuario
        System.out.println("Libro encontrado: " + libroPreview);
        System.out.println("""
            ¿Es este el libro que buscabas?
            ¿Deseas guardarlo en la base de datos?
            
            1) - SI
            2) - NO
            """);

        String respuesta = validador.validarInputVacio(scanner);
        int opcion = validador.validarInputNumero(respuesta);

        // 5️⃣ SOLO si el usuario dice que SÍ → se guarda
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

            System.out.println("Libro guardado con éxito.\n");

        } else {
            System.out.println("Operación cancelada. No se guardó nada.\n");
        }
    }

//    private void libroPorNombre(){
//        System.out.println("Escribe el nombre del libro que deseas buscar");
//        String nombreLibro = validador.validarInputVacio(scanner);
//        var json = consumoApi.obtenerDatos(
//                URL_BASE + "?search=" + nombreLibro.replace(" ", "%20")
//        );
//
//        var datos = conversor.convertirDatos(json, DatosApi.class);
//        if (datos == null || datos.libros().isEmpty()) {
//            System.out.println("Libro no encontrado!\n");
//            return;
//        }
//
//        // Tomamos el primer resultado
//        DatosLibro datosLibro = datos.libros().get(0);
//
//        // --- MANEJO DEL AUTOR ---
//        List<Autor> autoresLibro = new ArrayList<>();
//        for(DatosAutor datosAutor: datosLibro.autores()){
//            Autor autor = autorRepository
//                    .findByNombreAutor(datosAutor.nombreAutor())
//                    .orElseGet(()->{
//                        Autor autorNuevo = new Autor(datosAutor);
//                        return autorRepository.save(autorNuevo);
//                    });
//            autoresLibro.add(autor);
//        }
//        Libro libro = new Libro(datosLibro);
//        libro.setAutores(autoresLibro);
//
//        System.out.println("Libro encontrado: " + libro);
//        System.out.println("""
//                ¿Es este el libro que buscabas?
//                ¿Deseas guardarlo en la base de datos?
//
//                1) - SI
//                2) - NO
//                """);
//        String respuesta = validador.validarInputVacio(scanner);
//        int opcion = validador.validarInputNumero(respuesta);
//        switch (opcion){
//            case 1:
//                libroRepository.save(libro);
//                System.out.println("Libro guardado con éxito.\n");
//                break;
//            case 2:
//                System.out.println("Buscar otro libro\n");
//                break;
//            default:
//                System.out.println("Opción no válida\n");
//        }
//    }
}
