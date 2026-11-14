package br.com.carcara.domain.port;

import br.com.carcara.domain.entity.Empresa;
import br.com.carcara.domain.valueobject.CNPJ;
import java.util.Optional;

public interface EmpresaRepository {
   
    //Persiste ou atualiza uma empresa no repositório.
    void salva(Empresa empresa);

    //Busca uma empresa pelo CNPJ. Retorna um Optional que pode estar vazio se a empresa não for encontrada.
    Optional<Empresa> ProcuraPorCnpj(CNPJ cnpj);

    //verifica se uma empresa com o CNPJ existe, sem puxar tudo da memoria, apenas para verificar se existe ou não.
    boolean existePorCnpj(CNPJ cnpj);
}