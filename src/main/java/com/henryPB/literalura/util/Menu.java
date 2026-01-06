package com.henryPB.literalura.util;

public class Menu {

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
}
