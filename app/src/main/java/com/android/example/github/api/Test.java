package com.android.example.github.api;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.example.github.api.VJavaEndpoint.CompletionCallback;
import com.verkada.endpoint.kotlin.VKotlinEndpoint;

import java.util.Date;
import java.util.List;

public class Test {


    public void test() {

        VJavaEndpoint vj = new VJavaEndpoint();


        vj.searchMotion(0, 0, new Date(), 3600, new CompletionCallback() {
            @Override
            public void onComplete(@NonNull List<VJavaEndpoint.Motion> motionList, @Nullable Throwable error) {

            }
        });
    }

}
