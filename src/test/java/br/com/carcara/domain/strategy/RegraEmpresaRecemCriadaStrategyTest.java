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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegraEmpresaRecemCriadaStrategyTest {

    private RegraEmpresaRecemCriadaStrategy regra;

    @BeforeEach
    void setUp() {
        this.regra = new RegraEmpresaRecemCriadaStrategy();

    }

    @Test
    void deveDetectarAnomaliaQuandoEmpresaRecemCriadaGanhaContratoVultoso() {
        // ARRANGE (cenário do crime)
        LocalDate abertura = LocalDate.of(2026, 1, 1);
        LocalDate inicioDoContrato = abertura.plusDays(15);
        Socio socio = new Socio("Sócio Laranja", "12345678901", "Sócio-Administrador");

        Empresa empresa = new Empresa(
                "Empresa Fantasma LTDA",
                new CNPJ("11.222.333/0001-81"),
                abertura,
                new Money(new BigDecimal("10000.00")),
                List.of(socio)

        );
        Contrato contrato = new Contrato(
                new CNPJ("11.222.333/0001-81"),
                new Money(new BigDecimal("500000.00")),
                inicioDoContrato,
                inicioDoContrato,
                "CT-2026/001",
                "Prefeitura Municipal",
                "Serviços de Tecnologia");
        // ACT
        Optional<AlertaAnomalia> resultado = regra.verificarAnomalia(contrato, empresa);

        // ASSERT
        assertTrue(resultado.isPresent(), "Deveria detectar anomalia para empresa com apenas 15 dias de vida!");

        AlertaAnomalia alerta = resultado.get();
        assertEquals(TipoAnomalia.EMPRESA_RECEM_CRIADA, alerta.tipoAnomalia());
        assertEquals(NivelSeveridade.CRITICO, alerta.nivelSeveridade());
    }
}