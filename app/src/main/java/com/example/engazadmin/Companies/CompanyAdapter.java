package com.example.engazadmin.Companies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.Branches.BranchActivity;
import com.example.engazadmin.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ImageViewHolder> {
    private Context context;
    private List<CompanyModel> companiesModelList;


    public CompanyAdapter(Context context, List<CompanyModel> companiesModelList) {
        this.context = context;
        this.companiesModelList = companiesModelList;

    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.companies_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        CompanyModel companiesModel = companiesModelList.get(position);
        holder.textViewName.setText(companiesModel.getName());
        Picasso.with(context)
                .load(companiesModel.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageViewCompany);

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BranchActivity.class);
                intent.putExtra("companyId", companiesModel.getId());
                v.getContext().startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return companiesModelList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public CircleImageView imageViewCompany;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_company_name);
            imageViewCompany = itemView.findViewById(R.id.image_view_company);


        }
    }
}