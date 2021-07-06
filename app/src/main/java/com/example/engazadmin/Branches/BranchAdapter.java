package com.example.engazadmin.Branches;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.R;
import com.example.engazadmin.Services.ServicesActivity;
import com.squareup.picasso.Picasso;

import java.util.List;


public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.ImageViewHolder> {
    private Context context;
    private List<BranchModel> branchesModelList;
    private String branchId,companyID;



    public BranchAdapter(Context context, List<BranchModel> branchesModelList, String branchId,String companyID) {
        this.context = context;
        this.branchesModelList = branchesModelList;
        this.branchId = branchId;
        this.companyID=companyID;

    }

    @Override
    public BranchAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.branch_item, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BranchAdapter.ImageViewHolder holder, int position) {
        BranchModel branchesModel = branchesModelList.get(position);
        holder.branchName.setText(branchesModel.getBranchName());
        holder.branchTimeWork.setText(branchesModel.getBranchTimeWork());
        holder.branchDaysWork.setText(branchesModel.getBranchDaysWork());
        holder.branchStatus.setText(branchesModel.getBranchStatus());
        Picasso.with(context)
                .load(branchesModel.getBranchImage())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.branchImage);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ServicesActivity.class);
                intent.putExtra("branchId", branchesModel.getId());
                intent.putExtra("companyId", companyID);

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return branchesModelList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView branchName, branchTimeWork, branchDaysWork, branchStatus;
        public ImageView branchImage;
        public CardView cardView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            branchName = itemView.findViewById(R.id.branch_name);
            branchTimeWork = itemView.findViewById(R.id.branch_time_work);
            branchDaysWork = itemView.findViewById(R.id.branch_days_work);
            branchStatus = itemView.findViewById(R.id.branch_status);
            branchImage = itemView.findViewById(R.id.branch_image);
            cardView = itemView.findViewById(R.id.card_view_branch);


        }
    }
}
