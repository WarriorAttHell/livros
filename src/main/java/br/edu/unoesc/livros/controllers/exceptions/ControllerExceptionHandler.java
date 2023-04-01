package br.edu.unoesc.livros.controllers.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.edu.unoesc.livros.service_impl_exceptions.ObjetoNaoEncontradoException;


@ControllerAdvice
public class ControllerExceptionHandler {
	@ExceptionHandler(ObjetoNaoEncontradoException.class)
	public ResponseEntity<ErroPadrao> objetoNaoEncontrado(ObjetoNaoEncontradoException e){
		LocalDateTime atual = LocalDateTime.now();
		
		ErroPadrao erro = new ErroPadrao(HttpStatus.NOT_FOUND.value(), e.getMessage(), atual);
		
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
		
		
		@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<ErroPadrao> argumentoInvalidoDoMetodo(MethodArgumentNotValidException e){
			LocalDateTime atual = LocalDateTime.now();
			
			ErroPadrao erro = new ErroPadrao(HttpStatus.NOT_FOUND.value(),"Erro no formulário", atual);
			
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
		
	
}


