package com.task.data.remote;

import com.task.data.remote.dto.NewsModel;
import com.task.data.remote.service.NewsService;
import com.task.utils.Constants;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by AhmedEltaher on 5/12/2016
 */

public class ApiRepository {
    private ServiceGenerator serviceGenerator;

    @Inject
    public ApiRepository(ServiceGenerator serviceGenerator) {
        this.serviceGenerator = serviceGenerator;
    }

    public Observable getNews() {
        NewsService newsService = serviceGenerator.createService(NewsService.class, Constants.BASE_URL);
        Observable<Response<NewsModel>> newsObservable = newsService.fetchNews();
        return newsObservable;
    }
}
