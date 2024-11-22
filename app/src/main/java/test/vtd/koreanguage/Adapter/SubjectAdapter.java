package test.vtd.koreanguage.Adapter;

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
    private IClickItemSubject iClickItemSubject;

    public interface IClickItemSubject{
        void updateSubject(Subject subject);
        void deleteSubject(Subject subject);
        void onSubjectClick(Subject subject);
    }

    public SubjectAdapter(IClickItemSubject iClickItemSubject) {
        this.iClickItemSubject = iClickItemSubject;
    }

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
        holder.img_updateSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemSubject.updateSubject(subject);
            }
        });
        holder.img_deleteSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemSubject.deleteSubject(subject);
            }
        });
        holder.tv_subjectName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemSubject.onSubjectClick(subject);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListSubject != null)
            return mListSubject.size();
        return 0;
    }

    public class subjectViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_subjectName;
        private ImageView img_updateSubject;
        private ImageView img_deleteSubject;
        public subjectViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_subjectName = itemView.findViewById(R.id.tv_name);
            img_updateSubject = itemView.findViewById(R.id.img_update);
            img_deleteSubject = itemView.findViewById(R.id.img_delete);
        }
    }
}
