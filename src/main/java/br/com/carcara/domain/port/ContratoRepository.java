package br.com.carcara.domain.port;

import br.com.carcara.domain.entity.Contrato;
import br.com.carcara.domain.valueobject.CNPJ;

import java.util.Optional;
import java.util.List;

public interface ContratoRepository {
    //Salva ou atualiza um contrato no repositório.
    void salvar(Contrato contrato);
    //Busca um contrato pelo número do contrato. Retorna um Optional que pode estar vazio se o contrato não for encontrado.
    Optional<Contrato> buscarPorNumero(String numeroContrato);
    //verifica se um contrato com o número do contrato existe, sem puxar tudo da memoria, apenas para verificar se existe ou não.
    boolean existePorNumero(String numeroContrato);
    //Lista todos os contratos associados a uma empresa contratada pelo CNPJ.
    List <Contrato> listarPorCnpjContratada(CNPJ cnpj);
    // Retorna todos os contratos cadastrados para varredura em lote.
    List <Contrato> listarTodos();
    //Lista todos os contratos associados a uma empresa contratante pelo CNPJ.
    List <Contrato> listarPorOrgaoPublico(String orgaoPublico);

}
