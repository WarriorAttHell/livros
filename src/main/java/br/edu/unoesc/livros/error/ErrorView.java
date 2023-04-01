package br.edu.unoesc.livros.error;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ErrorView implements ErrorViewResolver {

	@Override
	public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> map) {
		System.out.println("---------");
		map.forEach(
				(chave, valor) -> System.out.println(chave + ": " + valor)
		);
		System.out.println("---------");
		
		ModelAndView model = new ModelAndView("/error");
		model.addObject("status", status.value());
		
		switch (status.value()) {
			case 404:
				model.addObject("error", "Página não encontrada!");
				model.addObject("message", "A URL para a página '" + map.get("path") + "' não existe!");
				break;
			case 500:
				model.addObject("error", "Erro interno do servidor!");
				model.addObject("message", "Ocorreu um erro inesperado, tente novamente mais tarde!");
				break;
			default:
				model.addObject("error", map.get("error"));
				model.addObject("message", map.get("message"));
		}
		
		return model;
	}

}