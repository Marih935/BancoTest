package banco;

public class Cliente extends Pessoa {
	
	private Conta conta;
	
	public Cliente(String nome, int cpf, Conta conta) {
		this.setNome(nome);
		this.setCpf(cpf);
		this.setConta(conta);
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}
	
}
