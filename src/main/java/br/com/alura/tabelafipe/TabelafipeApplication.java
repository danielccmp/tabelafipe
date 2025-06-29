package br.com.alura.tabelafipe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.alura.tabelafipe.principal.Principal;

@SpringBootApplication
public class TabelafipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TabelafipeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal();

		principal.exibeMenu();
	}
}
