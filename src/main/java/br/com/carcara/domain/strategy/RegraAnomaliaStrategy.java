package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.Contrato;
import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Empresa;

import java.util.Optional;

public interface RegraAnomaliaStrategy {
    Optional<AlertaAnomalia> verificarAnomalia(Contrato contrato, Empresa empresa);
}
