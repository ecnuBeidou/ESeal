package com.pekingopera.versionupdate.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The executor to check update and download new version on network
 *
 * @author lzh
 */
public class UpdateExecutor implements IUpdateExecutor {

    private static ExecutorService pool;
    private static UpdateExecutor executor;

    private UpdateExecutor() {
        pool = Executors.newSingleThreadExecutor();
    }

    public synchronized static UpdateExecutor getInstance() {
        if (executor == null) {
            executor = new UpdateExecutor();
        }
        return executor;
    }

    @Override
    public void check(final UpdateWorker worker) {
        pool.execute(worker);
    }

    @Override
    public void onlineCheck(OnlineCheckWorker worker) {
        pool.execute(worker);
    }
}
