package test.vtd.koreanguage.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import test.vtd.koreanguage.R;

public class subjectAdapter extends RecyclerView.Adapter<subjectAdapter.subjectViewHolder>{
    private List<subject> mListSubject;
    private IClickItemSubject iClickItemSubject;

    public interface IClickItemSubject{
        void updateSubject(subject subject);
        void deleteSubject(subject subject);
        void onSubjectClick(subject subject);
    }

    public subjectAdapter(IClickItemSubject iClickItemSubject) {
        this.iClickItemSubject = iClickItemSubject;
    }

    public void setData(List<subject> list){
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
        final subject subject = mListSubject.get(position);
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
