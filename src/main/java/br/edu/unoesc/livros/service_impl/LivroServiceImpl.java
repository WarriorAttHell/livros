package br.edu.unoesc.livros.service_impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.edu.unoesc.livros.dto.LivroDTO;
import br.edu.unoesc.livros.model.Livro;
import br.edu.unoesc.livros.repository.LivroRepository;
import br.edu.unoesc.livros.service.LivroService;

@Service
public class LivroServiceImpl implements LivroService {
	@Autowired
	private LivroRepository repositorio;
	
	@Override
	public void popularTabelaInicial() {
		repositorio.saveAll(List.of(
				new Livro(null, "O hobbit 1", 1, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 2", 2, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 3", 3, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 4", 4, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 5", 5, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 6", 6, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 7", 7, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 8", 8, "J.R.R.Tolkien"),
				new Livro(null, "O hobbit 9", 9, "J.R.R.Tolkien"),				
				new Livro(null, "O hobbit 10", 10, "J.R.R.Tolkien"),			
				new Livro(null, "O hobbit 11", 11, "J.R.R.Tolkien"),			
				new Livro(null, "O hobbit 12", 12, "J.R.R.Tolkien"),			
				new Livro(null, "O hobbit 13", 13, "J.R.R.Tolkien"),			
				new Livro(null, "O hobbit 14", 14, "J.R.R.Tolkien")		
			)
		);
		
		Livro l = new Livro(null, "O Senhor dos anéis", 42, "Tolkien");
		l = repositorio.save(l);
	}

	@Override
	public Livro incluir(Livro livro) {
		livro.setId(null);
		return repositorio.save(livro);
	}

	@Override
	public Livro alterar(Long id, Livro livro) {
		var l = repositorio.findById(id)
						   .orElseThrow(
								   () -> new ObjectNotFoundException("Livro não encontrado! Id: "
										   + id + ", Tipo: " + Livro.class.getName(), null)
						   );
		
		// Atualiza os dados do banco com os dados vindos da requisição
		l.setTitulo(livro.getTitulo());
		l.setAutor(livro.getAutor());
		l.setPaginas(livro.getPaginas());
		
		return repositorio.save(l);
	}

	@Override
	public void excluir(Long id) {
		if (repositorio.existsById(id)) {
			repositorio.deleteById(id);
		} else {
			throw new ObjectNotFoundException("Livro não encontrado! Id: "
					   						  + id + ", Tipo: " + Livro.class.getName(), null);
		}
	}

	@Override
	public List<Livro> listar() {
		List<Livro> livros = new ArrayList<Livro>();
		
		// Recupera todos os registros da tabela
		Iterable<Livro> itens = repositorio.findAll();
		
		// Cria uma cópia dos dados na lista 'livros'
		itens.forEach(livros::add);
		
		return livros;
	}

	@Override
	public Livro buscar(Long id) {
		Optional<Livro> obj = repositorio.findById(id);
				
		return obj.orElseThrow(
						() -> new ObjectNotFoundException("Livro não encontrado! Id: "
					  		+ id + ", Tipo: " + Livro.class.getName(), null)
					);
	}

	@Override
	public Livro buscarPorId(Long id) {
		return repositorio.findById(id).orElse(new Livro());					      
	}

	@Override
	public Optional<Livro> porId(Long id) {
		return repositorio.findById(id);
	}

	@Override
	public List<Livro> buscarPorTitulo(String titulo) {
		return repositorio.findByFiltro(titulo);
	}

	@Override
	public List<Livro> buscarPorAutor(String autor) {
		return repositorio.findByAutorContainingIgnoreCase(autor);
	}

	@Override
	public List<Livro> buscarPorQtdPaginas(Integer qtdPaginas) {
		return repositorio.porQtdPaginas(qtdPaginas);
	}

	@Override
	public Page<LivroDTO> listarPaginado(Pageable pagina) {
		Page<Livro> lista = repositorio.findAll(pagina);
		
		Page<LivroDTO> listaDTO = lista.map(livro -> new LivroDTO(livro));
		
		return listaDTO;
	}

	@Override
	public Page<Livro> buscaPaginadaPorTitulo(String titulo, Integer pagina, Integer tamPagina, String campo, String direcao) {
		PageRequest reqPagina = PageRequest.of(pagina, 
											   tamPagina, 
											   Sort.by(Direction.valueOf(direcao), campo)
								);
		Page<Livro> livros = repositorio.findByTituloContainingIgnoreCase(titulo, reqPagina);
		
		return livros;
	}

	@Override
	public Livro fromDTO(LivroDTO livroDTO) {
		return new Livro(livroDTO.getId(),
						 livroDTO.getTitulo(),
						 livroDTO.getPaginas(),
						 livroDTO.getAutor()
					);
	}
}
