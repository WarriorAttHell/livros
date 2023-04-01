package br.edu.unoesc.livros.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.unoesc.livros.dto.LivroDTO;
import br.edu.unoesc.livros.model.Livro;

public interface LivroService {
	Livro fromDTO(LivroDTO livroDTO);
	
	void popularTabelaInicial();

	Livro incluir(Livro livro);
	Livro alterar(Long id, Livro livro);
	void excluir(Long id);

	List<Livro> listar();
	Page<LivroDTO> listarPaginado(Pageable pagina);
	Page<Livro> buscaPaginadaPorTitulo(String titulo, Integer pagina, Integer tamPagina, String campo, String direcao);
	
	Livro buscar(Long id);		// Lança uma exceção caso o não exista o livro com id procurado
	Livro buscarPorId(Long id); // Retorna um novo objeto Livro caso id não seja encontrado
	Optional<Livro> porId(Long id);
	
	List<Livro> buscarPorTitulo(String titulo);
	List<Livro> buscarPorAutor(String autor);
	List<Livro> buscarPorQtdPaginas(Integer qtdPaginas);
}