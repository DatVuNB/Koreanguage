package test.vtd.koreanguage.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.model.newWord;

public class newWordAdapter extends RecyclerView.Adapter<newWordAdapter.newWordViewHolder>{
    private List<newWord> mListNewWord;
    private newWordAdapter.IClickItemNewWord iClickItemNewWord;

    public interface IClickItemNewWord{
        void updateNewWord(newWord newWord);
        void deleteNewWord(newWord newWord);
    }

    public newWordAdapter(newWordAdapter.IClickItemNewWord iClickItemNewWord) {
        this.iClickItemNewWord = iClickItemNewWord;
    }

    public void setData(List<newWord> list){
        this.mListNewWord = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public newWordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newword, parent, false);
        return new newWordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull newWordViewHolder holder, int position) {
        final newWord newWord = mListNewWord.get(position);
        if(newWord == null)
            return;
        holder.tv_newWordName.setText(newWord.getNewWord());
        holder.tv_newWordMean.setText(newWord.getMean());
        holder.img_updatenewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemNewWord.updateNewWord(newWord);
            }
        });
        holder.img_deletenewWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickItemNewWord.deleteNewWord(newWord);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListNewWord!=null)
            return mListNewWord.size();
        return 0;
    }

    public class newWordViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_newWordName;
        private TextView tv_newWordMean;
        private ImageView img_updatenewWord;
        private ImageView img_deletenewWord;
        public newWordViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_newWordName = itemView.findViewById(R.id.tv_name);
            tv_newWordMean = itemView.findViewById(R.id.tv_mean);
            img_updatenewWord = itemView.findViewById(R.id.img_update);
            img_deletenewWord = itemView.findViewById(R.id.img_delete);
        }
    }
}
