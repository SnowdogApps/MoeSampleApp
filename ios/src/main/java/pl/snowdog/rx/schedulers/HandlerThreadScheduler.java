package pl.snowdog.rx.schedulers;
/**
 * Copyright 2013 Netflix, Inc.
 * Copyright 2014 Ashley Williams
 * Copyright 2016 MattaKis Consulting Kft.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import apple.foundation.NSOperationQueue;
import rx.Scheduler;
import rx.Subscription;
import rx.functions.Action0;
import rx.internal.util.RxThreadFactory;
import rx.subscriptions.CompositeSubscription;
import rx.subscriptions.Subscriptions;

/**
 * Schedules actions to run on an iOS Handler thread.
 */
class HandlerThreadScheduler extends Scheduler {

    private static final String THREAD_PREFIX = "RxiOSScheduledExecutorPool-";
    private final NSOperationQueue operationQueue;

    public HandlerThreadScheduler(NSOperationQueue operationQueue) {
        this.operationQueue = operationQueue;
    }

    @Override
    public Worker createWorker() {
        return new HandlerThreadWorker(operationQueue);
    }

    private static class HandlerThreadWorker extends Worker {

        private final NSOperationQueue operationQueue;
        private final CompositeSubscription innerSubscription = new CompositeSubscription();

        HandlerThreadWorker(NSOperationQueue operationQueue) {
            this.operationQueue = operationQueue;
        }

        @Override
        public void unsubscribe() {
            innerSubscription.unsubscribe();
        }

        @Override
        public boolean isUnsubscribed() {
            return innerSubscription.isUnsubscribed();
        }

        @Override
        public Subscription schedule(final Action0 action, long delayTime, TimeUnit unit) {
            if (innerSubscription.isUnsubscribed()) {
                return Subscriptions.empty();
            }
            final ScheduledAction scheduledAction = new ScheduledAction(action, operationQueue);
            final ScheduledExecutorService executor = IOSScheduledExecutorPool.getInstance();
            Future<?> future;
            if (delayTime <= 0) {
                future = executor.submit(scheduledAction);
            } else {
                future = executor.schedule(scheduledAction, delayTime, unit);
            }
            scheduledAction.add(Subscriptions.from(future));
            scheduledAction.addParent(innerSubscription);
            return scheduledAction;
        }

        @Override
        public Subscription schedule(final Action0 action) {
            return schedule(action, 0, null);
        }
    }

    private static final class IOSScheduledExecutorPool {
        private static final RxThreadFactory THREAD_FACTORY = new RxThreadFactory(THREAD_PREFIX);
        private static final IOSScheduledExecutorPool INSTANCE = new IOSScheduledExecutorPool();
        private final ScheduledExecutorService executorService;

        private IOSScheduledExecutorPool() {
            executorService = Executors.newScheduledThreadPool(1, THREAD_FACTORY);
        }

        public static ScheduledExecutorService getInstance() {
            return INSTANCE.executorService;
        }
    }
}