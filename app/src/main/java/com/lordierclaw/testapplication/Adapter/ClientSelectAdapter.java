package com.lordierclaw.testapplication.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lordierclaw.testapplication.Listener.IClientSelectListener;
import com.lordierclaw.testapplication.Model.Client;
import com.lordierclaw.testapplication.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ClientSelectAdapter extends RecyclerView.Adapter<ClientSelectAdapter.ViewHolder> {
    private List<Client> clientList;
    private IClientSelectListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_client_select, null);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Client client = clientList.get(position);
        if (client == null) return;
        holder.bind(client);
        holder.itemView.setOnClickListener(view -> {
            holder.clientCheckbox.setChecked(!holder.clientCheckbox.isChecked());
            if (listener != null) listener.onClick(client);
        });
    }

    public void setClientSelectOnClickListener(IClientSelectListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (clientList == null) return 0;
        return clientList.size();
    }

    public void setList(List<Client> clientList) {
        this.clientList = clientList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView clientName;
        private CircleImageView clientPfp;
        private CheckBox clientCheckbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            clientName = itemView.findViewById(R.id.adapter_client_list_name);
            clientPfp = itemView.findViewById(R.id.adapter_client_list_pfp);
            clientCheckbox = itemView.findViewById(R.id.adapter_client_checkbox);
        }

        public void bind(Client client) {
            clientName.setText(client.getName());
            // TODO: set clientPfp by using client.getPfpUrl();
            clientCheckbox.setChecked(client.isChecked());
        }
    }
}
