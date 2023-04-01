package br.edu.unoesc.livros;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.edu.unoesc.livros.model.Livro;
import br.edu.unoesc.livros.service.LivroService;

@SpringBootApplication
public class LivrosApplication {
	@Value("${mensagem}")
	private String mensagem;
	
	@Value("${ambiente}")
	private String ambiente;
	
	public static void main(String[] args) {
		SpringApplication.run(LivrosApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("*")
						.allowedMethods("GET", "POST", "PUT", "DELETE");
			}
		};
	}
	
	@Bean
	CommandLineRunner commandLineRunner(LivroService servico) {
		return args -> {
			System.out.println(mensagem + " " + ambiente);
			
			servico.popularTabelaInicial();

			// Exemplo de tratamento de exceções
			try {
				//System.out.println(10 / 0);
				servico.excluir(20L);			
			} catch (EmptyResultDataAccessException e) {
				System.out.println("\n>>> Erro! Registro não encontrado! <<<\n");
			} catch (RuntimeException e) {
				System.out.println("\n>>> Erro de execução! <<<\n" + e.getMessage());
			}
			
			// Exemplo de utilização da classe Optional
			Optional<Livro> p = servico.porId(20L);
			if (p.isEmpty()) {
				System.out.println("\n>>> Registro não encontrado! <<<\n");
			} else {
				System.out.println(p);				
				System.out.println(p.get());				
				System.out.println(p.get().getTitulo());				
			}
			
			Livro a = servico.buscarPorId(50L);
			a.setTitulo("Em busca dos anéis perdidos");
			a.setPaginas(100);
			a.setAutor("Fulano da Silva");
			
			if (a.getId() == null) {
				servico.incluir(a);
			} else {
				servico.alterar(a.getId(), a);
			}
			
			// Recupera todos os registros
			System.out.println(servico.listar());
			
			// Exemplos dos métodos de busca
			for (var livro: servico.buscarPorAutor("tolkien")) {
				System.out.println(livro);
			}
			
			for (var livro: servico.buscarPorQtdPaginas(10)) {
				System.out.println(livro);
			}
			
			for (var livro: servico.buscarPorTitulo("busca")) {
				System.out.println(livro);
			}
		};
	}
}

