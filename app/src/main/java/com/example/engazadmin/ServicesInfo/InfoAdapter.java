package com.example.engazadmin.ServicesInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engazadmin.R;

import java.util.List;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.informationViewHolder> {

    private Context context;
    private List<InfoModel> informationModels;
    private String cid;


    public InfoAdapter(Context context, List<InfoModel> informationModels, String cid) {
        this.context = context;
        this.informationModels = informationModels;
        this.cid=cid;
    }

    @NonNull
    @Override
    public informationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.information_item,parent,false);
        return new informationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull informationViewHolder holder, int position) {

        InfoModel informationModel = informationModels.get(position);
        holder.title1.setText(informationModel.getInfoFirstTitle());
        holder.description1.setText(informationModel.getInfoDescription1());
        holder.description2.setText(informationModel.getInfoDescription2());
        holder.description3.setText(informationModel.getInfoDescription3());
        holder.description4.setText(informationModel.getInfoDescription4());
        holder.description5.setText(informationModel.getInfoDescription5());
        holder.title2.setText(informationModel.getInfoSecondTitle());
        holder.description6.setText(informationModel.getInfoDescription6());
        holder.description7.setText(informationModel.getInfoDescription7());
//        Picasso.with(context)
//                .load(informationModel.getInfoImage())
//                .placeholder(R.mipmap.ic_launcher)
//                .fit()
//                .centerCrop()
//                .into(holder.imageInformation);


    }

    @Override
    public int getItemCount() {
        return informationModels.size();
    }

    public class informationViewHolder extends RecyclerView.ViewHolder{

        public TextView title1, description1,description2,description3,description4,description5,
                title2, description6,description7;
//        public ImageView imageInformation;

        public informationViewHolder(@NonNull View itemView) {
            super(itemView);
            title1 = itemView.findViewById(R.id.text_view_first_title);
            description1 = itemView.findViewById(R.id.text_view_description1);
            description2 = itemView.findViewById(R.id.text_view_description2);
            description3 = itemView.findViewById(R.id.text_view_description3);
            description4 = itemView.findViewById(R.id.text_view_description4);
            description5 = itemView.findViewById(R.id.text_view_description5);
            title2 = itemView.findViewById(R.id.text_view_second_title);
            description6 = itemView.findViewById(R.id.text_view_description6);
            description7 = itemView.findViewById(R.id.text_view_description7);
//            imageInformation = itemView.findViewById(R.id.image_information);
        }
    }
}
