package pl.snowdog.interfaces;

import java.util.List;

import pl.snowdog.model.Repository;

public interface IRepositoryView {
    void showRepositories(List<Repository> allRepositories);
}
