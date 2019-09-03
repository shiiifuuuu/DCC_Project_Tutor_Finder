package com.teamocta.dcc_project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.teamocta.dcc_project.R;
import com.teamocta.dcc_project.pojo.UserProfile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TutorListAdapter extends RecyclerView.Adapter<TutorListAdapter.ViewHolder> {

    private OnTutorClickListener mOnTutorClickListener;
    private ArrayList<UserProfile> tutorList;
    private View view;

    public TutorListAdapter(ArrayList<UserProfile> tutorList, OnTutorClickListener onTutorClickListener) {
        this.tutorList = tutorList;
        this.mOnTutorClickListener = onTutorClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tutor_search, parent, false);
        return new ViewHolder(view, mOnTutorClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserProfile currentTutor = tutorList.get(position);
        Glide.with(view).load(currentTutor.getImageUrl()).into(holder.ivTutorPic);
        holder.tvName.setText(currentTutor.getFirstName() + " " + currentTutor.getLastName());
        holder.tvProfession.setText(currentTutor.getProfession());
        holder.tvInstitute.setText(currentTutor.getInstitute());
        holder.tvMinimumSalary.setText(currentTutor.getMinimumSalary() + "/=");

        if(currentTutor.getTutorRating()!=null){
            holder.tvUserRating.setText(currentTutor.getTutorRating());
        }
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnTutorClickListener onTutorClickListener;
        private CircleImageView ivTutorPic;
        private TextView tvName;
        private TextView tvProfession;
        private TextView tvInstitute;
        private TextView tvMinimumSalary;
        private TextView tvUserRating;

        public ViewHolder(@NonNull View itemView, OnTutorClickListener onTutorClickListener) {
            super(itemView);
            this.onTutorClickListener = onTutorClickListener;
            ivTutorPic=itemView.findViewById(R.id.ivTutorPic);
            tvName=itemView.findViewById(R.id.tvName);
            tvProfession=itemView.findViewById(R.id.tvProfession);
            tvInstitute=itemView.findViewById(R.id.tvInstitute);
            tvMinimumSalary=itemView.findViewById(R.id.tvMinimumSalary);
            tvUserRating=itemView.findViewById(R.id.tvUserRating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTutorClickListener.onTutorClick(getAdapterPosition());
        }
    }

    public interface OnTutorClickListener {
        void onTutorClick(int position);
    }

    public void filterList(ArrayList<UserProfile> filteredList) {
        tutorList = filteredList;
        notifyDataSetChanged();
    }
}
