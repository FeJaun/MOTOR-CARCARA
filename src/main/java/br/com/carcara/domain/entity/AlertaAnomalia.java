package br.com.carcara.domain.entity;

import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.enums.TipoAnomalia;
import br.com.carcara.domain.valueobject.CNPJ;
import java.time.LocalDateTime;

import java.util.Objects;


public record AlertaAnomalia (
    CNPJ cnpjContratada,
    TipoAnomalia tipoAnomalia,
    NivelSeveridade nivelSeveridade,
    LocalDateTime dataHoraDeteccao,
    String justificativa,
    String numeroContrato
) {
    public AlertaAnomalia {
        //As regras do CNPJ são validadas na classe CNPJ
        Objects.requireNonNull(cnpjContratada, "CNPJ não pode ser nulo");
        
        //As regras do TipoAnomalia são validadas na classe TipoAnomalia
        Objects.requireNonNull(tipoAnomalia, "TipoAnomalia não pode ser nulo");
        
        //As regras do NivelSeveridade são validadas na classe NivelSeveridade
        Objects.requireNonNull(nivelSeveridade, "NivelSeveridade não pode ser nulo");
        
        Objects.requireNonNull(dataHoraDeteccao, "DataHoraDeteccao não pode ser nula");
        
        Objects.requireNonNull(justificativa, "Justificativa não pode ser nula");
        if (justificativa.isBlank()) {
            throw new IllegalArgumentException("Justificativa não pode ser vazia ou conter apenas espaços em branco.");
        }
        Objects.requireNonNull(numeroContrato, "NumeroContrato não pode ser nulo");
        if (numeroContrato.isBlank()) {
            throw new IllegalArgumentException("NumeroContrato não pode ser vazio ou conter apenas espaços em branco.");
        }
    }
}
