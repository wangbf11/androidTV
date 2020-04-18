package com.example.xueliang.network;


import com.example.xueliang.bean.CommonResult;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by wbf on 2019/1/16.
 */
public final class RxSchedulerUtils {

    /**
     * 在RxJava的使用过程中我们会频繁的调用subscribeOn()和observeOn(),通过Transformer结合
     * Observable.compose()我们可以复用这些代码
     *
     * @return Transformer
     */
    public static <T> ObservableTransformer<T, T> normalSchedulersTransformer() {
        return new ObservableTransformer<T, T>(){

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static <T> SingleSource<T> toSimpleSingle(Single<T> upstream){
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> ObservableSource<T> toSimpleSingle(Observable<T> upstream){
        return upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 处理过的数据
     */
    public static <T> ObservableTransformer<CommonResult<T>, T> handleResult() {
        return new ObservableTransformer<CommonResult<T>, T>(){

            @Override
            public ObservableSource<T> apply(Observable<CommonResult<T>> upstream) {
                return upstream.flatMap(new Function<CommonResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(CommonResult<T> tCommonResult) throws Exception {
                        if ("OK".equals(tCommonResult.getMsg())){
                            return createData(tCommonResult.getList());
                        }else {
                            return Observable.error(new ServerException(tCommonResult.getMsg()));
                        }
                    }
                });
            }
        };
    }

    /**
     * 处理过的数据 并切换线程
     */
    public static <T> ObservableTransformer<CommonResult<T>, T> handleResultToMainThread() {
        return new ObservableTransformer<CommonResult<T>, T>(){

            @Override
            public ObservableSource<T> apply(Observable<CommonResult<T>> upstream) {
                return upstream.flatMap(new Function<CommonResult<T>, ObservableSource<T>>() {
                    @Override
                    public ObservableSource<T> apply(CommonResult<T> tCommonResult) throws Exception {
                        if ("OK".equals(tCommonResult.getMsg()))
                            return createData(tCommonResult.getList());
                        else {
                            return Observable.error(new ServerException(tCommonResult.getMsg()));
                        }

                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     */
    public static <T> Observable<T> createData(final T data) {

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> e) throws Exception {
                try {
                    e.onNext(data);
                    e.onComplete();
                } catch (Exception ex) {
                    e.onError(ex);
                }
            }
        });

    }
}
