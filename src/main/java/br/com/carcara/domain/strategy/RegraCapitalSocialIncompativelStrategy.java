package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Contrato;
import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.enums.TipoAnomalia;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class RegraCapitalSocialIncompativelStrategy {

    // Limiar regulatório/pericial: Capital Social mínimo de 10% do valor do contrato
    private static final BigDecimal PERCENTUAL_MINIMO_CAPITAL = new BigDecimal("0.10");

    public Optional<AlertaAnomalia> verificarAnomalia(Empresa empresa, Contrato contrato) {
        Objects.requireNonNull(empresa, "Empresa não pode ser nula");
        Objects.requireNonNull(contrato, "Contrato não pode ser nulo");

        BigDecimal capital = empresa.capitalSocial().valor();
        BigDecimal valorContrato = contrato.valor().valor();

        // Limiar mínimo exigido = valorContrato * 0.10
        BigDecimal capitalMinimoExigido = valorContrato.multiply(PERCENTUAL_MINIMO_CAPITAL);

        if (capital.compareTo(capitalMinimoExigido) < 0) {
            BigDecimal proporcaoPercentual = capital.multiply(new BigDecimal("100"))
                    .divide(valorContrato, 2, RoundingMode.HALF_UP);

            String justificativa = String.format(
                    "Capital social de R$ %s representa apenas %s%% do valor do contrato homologado (R$ %s). Abaixo do patamar de segurança de 10%%.",
                    capital.toPlainString(),
                    proporcaoPercentual.toPlainString(),
                    valorContrato.toPlainString()
            );

            return Optional.of(new AlertaAnomalia(
                    empresa.cnpj(),
                    TipoAnomalia.CAPITAL_SOCIAL_INCOMPATIVEL,
                    NivelSeveridade.ALTO,
                    LocalDateTime.now(),
                    justificativa,
                    contrato.numeroContrato()
            ));
        }

        return Optional.empty();
    }
}