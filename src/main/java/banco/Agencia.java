package banco;

public class Agencia {
	
	private int idAgencia;
	private String nomeAgencia;
	
	public Agencia(String nomeAgencia, int idAgencia) {
		this.setNomeAgencia(nomeAgencia);
		this.setIdAgencia(idAgencia);
	}
	
	public int getIdAgencia() {
		return idAgencia;
	}
	public void setIdAgencia(int idAgencia) {
		this.idAgencia = idAgencia;
	}
	public String getNomeAgencia() {
		return nomeAgencia;
	}
	public void setNomeAgencia(String nomeAgencia) {
		this.nomeAgencia = nomeAgencia;
	}
}
