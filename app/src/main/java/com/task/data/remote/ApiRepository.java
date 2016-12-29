package com.task.data.remote;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import com.task.App;
import com.task.data.remote.dto.NewsModel;
import com.task.data.remote.service.NewsService;
import com.task.utils.Constants;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;

import static com.task.utils.Constants.ERROR_UNDEFINED;
import static com.task.utils.NetworkUtils.ERROR_NO_INTERNET;
import static com.task.utils.NetworkUtils.NETWORK_ERROR;
import static com.task.utils.NetworkUtils.isNetworkAvailable;
import static com.task.utils.ObjectUtil.isNull;

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

        Observable<NewsModel> newsObservable = Observable.create(new Observable.OnSubscribe<NewsModel>() {
            @Override
            public void call(Subscriber<? super NewsModel> subscriber) {
                if (!isNetworkAvailable(App.getContext())) {
                    Exception e = new NetworkErrorException();
                    subscriber.onError(e);
                } else {
                    NewsService newsService = serviceGenerator.createService(NewsService.class, Constants.BASE_URL);
                    ResponseWrapper responseWrapper = processCall(newsService.fetchNews());
                    NewsModel newsModel = (NewsModel) responseWrapper.getResponse();
                    subscriber.onNext(newsModel);
                    subscriber.onCompleted();
                }
            }
        });
        return newsObservable;
    }

    //Process the calls
    @NonNull
    private ResponseWrapper processCall(Call call) {
        if (!isNetworkAvailable(App.getContext())) {
            return new ResponseWrapper(new ResponseError("", ERROR_NO_INTERNET));
        }
        return processResponse(call, false);
    }

    @NonNull
    private ResponseWrapper processResponse(Call call, boolean isVoid) {
        try {
            Response response = call.execute();
            if (isNull(response)) {
                //Extra check in case internet is disconnected in between or no proper response
                // received from backend
                return new ResponseWrapper(new ResponseError(NETWORK_ERROR, ERROR_UNDEFINED));
            }
            int responseCode = response.code();
            if (response.isSuccessful()) {
                return new ResponseWrapper(responseCode, isVoid ? null : response.body());
            } else {
                ResponseError responseError;
                responseError = new ResponseError(response.message(), responseCode);
                return new ResponseWrapper(responseError);
            }
        } catch (IOException e) {
            return new ResponseWrapper(new ResponseError(NETWORK_ERROR, ERROR_UNDEFINED));
        }
    }
}
