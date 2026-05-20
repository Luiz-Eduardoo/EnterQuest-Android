package com.example.enterquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ResgateRecompensaAdapter extends BaseAdapter {

    private final Context context;
    private final List<ResgateRecompensa> resgates;

    public ResgateRecompensaAdapter(Context context, List<ResgateRecompensa> resgates) {
        this.context = context;
        this.resgates = resgates;
    }

    @Override
    public int getCount() {
        return resgates.size();
    }

    @Override
    public Object getItem(int position) {
        return resgates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.item_resgate_recompensa, parent, false);
        }

        ResgateRecompensa resgate = resgates.get(position);

        TextView txtNome = view.findViewById(R.id.txtNomeResgate);
        TextView txtDescricao = view.findViewById(R.id.txtDescricaoResgate);
        TextView txtCusto = view.findViewById(R.id.txtCustoResgate);
        TextView txtData = view.findViewById(R.id.txtDataResgate);
        TextView txtCodigo = view.findViewById(R.id.txtCodigoResgate);
        TextView txtStatus = view.findViewById(R.id.txtStatusResgate);
        TextView txtDataUtilizacao = view.findViewById(R.id.txtDataUtilizacaoResgate);

        txtNome.setText(resgate.getNomeRecompensa());
        txtDescricao.setText(resgate.getDescricaoRecompensa());
        txtCusto.setText(resgate.getCustoPontos() + " pontos");
        txtData.setText(formatarData(resgate.getDataResgate()));
        txtCodigo.setText("Código: " + resgate.getCodigoResgate());

        if (resgate.isUtilizado()) {
            txtStatus.setText("Status: Entregue");
            txtStatus.setTextColor(0xFF15803D);

            txtDataUtilizacao.setVisibility(View.VISIBLE);
            txtDataUtilizacao.setText(
                    "Entregue em: " + formatarData(resgate.getDataUtilizacao())
            );
        } else {
            txtStatus.setText("Status: Pendente de retirada");
            txtStatus.setTextColor(0xFFD97706);

            txtDataUtilizacao.setVisibility(View.GONE);
        }

        return view;
    }

    private String formatarData(String data) {
        if (data == null || data.trim().isEmpty()) {
            return "Data não informada";
        }

        try {
            String[] partes = data.split("T");
            String[] dataPartes = partes[0].split("-");
            String hora = partes[1].substring(0, 5);

            return dataPartes[2] + "/" + dataPartes[1] + "/" + dataPartes[0] + " às " + hora;
        } catch (Exception e) {
            return data.replace("T", " ");
        }
    }
}