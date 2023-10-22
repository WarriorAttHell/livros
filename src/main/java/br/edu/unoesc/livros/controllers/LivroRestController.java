package br.edu.unoesc.livros.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.unoesc.livros.dto.LivroDTO;
import br.edu.unoesc.livros.model.Livro;
import br.edu.unoesc.livros.service.LivroService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "API REST de Livros")
@ApiResponses(value = {
    @ApiResponse(code = 200, message = "Operação realizada com sucesso"),
    @ApiResponse(code = 401, message = "Não autorizado"),
    @ApiResponse(code = 403, message = "Sem permissão para acessar este recurso"),
    @ApiResponse(code = 404, message = "Funcionário não encontrado"),
    @ApiResponse(code = 500, message = "Foi gerada uma exceção"),
})
@RequestMapping("/api")
public class LivroRestController {
	private Logger logger = Logger.getLogger(LivroRestController.class.getName()); 
	
	@Autowired
	private LivroService servico;
	
	@ApiOperation(value = "Retorna todos os livros")
	@GetMapping
	public Iterable<Livro> listar() {
		return servico.listar();
	}

	@ApiOperation(value = "Retorna uma consulta paginada dos livros")
	@GetMapping("/listar-paginas")
	public ResponseEntity<Page<LivroDTO>> listarPaginado(Pageable pagina) {
		return ResponseEntity.ok(servico.listarPaginado(pagina));
	}
	
	@ApiOperation(value = "Retorna uma consulta paginada dos livros com ordenação e filtro")
	@GetMapping("/paginas")
	public ResponseEntity<Page<Livro>> listarPaginas(
				@RequestParam(value="titulo", defaultValue = "") String titulo,
				@RequestParam(value="pagina", defaultValue = "0") Integer pagina,
				@RequestParam(value="tamPagina", defaultValue = "24") Integer tamPagina,
				@RequestParam(value="ordenacao", defaultValue = "titulo") String campo,
				@RequestParam(value="direcao", defaultValue = "ASC") String direcao,
				ModelMap modelo
			) {
		return ResponseEntity.ok(servico.buscaPaginadaPorTitulo(titulo, pagina, tamPagina, campo, direcao));
	} 
	
	@ApiOperation(value = "Retorna um único livro através do seu 'id'")
	@RequestMapping(value = "/{id}",
				    method = RequestMethod.GET,
				    produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Livro> porId(@PathVariable Long id) {
		Optional<Livro> livro = servico.porId(id);
		
		if (livro.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(livro.get());
	}
	
	@ApiOperation(value = "Retorna em XML um único livro através do seu 'id'")
	@RequestMapping(value = "/xml/{id}", headers = "Accept=application/xml")
	public Livro porIdXML(@PathVariable Long id) {
		return servico.buscar(id);
	}
	
	@ApiOperation(value = "Retorna uma lista de livros que contenham no título uma parte da string (case insensitive) buscada")
	@GetMapping("/find")
	List<Livro> porTitulo(@RequestParam("filtro") String titulo) {
		return servico.buscarPorTitulo(titulo);
	}
	
	@ApiOperation(value = "Retorna uma lista de livros que tenham no mínimo o número de páginas solicitado")
	@GetMapping(value = {"/pagina/{pagina}", "/pagina"})
	public List<Livro> porQdtPaginas(@PathVariable Optional<Integer> pagina) {
		return servico.buscarPorQtdPaginas(pagina.orElse(0));
	}
	
	@ApiOperation(value = "Inclui um livro")
    @PostMapping()
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> incluir(@Valid @RequestBody LivroDTO livroDTO) {
		Livro livro = servico.fromDTO(livroDTO);
        livro = servico.incluir(livro);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        									 .path("/{id}")
        									 .buildAndExpand(livro.getId())
        									 .toUri();
        
        System.out.println(uri.toString());
        
        return ResponseEntity.created(uri).build();
    }
    
    @PostMapping(value = "/xml", 
    		//consumes = MediaType.APPLICATION_XML_VALUE,
    		produces = MediaType.APPLICATION_XML_VALUE)
    public Livro incluirXML(@RequestBody Livro livro) {
        return servico.incluir(livro);
    }
 
    @ApiOperation(value = "Atualiza os dados de um livro")
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable("id") Long id, @RequestBody Livro livro) {
    	if (servico.porId(id).isEmpty()) {
    		return ResponseEntity.notFound().build();
    	}
    	
        return ResponseEntity.ok(servico.alterar(id, livro));
    }
 
    @ApiOperation(value = "Exclui um livro")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable("id") Long id) {
    	try {
    		servico.excluir(id);   		
    	} catch (ObjectNotFoundException e) {
    		System.out.println("Livro não encontrado!");
    		return ResponseEntity.notFound().build();
    	}
    	
    	return ResponseEntity.noContent().build();
    }
}