package br.com.carcara.domain.strategy;

import br.com.carcara.domain.entity.AlertaAnomalia;
import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.entity.Socio;
import br.com.carcara.domain.enums.NivelSeveridade;
import br.com.carcara.domain.enums.TipoAnomalia;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.*;

public class RegraVinculoSocietarioStrategy {
    public List<AlertaAnomalia> verificarAnomalias(List<Empresa> empresas) {
        if (empresas == null || empresas.size() < 2) {
            return Collections.emptyList();
        }
        List<AlertaAnomalia> alertas = new ArrayList<>();

        for (int i = 0; i < empresas.size(); i++) {
            for (int j = i + 1; j < empresas.size(); j++) {
                Empresa empA = empresas.get(i);
                Empresa empB = empresas.get(j);

                Set<String> docsEmpresaB = empB.socios().stream()
                        .map(Socio::identificador)
                        .collect(Collectors.toSet());

                List<Socio> sociosEmComum = empA.socios().stream()
                        .filter(s -> docsEmpresaB.contains(s.identificador()))
                        .toList();

                if (!sociosEmComum.isEmpty()) {
                    String nomesSocios = sociosEmComum.stream()
                            .map(s -> s.nome() + " (" + s.identificador() + ")")
                            .collect(Collectors.joining(", "));

                    String justificativa = String.format(
                            "Vinculo societario detectado entre concorrentes: %s (%s) e %s (%s). Sócio(s) em comum: %s",
                            empA.nome(), empA.cnpj().formatar(),
                            empB.nome(), empB.cnpj().formatar(),
                            nomesSocios);
                    alertas.add(new AlertaAnomalia(
                        empA.cnpj(),
                        TipoAnomalia.SOCIOS_CONCORRENTES,
                        NivelSeveridade.CRITICO,
                        LocalDateTime.now(),
                        justificativa,
                        "DISPUTA-LICITATORIA"
                    ));
                }
            }
        }
        return alertas;
    }
}