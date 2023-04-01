package br.edu.unoesc.livros.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.edu.unoesc.livros.dto.LivroDTO;
import br.edu.unoesc.livros.model.Livro;
import br.edu.unoesc.livros.service.LivroService;
import br.edu.unoesc.livros.validator.LivroDTOValidator;

@Controller
@RequestMapping("/livros")
public class LivroController {
	@Autowired
	private LivroService servico;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new LivroDTOValidator());
	}
	@GetMapping("/listar")
	public String listarPaginas(
				@RequestParam(value="titulo", defaultValue = "") String titulo,
				@RequestParam(value="pagina", defaultValue = "0") Integer pagina,
				@RequestParam(value="tamPagina", defaultValue = "8") Integer tamPagina,
				@RequestParam(value="ordenacao", defaultValue = "titulo") String campo,
				@RequestParam(value="direcao", defaultValue = "ASC") String direcao,
				ModelMap modelo
			) {
		Page<Livro> buscaPaginada = servico.buscaPaginadaPorTitulo(titulo, pagina, tamPagina, campo, direcao);
		
		modelo.addAttribute("pagina", buscaPaginada);
		modelo.addAttribute("numRegistros", buscaPaginada.getNumberOfElements());
		modelo.addAttribute("titulo", titulo);
		modelo.addAttribute("tamanhoPagina", tamPagina);
		modelo.addAttribute("campoOrdenacao", campo);
		modelo.addAttribute("direcaoOrdenacao", direcao);
		modelo.addAttribute("direcaoReversa", direcao.equals("ASC") ? "DESC" : "ASC");
		
		return "livro/lista";
	}
	
	@GetMapping("/cadastrar")
	public String cadastrar(LivroDTO livroDTO) {
		return "livro/cadastro";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid LivroDTO livroDTO, BindingResult resultado, RedirectAttributes atributo) {
		Livro livro = servico.fromDTO(livroDTO);
		
		if (resultado.hasErrors()) {
			var erros = resultado.getAllErrors();
			for (var erro : erros) {
				System.out.println(erro.getDefaultMessage());
			}
			
			return "/livro/cadastro";
		}
		
		servico.incluir(livro);
		
		atributo.addFlashAttribute("sucesso", "Livro inserido com sucesso");
		
		return "redirect:/livros/listar";
	}

	@GetMapping("/editar/{id}")
	public String iniciarEdicao(@PathVariable("id") Long id, Model model) {
		model.addAttribute("livroDTO", new LivroDTO(servico.buscarPorId(id)));
		
		return "livro/cadastro";
	}
	
	@PostMapping("/editar")
	public String finalizarEdicao(@Valid LivroDTO livroDTO, BindingResult resultado, RedirectAttributes atributo) {
		Livro livro = servico.fromDTO(livroDTO);
		
		if (resultado.hasErrors()) {
			return "/livro/cadastro";
		}
		
		servico.alterar(livro.getId(), livro);
		
		atributo.addFlashAttribute("sucesso", "Livro alterado com sucesso");
		
		return "redirect:/livros/listar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, RedirectAttributes atributo) {
		servico.excluir(id);
		
		atributo.addFlashAttribute("sucesso", "Livro excluído com sucesso");
		
		return "redirect:/livros/listar";
	}
}