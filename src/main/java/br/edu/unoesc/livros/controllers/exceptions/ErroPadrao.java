package br.edu.unoesc.livros.controllers.exceptions;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class ErroPadrao implements Serializable {
	private Integer status;
	private String msg;
	
	@JsonFormat(pattern="dd/MM/yyyy HH:mm:ss")
	private LocalDateTime dataHora;
	
	private List<Campo> erros = new ArrayList<Campo>();
	
	@Data
	@AllArgsConstructor
	private static class Campo {
		private String nome;
		private String mensagem;
	}
	
	public void adicionaErro(String nome, String mensagem) {
		this.erros.add(new Campo(nome, mensagem));
	}
	
	public ErroPadrao(Integer status, String msg, LocalDateTime dataHora) {
		this.status = status;
		this.msg = msg;
		this.dataHora = dataHora;
	}
}