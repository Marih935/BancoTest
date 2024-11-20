package banco;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BancoTest {
	
	private Banco banco;
	
	@BeforeEach
	void setUp() {
		banco = new Banco("Banco Teste");
	}

	@Test
	void testCriarConta_Sucesso() {
		Conta conta = new Conta(1, ContaTipo.CORRENTE);
		banco.criarConta("Mariana", 123456789, conta);
		
		Cliente cliente = banco.procurarContasPorCPF(123456789, ContaTipo.CORRENTE);
		assertNotNull(cliente);
		assertEquals("Mariana", cliente.getNome());
		assertEquals(ContaTipo.CORRENTE, cliente.getConta().getTipoConta());
	}

	@Test
	void testCriarConta_ContaJaExistente() {
	    Conta conta1 = new Conta(1, ContaTipo.CORRENTE);
	    banco.criarConta("Mari", 123456789, conta1);
	    
	    Conta conta2 = new Conta(2, ContaTipo.CORRENTE);
	    banco.criarConta("Mari", 222222222, conta2);

	    // Valida que apenas a primeira conta foi criada
	    Cliente cliente = banco.procurarContasPorCPF(123456789, ContaTipo.CORRENTE);
	    assertNotNull(cliente);
	    assertEquals(conta1.getNumeroConta(), cliente.getConta().getNumeroConta());
	    
	    // Valida que a segunda conta não foi registrada
	    assertNotEquals(conta2.getNumeroConta(), cliente.getConta().getNumeroConta());
	}

	@Test
	void testCreditarValor_Sucesso() {
		Conta conta = new Conta(1, ContaTipo.POUPANCA);
		banco.criarConta("Mari", 123456789, conta);
		
		banco.creditarValor(123456789, 20.0, ContaTipo.POUPANCA);
		double saldo = banco.consultarSaldo(123456789, ContaTipo.POUPANCA);
		
		assertEquals(20.0, saldo);
	}
	
	@Test
	void testCreditarValor_ContaInexistente() {
	    // Prepara para capturar a saída do System.out.println
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    banco.creditarValor(999999999, 50, ContaTipo.POUPANCA);
	    
	    assertEquals("Conta inexistente", outContent.toString().trim());
	    
	    // Restaura o System.out
	    System.setOut(System.out);
	}
	
	@Test
	void testCreditarValor_ValorNegativo() {
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    Conta conta = new Conta(1, ContaTipo.POUPANCA);
	    banco.criarConta("Mari", 123456789, conta);
	    banco.creditarValor(123456789, -50, ContaTipo.POUPANCA);
	    
	    // Divide a saída capturada em linhas
	    String[] lines = outContent.toString().split(System.lineSeparator());
	    
	    assertEquals("Valor inválido", lines[1].trim());
	    	    
	    System.setOut(System.out);
	    
	}
	
	@Test
    void testDebitarValor_Sucesso() {
        Conta conta = new Conta(1, ContaTipo.POUPANCA);
        banco.criarConta("Maria", 456789123, conta);
        banco.creditarValor(456789123, 1000.0, ContaTipo.POUPANCA);

        banco.debitarValor(456789123, 300.0, ContaTipo.POUPANCA);
        double saldo = banco.consultarSaldo(456789123, ContaTipo.POUPANCA);

        assertEquals(700.0, saldo);
    }
	
	@Test
	void testDebitarValor_ContaInexistente() {
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    banco.debitarValor(999999999, 50, ContaTipo.POUPANCA);
	    
	    assertEquals("Conta inexistente", outContent.toString().trim());
	    
	    System.setOut(System.out);
	}
	
	@Test
	void testDebitarValor_SaldoInsuficiente() {
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    Cliente cliente = new Cliente("Cliente Teste", 123456789, new Conta(1, ContaTipo.POUPANCA));
	    banco.criarConta("Cliente Teste", 123456789, cliente.getConta());
	    banco.debitarValor(123456789, 50, ContaTipo.POUPANCA);
	    
	    String[] lines = outContent.toString().split(System.lineSeparator());
	    
	    assertEquals("Saldo insuficiente", lines[1].trim());
	    assertEquals(0, cliente.getConta().getSaldoConta());
	    
	    System.setOut(System.out);
	}
	
	@Test
	void testDebitarValor_ValorNegativo() {
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    Conta conta = new Conta(1, ContaTipo.POUPANCA);
	    banco.criarConta("Mari", 123456789, conta);
	    banco.debitarValor(123456789, -50, ContaTipo.POUPANCA);
	    
	    String[] lines = outContent.toString().split(System.lineSeparator());
	    
	    assertEquals("Valor inválido", lines[1].trim());
	    	    
	    System.setOut(System.out);
	    
	}
	
	@Test
    void testTransferirValor_Sucesso() {
        Conta contaOrigem = new Conta(1, ContaTipo.CORRENTE);
        Conta contaDestino = new Conta(2, ContaTipo.POUPANCA);
        banco.criarConta("Mari", 111222333, contaOrigem);
        banco.criarConta("Mari Gemea", 444555666, contaDestino);

        // Mari tem 1000 e transfere 500 para Mari Gemea
        banco.creditarValor(111222333, 1000.0, ContaTipo.CORRENTE);
        banco.transferirValor(111222333, ContaTipo.CORRENTE, 500, 444555666, ContaTipo.POUPANCA);

        double saldoOrigem = banco.consultarSaldo(111222333, ContaTipo.CORRENTE);
        double saldoDestino = banco.consultarSaldo(444555666, ContaTipo.POUPANCA);

        assertEquals(500.0, saldoOrigem);
        assertEquals(500.0, saldoDestino);
    }
	
	@Test
	void testTransferirValor_ContaInexistente() {
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    banco.transferirValor(999999999, ContaTipo.POUPANCA, 50, 888888888, ContaTipo.CORRENTE);
	    
	    assertEquals("Conta de origem ou destino inexistente", outContent.toString().trim());
	    
	    System.setOut(System.out);
	}
	
	@Test
	void testTransferirValor_SaldoInsuficiente() {
	    Conta contaOrigem = new Conta(1, ContaTipo.CORRENTE);
	    Conta contaDestino = new Conta(2, ContaTipo.POUPANCA);
	    banco.criarConta("Mari", 111222333, contaOrigem);
	    banco.criarConta("Mari Gemea", 444555666, contaDestino);

	    // Tentativa de transferir mais do que o saldo da conta origem (saldo inicial é 0)
	    banco.transferirValor(111222333, ContaTipo.CORRENTE, 500, 444555666, ContaTipo.POUPANCA);

	    // Saldos devem permanecer inalterados
	    double saldoOrigem = banco.consultarSaldo(111222333, ContaTipo.CORRENTE);
	    double saldoDestino = banco.consultarSaldo(444555666, ContaTipo.POUPANCA);

	    assertEquals(0.0, saldoOrigem);
	    assertEquals(0.0, saldoDestino);
	}
	
	@Test
    void testConsultarSaldo_ContaInexistente() {
        assertThrows(NullPointerException.class, () -> {
            banco.consultarSaldo(999999999, ContaTipo.CORRENTE);
        });
    }
	
	@Test
	void testProcurarContaPorCPF_Sucesso() {
		Conta conta = new Conta(1, ContaTipo.POUPANCA);
	    banco.criarConta("Mari", 666666666, conta);
	    banco.creditarValor(666666666, 50, ContaTipo.POUPANCA);
	    
	    banco.procurarContasPorCPF(666666666, ContaTipo.POUPANCA);
	    assertEquals(50, conta.getSaldoConta());
	}
	
	@Test
	void testProcurarContaPorCPF_Inexistente() {
		Cliente cliente = banco.procurarContasPorCPF(999999999, ContaTipo.CORRENTE);
	    assertEquals(null, cliente);
	}
	
	@Test
	void testListarContasClientes_Vazio() {
	    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	    System.setOut(new PrintStream(outContent));
	    
	    banco.listarContasClientes();
	    
	    // Verifica se não há saída, já que não há clientes
	    assertEquals("", outContent.toString().trim());
	    
	    System.setOut(System.out);
	}
	
	@Test
	void testProcurarClientePorCPF_Sucesso() {
		Conta conta = new Conta(1, ContaTipo.POUPANCA);
	    banco.criarConta("Mari", 555555555, conta);
	    
	    Cliente cliente = banco.procurarClientePorCPF(555555555);
	    assertEquals("Mari", cliente.getNome());
	    assertEquals(ContaTipo.POUPANCA, cliente.getConta().getTipoConta());
	}
	
	@Test
	void testProcurarClientePorCPF_Inexistente() {
	    Cliente cliente = banco.procurarClientePorCPF(999999999);
	    assertEquals(null, cliente);
	}
}
