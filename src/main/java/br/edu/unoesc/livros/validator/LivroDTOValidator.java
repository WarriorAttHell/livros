package br.edu.unoesc.livros.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import br.edu.unoesc.livros.dto.LivroDTO;

public class LivroDTOValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return LivroDTO.class.equals(clazz) || 
				org.springframework.data.domain.PageImpl.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		LivroDTO l = (LivroDTO) target;
		
		if (l.getTitulo().toUpperCase().contains("UNOESC")) {
			errors.rejectValue("titulo", "titulo.proibido", "Título proibido!");
		}
	}
}
