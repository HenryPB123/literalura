package com.henryPB.literalura.util;

import java.util.Scanner;

public class ValidarInput {


    public String validarInputVacio( Scanner scanner){
        while (true){
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("El campo está vacio, por favor ingresa el dato requerido!");
            }else {
                return entrada;
            }
        }

    }

    public int validarInputNumero( String entrada){
           try {
               return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
               throw new IllegalArgumentException("El dato requerido debe ser un número!");
           }
    }
}
