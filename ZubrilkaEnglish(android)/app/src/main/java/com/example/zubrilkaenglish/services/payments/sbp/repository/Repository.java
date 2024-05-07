package com.example.zubrilkaenglish.services.payments.sbp.repository;

import java.util.List;

public interface Repository {
    interface PackagesLoadListener {
        void onPackages(List<String> packages);
    }

    void loadAppPackages(PackagesLoadListener listener);
}
