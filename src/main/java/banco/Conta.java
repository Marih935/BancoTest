package banco;

import java.util.Random;

public class Conta {
	
	private int numeroConta;
	private ContaTipo tipoConta;
	private double saldoConta;
	private Agencia agencia;
	
	public Conta(int idAgencia, ContaTipo contaTipo){
		this.numeroConta = new Random().nextInt(100);
		this.setTipoConta(contaTipo);
		this.saldoConta = 0;
		setAgencia(new Agencia("", idAgencia));
	}
	
	public void creditar(double valor){
		this.saldoConta = this.saldoConta + valor;
	}
	
	public void debitar(double valor){
		this.saldoConta = this.saldoConta - valor;
	}
	
	public int getNumeroConta() {
		return numeroConta;
	}

	public double getSaldoConta() {
		return saldoConta;
	}

	public Agencia getAgencia() {
		return agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	public ContaTipo getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(ContaTipo tipoConta) {
		this.tipoConta = tipoConta;
	}

}
