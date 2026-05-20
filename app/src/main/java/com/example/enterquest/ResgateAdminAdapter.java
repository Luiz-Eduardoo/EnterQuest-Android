package com.example.enterquest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ResgateAdminAdapter extends BaseAdapter {

    public interface OnConfirmarEntregaClickListener {
        void onConfirmarEntrega(ResgateRecompensa resgate);
    }

    private final Context context;
    private final List<ResgateRecompensa> resgates;
    private final OnConfirmarEntregaClickListener listener;

    public ResgateAdminAdapter(
            Context context,
            List<ResgateRecompensa> resgates,
            OnConfirmarEntregaClickListener listener
    ) {
        this.context = context;
        this.resgates = resgates;
        this.listener = listener;
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
                    .inflate(R.layout.item_resgate_admin, parent, false);
        }

        ResgateRecompensa resgate = resgates.get(position);

        TextView txtNome = view.findViewById(R.id.txtNomeResgateAdmin);
        TextView txtDescricao = view.findViewById(R.id.txtDescricaoResgateAdmin);
        TextView txtCodigo = view.findViewById(R.id.txtCodigoResgateAdmin);
        TextView txtStatus = view.findViewById(R.id.txtStatusResgateAdmin);
        TextView txtCusto = view.findViewById(R.id.txtCustoResgateAdmin);
        TextView txtData = view.findViewById(R.id.txtDataResgateAdmin);
        TextView btnConfirmarEntrega = view.findViewById(R.id.btnConfirmarEntregaResgate);

        txtNome.setText(resgate.getNomeRecompensa());
        txtDescricao.setText(resgate.getDescricaoRecompensa());

        String codigo = resgate.getCodigoResgate();
        if (codigo == null || codigo.trim().isEmpty()) {
            txtCodigo.setText("Código: Não informado");
        } else {
            txtCodigo.setText("Código: " + codigo);
        }

        txtCusto.setText(resgate.getCustoPontos() + " pontos");
        txtData.setText(formatarData(resgate.getDataResgate()));

        if (resgate.isUtilizado()) {
            txtStatus.setText("Status: Entregue");
            txtStatus.setTextColor(0xFF15803D);
            btnConfirmarEntrega.setVisibility(View.GONE);
        } else {
            txtStatus.setText("Status: Pendente de retirada");
            txtStatus.setTextColor(0xFFD97706);
            btnConfirmarEntrega.setVisibility(View.VISIBLE);

            btnConfirmarEntrega.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onConfirmarEntrega(resgate);
                }
            });
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