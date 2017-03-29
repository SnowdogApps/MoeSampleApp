package pl.snowdog.rx.schedulers;

import apple.foundation.NSOperationQueue;
import rx.Scheduler;

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
 * <p>
 * import ios.foundation.NSOperationQueue;
 * import rx.Scheduler;
 * <p>
 * /**
 * Static factory methods for creating Schedulers.
 */
public class IOSSchedulers {

    private static final Scheduler MAIN_THREAD_SCHEDULER =
            new HandlerThreadScheduler((NSOperationQueue) NSOperationQueue.mainQueue());

    private IOSSchedulers() {
    }

    /**
     * Converts an {@link NSOperationQueue} into a new Scheduler instance.
     *
     * @param operationQueue the operationQueue to wrap
     * @return the new Scheduler wrapping the NSOperationQueue
     */
    public static Scheduler handlerThread(final NSOperationQueue operationQueue) {
        return new HandlerThreadScheduler(operationQueue);
    }

    /**
     * Creates and returns a {@link Scheduler} that executes work on the main thread.
     *
     * @return a {@link Scheduler} that queues work on the main thread
     */
    public static Scheduler mainThread() {
        return MAIN_THREAD_SCHEDULER;
    }
}