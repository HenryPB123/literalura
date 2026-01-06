package com.henryPB.literalura.principal;

import com.henryPB.literalura.util.EjecutaOpcionesDeMenu;
import com.henryPB.literalura.util.Menus;
import com.henryPB.literalura.util.ValidarInput;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class Principal {

    private final EjecutaOpcionesDeMenu ejecutaOpcionesDeMenu;
    private final Scanner scanner = new Scanner(System.in);
    private final ValidarInput validador = new ValidarInput();
    private final Menus menu = new Menus();

    public Principal(EjecutaOpcionesDeMenu ejecutaOpcionesDeMenu){
        this.ejecutaOpcionesDeMenu = ejecutaOpcionesDeMenu;
    }

    public void mostrarMenu(){
        int opcion = -1;
        String entrada;

        while (opcion !=0){
            menu.menuPrincipal();
            try {
                entrada = validador.validarInputVacio(scanner);
                opcion = validador.validarInputNumero(entrada);
                ejecutaOpcionesDeMenu.ejecutarOpcion(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
