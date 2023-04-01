package br.edu.unoesc.livros.service_impl_exceptions;

@SuppressWarnings("serial")
public class ObjetoNaoEncontradoException extends RuntimeException {
	public ObjetoNaoEncontradoException(String msg, Throwable causa) {
		super(msg, causa);
	}
	
	public ObjetoNaoEncontradoException(String msg) {
		super(msg);
	}
}