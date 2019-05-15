package com.example.myapplication;

import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;

class TestDialog extends Dialog implements LifecycleObserver {
    private Lifecycle lifecycle;

    public TestDialog(Context context) {
        super(context);
    }


}
