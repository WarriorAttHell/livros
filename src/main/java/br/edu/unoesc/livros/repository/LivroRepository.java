package br.edu.unoesc.livros.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.unoesc.livros.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
	public List<Livro> findByAutorContainingIgnoreCase(String autor);
	
	public Page<Livro> findByTituloContainingIgnoreCase(String titulo, Pageable pagina);
	
	@Query("Select l from Livro l where l.paginas >= :paginas")
	public List<Livro> porQtdPaginas(@Param("paginas") Integer paginas);
	
	@Query("Select l from Livro l where upper(l.titulo) like upper(concat('%', :filtro, '%')) order by titulo")
	public List<Livro> findByFiltro(@Param("filtro") String filtro);
}