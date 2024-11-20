package banco;

import java.util.ArrayList;
import java.util.List;

public class Banco {
	private List<Cliente> listClientes = new ArrayList<>();
	private String nomeBanco;
	private int nrBanco;

	public Banco(String nomeBanco){
		this.setNomeBanco(nomeBanco);
	}

	public void criarConta(String nomeCliente, int cpfCliente, Conta conta){
		if (procurarContasPorCPF(cpfCliente, conta.getTipoConta()) == null){
			listClientes.add(new Cliente(nomeCliente, cpfCliente, conta));
			System.out.println(
					String.format("Conta " + conta.getTipoConta() +  " de N %s foi criada com sucesso.",
					procurarContasPorCPF(cpfCliente, conta.getTipoConta()).getConta().getNumeroConta()));
		} else {
			System.out.println(
					String.format("Conta de N %d já existe para o cliente %s.", 
							procurarContasPorCPF(cpfCliente, conta.getTipoConta()).getConta().getNumeroConta()));
		}
	}

	public Cliente procurarContasPorCPF(int cpf, ContaTipo contaTipo){
		for (Cliente cliente : listClientes) {
			if(cliente.getCpf() == cpf && cliente.getConta().getTipoConta().equals(contaTipo)){
				if (cliente.getConta() != null) {
					//return cliente.getConta();
					return cliente;
				}
			}
		}
		return null;
	}

	public void creditarValor(int cpf, double valor, ContaTipo contaTipo){
		Cliente c = procurarContasPorCPF(cpf, contaTipo);		
		if(c!=null) { 
			if (valor < 0) {
				System.out.println("Valor inválido");
			} else {				
				c.getConta().creditar(valor);
			}
		}
		else
			System.out.println("Conta inexistente");
	}

	public void debitarValor(int cpf, double valor, ContaTipo contaTipo){
		Cliente c = procurarContasPorCPF(cpf, contaTipo);
		if(c!=null) {
			if (valor < 0) {
				System.out.println("Valor inválido");
			}
			if (c.getConta().getSaldoConta() < valor) {				
				System.out.println("Saldo insuficiente");
			} else {				
				c.getConta().debitar(valor);
			}
		}
		else
			System.out.println("Conta inexistente");
	}

	public void transferirValor(int cpfOrigem, ContaTipo contaTipoOrigem, int valor, int cpfDestino, ContaTipo contaTipoDestino){
		Cliente cOrigem = procurarContasPorCPF(cpfOrigem, contaTipoOrigem);
		Cliente cDestino = procurarContasPorCPF(cpfDestino, contaTipoDestino);
		if(cOrigem!=null && cDestino!=null) {
			if (cOrigem.getConta().getTipoConta() != contaTipoOrigem || cDestino.getConta().getTipoConta() != contaTipoDestino) {				
				System.out.println("Tipo de conta incorreto");
			}
			if (valor < 0) {
				System.out.println("Valor inválido");
			}
			if (cOrigem.getConta().getSaldoConta() < valor) {				
				System.out.println("Saldo insuficiente");
			} else {				
				cOrigem.getConta().debitar(valor); 
				cDestino.getConta().creditar(valor);
			}
			System.err.println(cDestino.getConta().getSaldoConta());
		}
		else
			System.out.println("Conta de origem ou destino inexistente");
	}

	public void listarContasClientes() {
		for (Cliente c : listClientes) {
			System.out.println("#############################");
			System.out.println("# CPF: " + c.getCpf());
			System.out.println("# Nome: " + c.getNome()); 
			System.out.println("# Ag: " + c.getConta().getAgencia().getIdAgencia());
			System.out.println("# CC: " + c.getConta().getTipoConta()); 
			System.out.println("# Saldo: " + c.getConta().getSaldoConta()); 
			System.out.println("#############################"); 
		}
	}
	
	public Cliente procurarClientePorCPF(int cpf){
		for (Cliente cliente : listClientes) {
			if(cliente.getCpf() == cpf){
				if (cliente.getConta() != null) {
					return cliente;
				} else System.out.println("Cliente não localizado!");
			}
		}
		return null;
	}

	public double consultarSaldo(int cpf, ContaTipo contaTipo){
		return procurarContasPorCPF(cpf, contaTipo).getConta().getSaldoConta(); 
	}

	public int getNrBanco() {
		return nrBanco;
	}

	public void setNrBanco(int nrBanco) {
		this.nrBanco = nrBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}
}
