/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package feign.remoting.connection.strategy;

import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import feign.remoting.config.ConfigManager;
import feign.remoting.connection.ConnectionPool;
import feign.remoting.connection.manager.DefaultConnectionManager;
import feign.remoting.util.NamedThreadFactory;
import feign.remoting.util.RunStateRecordedFutureTask;


/**
 *  A default connection monitor that handle connections with strategies
 *
 * @author tsui
 * @version $Id: DefaultConnectionMonitor.java, v 0.1 2017-02-21 12:09 tsui Exp $
 */
public class DefaultConnectionMonitor {

    private static final Logger logger = LoggerFactory.getLogger("DefaultConnectionMonitor");

    /** Connection pools to monitor */
    private DefaultConnectionManager    connectionManager;

    /** Monitor strategy */
    private ConnectionMonitorStrategy   strategy;

    private ScheduledThreadPoolExecutor executor;

    public DefaultConnectionMonitor(ConnectionMonitorStrategy strategy,
                                    DefaultConnectionManager connectionManager) {
        this.strategy = strategy;
        this.connectionManager = connectionManager;
    }

    /**
     * Start schedule task
     *
     */
    public void start() {
    	//初始化延时的时间，默认值是：10000ms
        long initialDelay = ConfigManager.conn_monitor_initial_delay();

        //执行的时间间隔，默认值是：180000ms
        long period = ConfigManager.conn_monitor_period();

        this.executor = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(
            "ConnectionMonitorThread", true), new ThreadPoolExecutor.AbortPolicy());
        MonitorTask monitorTask = new MonitorTask();
        //每隔6分钟，执行一次MonitorTask
        this.executor.scheduleAtFixedRate(monitorTask, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * cancel task and shutdown executor
     *
     * @throws Exception
     */
    public void destroy() {
        executor.purge();
        executor.shutdown();
    }

    /**
     * Monitor Task
     *
     * @author tsui
     * @version $Id: DefaultConnectionMonitor.java, v 0.1 2017-02-21 12:09 tsui Exp $
     */
    private class MonitorTask implements Runnable {
        /**
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                if (strategy != null) {
                	//获取所有连接池的异步任务
                    Map<String, RunStateRecordedFutureTask<ConnectionPool>> connPools = connectionManager.getConnPools();
                    strategy.monitor(connPools);
                }
            } catch (Exception e) {
                logger.warn("MonitorTask error", e);
            }
        }
    }
}
