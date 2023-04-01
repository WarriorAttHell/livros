package br.edu.unoesc.livros.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.edu.unoesc.livros.model.Livro;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
public class LivroDTO implements Serializable {
	Long id;
	
	@NotBlank(message = "É obrigatório informar um título")
	@Size(min = 1, max = 100, message = "O título do livro deve ter entre {min} e {max} caracteres")
	String titulo;
	
	@NotNull(message = "É obrigatório informar o número de páginas do livro")
	@Min(value = 1, message = "O livro deve ter pelo menos 1 página")
	@Max(value = 10000, message = "O livro pode ter, no máximo, 10.000 páginas")
	Integer paginas;
	
	@NotBlank(message = "É obrigatório informar o nome do autor")
	@Size(min = 3, max = 100, message = "O nome do autor deve ter entre {min} e {max} caracteres")
	String autor;
	
	public LivroDTO(Livro livro) {
		this.id = livro.getId();
		this.titulo = livro.getTitulo();
		this.paginas = livro.getPaginas();
		this.autor = livro.getAutor();
	}
}