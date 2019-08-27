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

public class TuitionListAdapter extends RecyclerView.Adapter<TuitionListAdapter.ViewHolder>{

    private OnTuitionClickListener mOnTuitionClickListener;
    private ArrayList<UserProfile> tuitionList;
    private View view;

    public TuitionListAdapter(ArrayList<UserProfile> tuitionList, OnTuitionClickListener onTuitionClickListener) {
        this.tuitionList = tuitionList;
        this.mOnTuitionClickListener = onTuitionClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_tuition_search, parent, false);
        return new ViewHolder(view, mOnTuitionClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserProfile currentStudent = tuitionList.get(position);
        Glide.with(view).load(currentStudent.getImageUrl()).into(holder.ivStudentPic);
        holder.tvName.setText(currentStudent.getFirstName() + " " + currentStudent.getLastName());
        holder.tvClassDept.setText(currentStudent.getStudentClass() + " - " + currentStudent.getDepartment());
        holder.tvDaysPerWeek.setText(currentStudent.getDaysPerWeek());
        holder.tvSalaryRange.setText(currentStudent.getSalaryRange());
        holder.tvSubject.setText(currentStudent.getSubjects());
    }

    @Override
    public int getItemCount() {
        return tuitionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnTuitionClickListener onTuitionClickListener;
        private CircleImageView ivStudentPic;
        private TextView tvName;
        private TextView tvClassDept;
        private TextView tvSubject;
        private TextView tvDaysPerWeek;
        private TextView tvSalaryRange;

        public ViewHolder(@NonNull View itemView, OnTuitionClickListener onTuitionClickListener) {
            super(itemView);

            this.onTuitionClickListener = onTuitionClickListener;
            ivStudentPic=itemView.findViewById(R.id.ivStudentPic);
            tvName=itemView.findViewById(R.id.tvName);
            tvClassDept=itemView.findViewById(R.id.tvClass_Dept);
            tvSubject=itemView.findViewById(R.id.tvSubject);
            tvDaysPerWeek=itemView.findViewById(R.id.tvDaysPerWeek);
            tvSalaryRange=itemView.findViewById(R.id.tvSalaryRange);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onTuitionClickListener.onTuitionClick(getAdapterPosition());
        }
    }
    public interface OnTuitionClickListener {
        void onTuitionClick(int position);
    }

    public void filterList(ArrayList<UserProfile> filteredList) {
        tuitionList = filteredList;
        notifyDataSetChanged();
    }
}
