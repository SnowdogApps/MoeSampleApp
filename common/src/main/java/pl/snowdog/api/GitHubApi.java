package pl.snowdog.api;

import java.util.List;

import pl.snowdog.model.Repository;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ewelinabukowska on 27.03.2017.
 */

public interface GitHubApi {
    @GET("orgs/SnowdogApps/repos")
    Observable<List<Repository>> getRepos();
}
