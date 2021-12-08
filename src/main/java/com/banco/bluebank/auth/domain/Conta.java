package com.banco.bluebank.auth.domain;

import com.banco.bluebank.auth.core.DigitoVerificadorLuhn;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "conta")
public class Conta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="num_conta")
    private Long numeroConta;

	@Column(name = "senha")
	private String senha;

	@Transient
	private String usuario;

	@PostLoad
	public void configurarUsuario(){
		var dv = new DigitoVerificadorLuhn();
		usuario = String.format("%d%s", numeroConta, dv.calculaDigitoVerificador(numeroConta.toString()))

	}

	public Conta() {
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Conta(Long numeroConta, String senha) {
		this.numeroConta = numeroConta;
		this.senha = senha;
	}

	public Long getNumeroConta() {
		return numeroConta;
	}

	public void setNumeroConta(Long numeroConta) {
		this.numeroConta = numeroConta;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Conta conta = (Conta) o;

		return numeroConta.equals(conta.numeroConta);
	}

	@Override
	public int hashCode() {
		return numeroConta.hashCode();
	}

}
