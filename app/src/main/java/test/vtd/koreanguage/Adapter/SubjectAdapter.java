package test.vtd.koreanguage.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import test.vtd.koreanguage.Model.Subject;
import test.vtd.koreanguage.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.subjectViewHolder>{
    private List<Subject> mListSubject;
    private final IClickItemSubject iClickItemSubject;

    public interface IClickItemSubject{
        void updateSubject(Subject subject);
        void deleteSubject(Subject subject);
        void onSubjectClick(Subject subject);
    }

    public SubjectAdapter(IClickItemSubject iClickItemSubject) {
        this.iClickItemSubject = iClickItemSubject;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Subject> list){
        this.mListSubject = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public subjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new subjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull subjectViewHolder holder, int position) {
        final Subject subject = mListSubject.get(position);
        if(subject == null)
            return;
        holder.tv_subjectName.setText(subject.getSubjectName());
        holder.img_updateSubject.setOnClickListener(v -> iClickItemSubject.updateSubject(subject));
        holder.img_deleteSubject.setOnClickListener(v -> iClickItemSubject.deleteSubject(subject));
        holder.tv_subjectName.setOnClickListener(v -> iClickItemSubject.onSubjectClick(subject));
    }

    @Override
    public int getItemCount() {
        if(mListSubject != null)
            return mListSubject.size();
        return 0;
    }

    public static class subjectViewHolder extends RecyclerView.ViewHolder{
        private final TextView tv_subjectName;
        private final ImageView img_updateSubject;
        private final ImageView img_deleteSubject;
        public subjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subjectName = itemView.findViewById(R.id.tv_name);
            img_updateSubject = itemView.findViewById(R.id.img_update);
            img_deleteSubject = itemView.findViewById(R.id.img_delete);
        }
    }
}
