package pl.snowdog.moesampleapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.snowdog.model.Repository;

public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoriesViewHolder> {

    private List<Repository> mRepositoriesList;

    public RepositoriesAdapter(List<Repository> mRepositoriesList) {
        this.mRepositoriesList = mRepositoriesList;
    }

    @Override
    public RepositoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_row, parent, false);
        return new RepositoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoriesViewHolder holder, int position) {
        holder.titleTextView.setText(mRepositoriesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRepositoriesList.size();
    }

    public class RepositoriesViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;

        public RepositoriesViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
