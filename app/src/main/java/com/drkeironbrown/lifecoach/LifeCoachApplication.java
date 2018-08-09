package com.drkeironbrown.lifecoach;

import android.app.Application;

import com.drkeironbrown.lifecoach.db.DBOpenHelper;
import com.drkeironbrown.lifecoach.db.DatabaseManager;
import com.facebook.stetho.Stetho;

public class LifeCoachApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initDataBase();
        initStetho();
    }

    private void initDataBase() {
        DatabaseManager.initialize(DBOpenHelper.getInstance((this)));
        DBOpenHelper.getInstance((this)).createDataBase(this);
    }

    private void initStetho() {

        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
