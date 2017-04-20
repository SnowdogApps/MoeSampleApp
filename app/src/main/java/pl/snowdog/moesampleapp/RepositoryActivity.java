package pl.snowdog.moesampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import pl.snowdog.interfaces.IRepositoryView;
import pl.snowdog.model.Repository;
import pl.snowdog.presenter.RepositoryPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RepositoryActivity extends AppCompatActivity implements IRepositoryView {

    private List<Repository> mAllRepositories = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RepositoriesAdapter mRepositoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_repositories);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                LinearLayout.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        RepositoryPresenter repositoryPresenter = new RepositoryPresenter(this);
        repositoryPresenter.getRepositories(AndroidSchedulers.mainThread(), Schedulers.newThread());
    }

    @Override
    public void showRepositories(List<Repository> allRepositories) {
        mAllRepositories = allRepositories;
        mRepositoriesAdapter = new RepositoriesAdapter(mAllRepositories);
        mRecyclerView.setAdapter(mRepositoriesAdapter);
    }
}
