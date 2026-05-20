package com.example.enterquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class RecompensaGerenteAdapter extends BaseAdapter {

    private final Context context;
    private final List<Recompensa> recompensas;

    public RecompensaGerenteAdapter(Context context, List<Recompensa> recompensas) {
        this.context = context;
        this.recompensas = recompensas;
    }

    @Override
    public int getCount() {
        return recompensas.size();
    }

    @Override
    public Object getItem(int position) {
        return recompensas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recompensa_gerente, parent, false);
        }

        Recompensa recompensa = recompensas.get(position);

        TextView txtNome = view.findViewById(R.id.txtNomeRecompensaGerente);
        TextView txtDescricao = view.findViewById(R.id.txtDescricaoRecompensaGerente);
        TextView txtPontos = view.findViewById(R.id.txtPontosRecompensaGerente);
        TextView txtEstoque = view.findViewById(R.id.txtEstoqueRecompensaGerente);

        txtNome.setText(recompensa.getNome());
        txtDescricao.setText(recompensa.getDescricao());
        txtPontos.setText(recompensa.getCustoPontos() + " pontos");
        txtEstoque.setText("Estoque: " + recompensa.getEstoque());

        return view;
    }
}