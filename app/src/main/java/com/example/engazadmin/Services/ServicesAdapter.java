package com.example.engazadmin.Services;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.R;
import com.example.engazadmin.ServicesInfo.InfoActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.servicesViewHolder> {
    private Context context;
    private List<ServicesModel> servicesModelList;
    private String branchId;
    private String companyId;

    public ServicesAdapter(Context context, List<ServicesModel> servicesModelList, String branchId,String companyId) {
        this.context = context;
        this.servicesModelList = servicesModelList;
        this.branchId = branchId;
        this.companyId = branchId;
    }
    @NonNull
    @Override
    public servicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.services_item, parent, false);
        return new servicesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull servicesViewHolder holder, int position) {

        ServicesModel servicesModel = servicesModelList.get(position);
        holder.description.setText(servicesModel.getServicesDescription());
//        holder.more.setText(servicesModel.getServicesMore());
        Picasso.with(context)
                .load(servicesModel.getServicesImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageServices);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InfoActivity.class);
                intent.putExtra("id", servicesModel.getId());
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesModelList.size();
    }
    public class servicesViewHolder extends RecyclerView.ViewHolder {
        public TextView description, more;
        public ImageView imageServices;

        public servicesViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.text_description);
            more = itemView.findViewById(R.id.text_more);
            imageServices = itemView.findViewById(R.id.image_service);

        }
    }
}