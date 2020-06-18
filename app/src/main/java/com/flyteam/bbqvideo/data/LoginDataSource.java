package com.flyteam.bbqvideo.data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.flyteam.bbqvideo.data.model.LoggedInUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    Handler mainHandler = new Handler(Looper.getMainLooper());
    public void login(final LoginRepository loginRepository,String username, String password) {
        try {
            // TODO: handle loggedInUser authentication
            final String url = " http://129.226.134.65:8080/login";
            OkHttpClient client = new OkHttpClient.Builder().readTimeout(5, TimeUnit.SECONDS)//设置超时时间
                    .build();
            RequestBody formBody = new FormBody.Builder()   //创建表单请求体
                    .add("username",username)
                    .add("password",password)
                    .build();
            Request request = new Request.Builder()//创建Request对象
                    .url(url)
                    .post(formBody)//传递请求体
                    .build();
            client.newCall(request).enqueue(new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            loginRepository.loginRsp(ResultGenerator.genFailResult("请求失败"));
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    if(response.isSuccessful())
                    {
                        final  String content = response.body().string();
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                final Result sucResult = JSON.parseObject(content, Result.class);
                                if(sucResult.getData()!=null){
                                    sucResult.setData(JSON.parseObject(sucResult.getData().toString(), LoggedInUser.class));
                                }
                                loginRepository.loginRsp(sucResult);
                            }
                        });
                    }
                    else
                    {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                loginRepository.loginRsp(ResultGenerator.genFailResult("请求失败"));
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            loginRepository.loginRsp(ResultGenerator.genFailResult(e.toString()));
        }
    }
    public void logout() {
        // TODO: revoke authentication
    }
}