/* 
 * This file is part of YMActors project.
 * Copyright (C) 2017 Yarhoslav ME <yarhoslavme@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.yarhoslav.ymactors.core.services;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yarhoslav.ymactors.core.interfaces.IWorker;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author yarhoslavme
 */
public class YMExecutorService {
    //TODO: Check health of the threadpoolexecutors
    //TODO: Implement steal jobs or better dispatcher giver based on job queue size
    
    private final Logger logger = LoggerFactory.getLogger(YMExecutorService.class);
    private final ExecutorService[] pool;
    private final BlockingQueue<Runnable>[] queues;
    private final int threads;
    private final AtomicInteger index;
    public final AtomicInteger mensajes = new AtomicInteger(0);

    public YMExecutorService(int pThreads) {
        threads = pThreads;
        pool = new ExecutorService[threads];
        queues = new BlockingQueue[threads];
        index = new AtomicInteger(0);
        for (int i = 0; i < threads; i++) {
            queues[i] = new LinkedBlockingQueue<>();
            pool[i] = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, queues[i], new DaemonThreadFactory());
        }
    }

    public void offer(IWorker pTask) {
        mensajes.incrementAndGet();
        pool[pTask.getDispatcher()].execute(pTask);
    }

    public int getDispacher() {
        //TODO: Take care of integer.MAX in index.
        int disp = index.getAndIncrement() % threads;
        return disp;

    }

    @Override
    public String toString() {
        String salida = "YMExecutorService:";
        for (int i = 0; i < threads; i++) {
            salida = salida + " " + i + "->" + queues[i].size();
        }
        return salida;
    }
    
    public void shutdown() {
        for (int i = 0; i < threads; i++) {
            pool[i].shutdown();
        }
    }
}
