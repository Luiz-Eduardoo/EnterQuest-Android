package com.example.enterquest;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ChamadoAdapter extends BaseAdapter {

    private Context context;
    private List<Chamado> listaChamados;

    public ChamadoAdapter(Context context, List<Chamado> listaChamados) {
        this.context = context;
        this.listaChamados = listaChamados;
    }

    @Override
    public int getCount() {
        return listaChamados.size();
    }

    @Override
    public Object getItem(int position) {
        return listaChamados.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_chamado, parent, false);
        }

        TextView txtTituloChamado = convertView.findViewById(R.id.txtTituloChamado);
        TextView txtSetorChamado = convertView.findViewById(R.id.txtSetorChamado);
        TextView txtCategoriaChamado = convertView.findViewById(R.id.txtCategoriaChamado);
        TextView txtStatusChamado = convertView.findViewById(R.id.txtStatusChamado);

        Chamado chamado = listaChamados.get(position);

        txtTituloChamado.setText(chamado.getTitulo());
        txtSetorChamado.setText("Setor: " + chamado.getSetor());
        txtCategoriaChamado.setText("Categoria: " + chamado.getCategoria());
        txtStatusChamado.setText(chamado.getStatus());

        if (chamado.getStatus() != null) {
            String status = chamado.getStatus().toLowerCase();

            if (status.contains("pendente")) {
                txtStatusChamado.setTextColor(Color.parseColor("#B45309"));
            } else if (status.contains("concluído") || status.contains("concluido")) {
                txtStatusChamado.setTextColor(Color.parseColor("#047857"));
            } else {
                txtStatusChamado.setTextColor(Color.parseColor("#007C8A"));
            }
        }

        return convertView;
    }
}