package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.TutorProfile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorListAdapter extends RecyclerView.Adapter<TutorListAdapter.ViewHolder> {

    private ArrayList<TutorProfile> tutorList;
    private View view;
    public TutorListAdapter(ArrayList<TutorProfile> tutorList) {
        this.tutorList = tutorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tutor_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TutorProfile currentTutor = tutorList.get(position);
        Glide.with(view).load(currentTutor.getImageUrl()).into(holder.ivTutorPic);
        holder.tvName.setText(currentTutor.getFirstName() + " " + currentTutor.getLastName());
        holder.tvProfession.setText(currentTutor.getProfession());
        holder.tvInstitute.setText(currentTutor.getInstitute());
        holder.tvMinimumSalary.setText(currentTutor.getMinimumSalary() + "/=");
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView ivTutorPic;
        private TextView tvName;
        private TextView tvProfession;
        private TextView tvInstitute;
        private TextView tvMinimumSalary;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivTutorPic=itemView.findViewById(R.id.ivTutorPic);
            tvName=itemView.findViewById(R.id.tvName);
            tvProfession=itemView.findViewById(R.id.tvProfession);
            tvInstitute=itemView.findViewById(R.id.tvInstitute);
            tvMinimumSalary=itemView.findViewById(R.id.tvMinimumSalary);
        }
    }
}
