package com.jim.framework.core.config;

import java.io.IOException;
import java.util.*;

/**
 * Created by celiang.hu on 2018-11-22.
 */
public class LaunchArgsLoaderChain {
    private List<LaunchArgsLoader> loaders;

    public LaunchArgsLoaderChain(List<LaunchArgsLoader> loaders) {
        this.loaders = loaders;
    }

    public LaunchArgsLoaderChain() {
        this.loaders = new ArrayList<>();
    }

    public void add(LaunchArgsLoader loader) {
        this.loaders.add(loader);
    }

    public void remove(LaunchArgsLoader loader) {
        this.remove(loader);
    }

    public void load() throws Exception {
        sort();
        for (LaunchArgsLoader loader : this.loaders) {
            loader.load();
        }
    }
    private void sort() {
        Collections.sort(this.loaders, new Comparator<LaunchArgsLoader>() {
            @Override
            public int compare(LaunchArgsLoader o1, LaunchArgsLoader o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
    }
}
