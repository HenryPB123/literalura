package com.henryPB.literalura;

import com.henryPB.literalura.principal.Principal;
import com.henryPB.literalura.repository.LibroRepository;
import com.henryPB.literalura.util.EjecutaOpcionesDeMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final Principal principal;

    public LiteraluraApplication(Principal principal) {
        this.principal = principal;
    }

    public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
      principal.mostrarMenu();
    }
}
