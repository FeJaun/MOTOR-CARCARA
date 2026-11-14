package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.Contrato;
import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.enums.TipoAnomalia;
import br.com.carcara.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

public class RegraEmpresaRecemCriadaStrategy implements RegraAnomaliaStrategy {
    private static final long LIMITE_DIAS_RECEM_CRIADA = 90;
    private static final Money VALOR_VULTOSO = new Money(new BigDecimal("100000.00"));

    @Override
    public Optional<AlertaAnomalia> verificarAnomalia(Contrato contrato, Empresa empresa) {
        Objects.requireNonNull(contrato, "Contrato não pode ser nulo");
        Objects.requireNonNull(empresa, "Empresa não pode ser nula.");

        // 1. Calcula os dias de vida da empresa até o início do contrato
        long diasDeVida = ChronoUnit.DAYS.between(empresa.dataAbertura(), contrato.dataInicio());
        // 2. Aplica os critérios de auditoria
        boolean isRecemCriada = diasDeVida >= 0 && diasDeVida < LIMITE_DIAS_RECEM_CRIADA;
        boolean isValorVultoso = contrato.valor().compareTo(VALOR_VULTOSO) >= 0;
        // 3. Se cumprir os dois critérios, acende o alerta de anomalia crítica
        if (isRecemCriada && isValorVultoso) {
            String descricao = String.format(
                    "Empresa '%s' possui apenas %d dias de existência e venceu contrato vultoso no valor de %s.",
                    empresa.nome(), diasDeVida,
                    contrato.valor().formatar()

            );
            AlertaAnomalia alerta = new AlertaAnomalia(
                    empresa.cnpj(),
                    TipoAnomalia.EMPRESA_RECEM_CRIADA,
                    NivelSeveridade.CRITICO,
                    LocalDateTime.now(),
                    descricao,
                    contrato.numeroContrato());
            return Optional.of(alerta);
        }
        return Optional.empty();

    }
}
