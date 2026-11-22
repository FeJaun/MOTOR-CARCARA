package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Contrato;
import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.entity.Socio;
import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.enums.TipoAnomalia;
import br.com.carcara.domain.valueobject.CNPJ;
import br.com.carcara.domain.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegraCapitalSocialIncompativelStrategyTest {

    private final CNPJ cnpjValido = new CNPJ("00.000.000/0001-91");
    private final Socio socio = new Socio("João Silva", "12345678900", "Administrador");

    @Test
    @DisplayName("Deve gerar alerta de severidade ALTO quando o capital social for desproporcionalmente inferior ao valor do contrato")
    void deveGerarAlertaQuandoCapitalSocialForIncompativelComContrato() {
        // Empresa com R$ 10.000,00 de capital social
        Empresa empresa = new Empresa(
                "Construcoes Fantasma LTDA",
                cnpjValido,
                LocalDate.of(2020, 1, 1),
                new Money(new BigDecimal("10000.00")),
                List.of(socio)
        );

        // Contrato milionário de R$ 2.000.000,00
        Contrato contrato = new Contrato(
                cnpjValido,
                new Money(new BigDecimal("2000000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                "CT-2023/001",
                "Prefeitura Municipal",
                "Pavimentação e recapeamento asfáltico"
        );

        RegraCapitalSocialIncompativelStrategy regra = new RegraCapitalSocialIncompativelStrategy();

        Optional<AlertaAnomalia> alerta = regra.verificarAnomalia(empresa, contrato);

        assertTrue(alerta.isPresent(), "Deveria emitir alerta de capital social incompatível");
        assertEquals(TipoAnomalia.CAPITAL_SOCIAL_INCOMPATIVEL, alerta.get().tipoAnomalia());
        assertEquals(NivelSeveridade.ALTO, alerta.get().nivelSeveridade());
        assertEquals(cnpjValido, alerta.get().cnpjContratada());
        assertEquals("CT-2023/001", alerta.get().numeroContrato());
    }

    @Test
    @DisplayName("Não deve gerar alerta quando o capital social for compatível com o valor do contrato")
    void naoDeveGerarAlertaQuandoCapitalSocialForCompativel() {
        // Capital de R$ 500.000,00 (25% do valor do contrato)
        Empresa empresa = new Empresa(
                "Engenharia Solida LTDA",
                cnpjValido,
                LocalDate.of(2018, 5, 10),
                new Money(new BigDecimal("500000.00")),
                List.of(socio)
        );

        Contrato contrato = new Contrato(
                cnpjValido,
                new Money(new BigDecimal("2000000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                "CT-2023/002",
                "Secretaria de Obras",
                "Reforma Estrutural"
        );

        RegraCapitalSocialIncompativelStrategy regra = new RegraCapitalSocialIncompativelStrategy();

        Optional<AlertaAnomalia> alerta = regra.verificarAnomalia(empresa, contrato);

        assertTrue(alerta.isEmpty(), "Não deveria gerar alerta para capital social compatível");
    }

    @Test
    @DisplayName("Não deve gerar alerta quando o capital social for exatamente o limiar mínimo de 10%")
    void naoDeveGerarAlertaQuandoCapitalSocialForExatamenteNoLimiar() {
        // Capital de R$ 100.000,00 (exatamente 10% de R$ 1.000.000,00)
        Empresa empresa = new Empresa(
                "Pavimentadora Padrao LTDA",
                cnpjValido,
                LocalDate.of(2019, 3, 15),
                new Money(new BigDecimal("100000.00")),
                List.of(socio)
        );

        Contrato contrato = new Contrato(
                cnpjValido,
                new Money(new BigDecimal("1000000.00")),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                "CT-2023/003",
                "Prefeitura Municipal",
                "Conservação de Vias"
        );

        RegraCapitalSocialIncompativelStrategy regra = new RegraCapitalSocialIncompativelStrategy();

        Optional<AlertaAnomalia> alerta = regra.verificarAnomalia(empresa, contrato);

        assertTrue(alerta.isEmpty(), "Não deveria gerar alerta no limite exato de 10%");
    }

    @Test
    @DisplayName("Deve lançar exceção quando empresa ou contrato forem nulos")
    void deveLancarExcecaoQuandoParametrosForemNulos() {
        RegraCapitalSocialIncompativelStrategy regra = new RegraCapitalSocialIncompativelStrategy();

        assertThrows(NullPointerException.class, () -> regra.verificarAnomalia(null, null));
    }
}