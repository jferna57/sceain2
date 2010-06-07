package es.in2.framework.demo.persistence.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import es.in2.framework.demo.core.bean.IPojo;

/**
 * @author CETEC/CTJEE
 */
@Entity
@Table(name="disciplina")
public class Disciplina implements IPojo{

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(generator="sq_disciplina")
	@Column(name="id_disciplina")
	private Long id;
	
	@Column(name="nome", length=100)
	private String nome;

	public Disciplina() {		
	}

	public Disciplina(String nome) {
		this.nome = nome;
	}

	public Disciplina(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
}
