package com.henryPB.literalura.util;

public class Menus {

    public void menuPrincipal(){
        System.out.println("""
                ************** BIENVENIDO A LITERALURA, TU APLICACIÓN DE LIBROS **************
                ------------------------------------------------------------------------------
                Digita el número de la acción que deseas realizar:
                
                1) - Buscar libro por título
                2) - Listar libros registrados
                3) - Listar autores registrados
                4) - Listar autores vivos en un año determinado
                5) - Listar libros por idioma
                
                0) - SALIR
                """);
    }

    public void menuConfirmacionIngresoDB(){
        System.out.println("""
            ¿Es este el libro que buscabas?
            ¿Deseas guardarlo en la base de datos?
            
            1) - SI
            2) - NO
            """);
    }

    public void menuIdiomas(){
        System.out.println("""
                Elije la opción del idioma de los libros que deseas ver:
                
                1) - Español - es
                2) - Inglés - en
                3) - Francés - fr
                4) - Portugués - pt
                5) - Finlandés - fi
                
                0) - Volver a Menú Principal
                """);
    }
}
