package br.com.schumaker.exceptions.conta;

public class SaldoInsuficienteException extends Exception{ //checked
	
	public SaldoInsuficienteException(String msg) {
		super(msg);
	}
}
