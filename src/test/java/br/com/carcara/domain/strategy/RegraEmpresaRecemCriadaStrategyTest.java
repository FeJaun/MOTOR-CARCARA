package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.Contrato;
import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.enums.TipoAnomalia;
import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.valueobject.CNPJ;
import br.com.carcara.domain.valueobject.Money;
import br.com.carcara.domain.entity.Socio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegraEmpresaRecemCriadaStrategyTest {

    private RegraEmpresaRecemCriadaStrategy regra;
    private final LocalDate dataBaseAbertura = LocalDate.of(2026, 1, 1);
    private final CNPJ cnpjPadrao = new CNPJ("11.222.333/0001-81");

    @BeforeEach
    void setUp() {
        this.regra = new RegraEmpresaRecemCriadaStrategy();

    }

    private Empresa criarEmpresa(LocalDate dataAbertura) {
        Socio socio = new Socio("Socio padrão", "12345678901", "Sócio-Administrador");
        return new Empresa(
                "Empresa Teste LTDA",
                cnpjPadrao,
                dataAbertura,
                new Money(new BigDecimal("10000.00")),
                List.of(socio));
    }

    private Contrato criarContrato(LocalDate dataInicio, BigDecimal valor) {
        return new Contrato(cnpjPadrao,
                new Money(valor),
                dataInicio,
                dataInicio.plusMonths(12),
                "CT-2026/001",
                "Prefeitura Municipal",
                "Serviços Gerais"
            );
    }

    @Test
    @DisplayName ("Deve detectar anomalia critica quando a empresa com menos de 90 dias vence contrato vultoso")
    void deveDetectarAnomaliaQuandoEmpresaRecemCriadaGanhaContratoVultoso() {
        // ARRANGE (15 dias de vida, R$ 500.000,00)
        Empresa empresa = criarEmpresa(dataBaseAbertura);
        Contrato contrato = criarContrato(dataBaseAbertura.plusDays(15), new BigDecimal("500000.00"));
        // ACT
        Optional<AlertaAnomalia> resultado = regra.verificarAnomalia(contrato, empresa);
        // ASSERT
        assertTrue(resultado.isPresent(), "Deveria detectar anomalia para empresa com menos de 90 dias de vida");
        AlertaAnomalia alerta = resultado.get();
        assertEquals(TipoAnomalia.EMPRESA_RECEM_CRIADA, alerta.tipoAnomalia());
        assertEquals(NivelSeveridade.CRITICO, alerta.nivelSeveridade());
        assertEquals("CT-2026/001", alerta.numeroContrato());
        assertEquals(cnpjPadrao, alerta.cnpjContratada());
    }
    @Test 
    @DisplayName ("Não deve detectar anomalia quando a empresa possui mais de 90 dias de vida")
    void naoDeveDetectarAnomaliaQuandoEmpresaTemMaisDe90Dias(){
        // Arrange (91 dias de vida, valor vultoso de 500.000,00)
        Empresa empresa = criarEmpresa(dataBaseAbertura);
        Contrato contrato = criarContrato(dataBaseAbertura.plusDays(91), new BigDecimal("500000.00"));
        //Act
        Optional<AlertaAnomalia> resultado = regra.verificarAnomalia(contrato, empresa);
        //Assert
        assertTrue(resultado.isEmpty(), "Empresas com mais de 90 dias não devem disparar essa regra.");

    }   
    @Test 
    @DisplayName ("Não deve detectar anomalia quando o contrato NÃO for vultoso (< 100k)")
    void naoDeveDetectarAnomaliaQuandoContratoNaoForVultoso(){
        //Arrange (10 dias de vida, mas apenas R$45.000,00)
        Empresa empresa = criarEmpresa(dataBaseAbertura);
        Contrato contrato = criarContrato(dataBaseAbertura.plusDays(10), new BigDecimal(45000.00));
        //Act
        Optional<AlertaAnomalia> resultado = regra.verificarAnomalia(contrato, empresa);
        //Assert
        assertTrue(resultado.isEmpty(), "Contratos de pequeno valor não devem disparar a RN-01!");
    }
    @Test 
    @DisplayName ("Fronteira temporal: Empresa com exatamente 90 dias de vida NÃO deve disparar anomalia")
    void naoDeveDetectarAnomaliaNoLimiteDe90Dias(){
        //Arrange (exatamente 90 dias de vida)
        Empresa empresa = criarEmpresa(dataBaseAbertura);
        Contrato contrato = criarContrato(dataBaseAbertura.plusDays(90), new BigDecimal(200000.00));
        //Act 
        Optional<AlertaAnomalia> resultado = regra.verificarAnomalia(contrato, empresa);
        //Assert
        assertTrue(resultado.isEmpty(), "No limite maximo de 90 dias,a anomalia não deve disparar (limite exclusivo)!");
    }
    @Test 
    @DisplayName ("Fronteira de valor: Contrato no valor exato de R$100.000,00 DEVE disparar anomalia")
    void deveDetectarAnomaliaNoLimiteExatoDeValorVultoso(){
        //arrange
        Empresa empresa = criarEmpresa(dataBaseAbertura);
        Contrato contrato = criarContrato(dataBaseAbertura.plusDays(20),new BigDecimal("100000.00"));
        //act
        Optional<AlertaAnomalia> resultado = regra.verificarAnomalia(contrato, empresa);
        //assert
        assertTrue(resultado.isPresent(),"Contrato no valor exato de R$100.000,00 já é considerado vultoso!");
    }
    @Test 
    @DisplayName("Robustez defensiva: deve lançar NullPointerException caso contrato ou empresa sejam nulos")
    void deveLancarExcecaoParaEntradasNulas(){
        Empresa empresa = criarEmpresa(dataBaseAbertura);
        Contrato contrato = criarContrato(dataBaseAbertura.plusDays(10), new BigDecimal(100000.00));
        //
        assertThrows(NullPointerException.class,() -> regra.verificarAnomalia(null, empresa));
        assertThrows(NullPointerException.class,() -> regra.verificarAnomalia(contrato, null));
    }
}