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

import test.vtd.koreanguage.R;
import test.vtd.koreanguage.Model.NewWord;

public class NewWordAdapter extends RecyclerView.Adapter<NewWordAdapter.newWordViewHolder>{
    private List<NewWord> mListNewWord;
    private final NewWordAdapter.IClickItemNewWord iClickItemNewWord;

    public interface IClickItemNewWord{
        void updateNewWord(NewWord newWord);
        void deleteNewWord(NewWord newWord);
    }

    public NewWordAdapter(NewWordAdapter.IClickItemNewWord iClickItemNewWord) {
        this.iClickItemNewWord = iClickItemNewWord;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<NewWord> list){
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
        final NewWord newWord = mListNewWord.get(position);
        if(newWord == null)
            return;
        holder.tv_newWordName.setText(newWord.getNewWord());
        holder.tv_newWordMean.setText(newWord.getMean());
        holder.img_updatenewWord.setOnClickListener(v -> iClickItemNewWord.updateNewWord(newWord));
        holder.img_deletenewWord.setOnClickListener(v -> iClickItemNewWord.deleteNewWord(newWord));
    }

    @Override
    public int getItemCount() {
        if(mListNewWord!=null)
            return mListNewWord.size();
        return 0;
    }

    public static class newWordViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_newWordName;
        private final TextView tv_newWordMean;
        private final ImageView img_updatenewWord;
        private final ImageView img_deletenewWord;
        public newWordViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_newWordName = itemView.findViewById(R.id.tv_name);
            tv_newWordMean = itemView.findViewById(R.id.tv_mean);
            img_updatenewWord = itemView.findViewById(R.id.img_update);
            img_deletenewWord = itemView.findViewById(R.id.img_delete);
        }
    }
}
