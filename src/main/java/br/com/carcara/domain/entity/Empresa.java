package br.com.carcara.domain.entity;

import br.com.carcara.domain.valueobject.CNPJ; 
import br.com.carcara.domain.valueobject.Money; 
import java.time.LocalDate; 
import java.util.List;
import java.util.Objects;

public record Empresa(String nome, CNPJ cnpj, LocalDate dataAbertura, Money capitalSocial, List<Socio> socios) {
    
    public Empresa {
        Objects.requireNonNull(nome, "Nome não pode ser nulo.");
        if (nome.isBlank()) {
            throw new IllegalArgumentException("Nome não pode ser vazio ou conter apenas espaços em branco.");
        }
        Objects.requireNonNull(cnpj, "CNPJ não pode ser nulo.");
        // Validação do CNPJ é feita na classe CNPJ   
         
        Objects.requireNonNull(dataAbertura, "Data de abertura não pode ser nula.");
        if (dataAbertura.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Data de abertura não pode ser futura.");
        }
        Objects.requireNonNull(capitalSocial, "Capital social não pode ser nulo.");
        if (capitalSocial.valor().signum() < 0) {
            throw new IllegalArgumentException("Capital social não pode ser negativo.");
        }
        Objects.requireNonNull(socios, "Lista de sócios não pode ser nula.");
        socios = List.copyOf(socios);
        if (socios.isEmpty()) {
            throw new IllegalArgumentException("A empresa deve ter pelo menos um sócio no QSA.");

        }
    }

}
