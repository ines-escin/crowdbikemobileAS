package br.ufpe.cin.util.bikecidadao;

import java.util.List;

import br.ufpe.cin.br.adapter.bikecidadao.Ocorrencia;

/**
 * Created by jal3 on 05/11/2015.
 */
public interface OnGetOccurrencesCompletedCallback {

    void onGetOccurrencesCompleted(List<Ocorrencia> occurrences);
}
