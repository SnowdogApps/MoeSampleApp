package pl.snowdog.rx.schedulers;
/**
 * Copyright 2014 Netflix, Inc.
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

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

import apple.foundation.NSBlockOperation;
import apple.foundation.NSOperationQueue;
import rx.Subscription;
import rx.exceptions.OnErrorNotImplementedException;
import rx.functions.Action0;
import rx.plugins.RxJavaPlugins;
import rx.subscriptions.CompositeSubscription;

/**
 * A {@code Runnable} that executes an {@code Action0} that can be cancelled.
 */
final class ScheduledAction implements Runnable, Subscription {

    private static final AtomicIntegerFieldUpdater<ScheduledAction> ONCE_UPDATER
            = AtomicIntegerFieldUpdater.newUpdater(ScheduledAction.class, "once");
    private final CompositeSubscription cancel;
    private final Action0 action;
    private final NSBlockOperation nsBlockOperation;
    private final NSOperationQueue operationQueue;
    volatile int once;

    ScheduledAction(Action0 action, NSOperationQueue operationQueue) {
        this.action = action;
        this.operationQueue = operationQueue;
        this.cancel = new CompositeSubscription();
        nsBlockOperation = NSBlockOperation.alloc().init();
    }

    @Override
    public void run() {
        nsBlockOperation.addExecutionBlock(new NSBlockOperation.Block_addExecutionBlock() {
            @Override
            public void call_addExecutionBlock() {
                try {
                    action.call();
                } catch (Throwable e) {
                    // nothing to do but print a System error as this is fatal and there is nowhere else to throw this
                    IllegalStateException ie;
                    if (e instanceof OnErrorNotImplementedException) {
                        ie = new IllegalStateException("Exception thrown on Scheduler.Worker thread. Add `onError` handling.", e);
                    } else {
                        ie = new IllegalStateException("Fatal Exception thrown on Scheduler.Worker thread.", e);
                    }
                    RxJavaPlugins.getInstance().getErrorHandler().handleError(ie);
                    Thread thread = Thread.currentThread();
                    thread.getUncaughtExceptionHandler().uncaughtException(thread, ie);
                } finally {
                    unsubscribe();
                }
            }
        });
        operationQueue.addOperation(nsBlockOperation);
    }

    @Override
    public void unsubscribe() {
        if (ONCE_UPDATER.compareAndSet(this, 0, 1)) {
            nsBlockOperation.cancel();
            cancel.unsubscribe();
        }
    }

    @Override
    public boolean isUnsubscribed() {
        return cancel.isUnsubscribed();
    }

    /**
     * Adds a {@code Subscription} to the {@link CompositeSubscription} to be later cancelled on
     * unsubscribe
     *
     * @param s subscription to add
     */
    void add(Subscription s) {
        cancel.add(s);
    }

    /**
     * Adds a parent {@link rx.subscriptions.CompositeSubscription} to this {@code ScheduledAction}
     * so when the action is cancelled or terminates, it can remove itself from this parent
     * @param parent the parent {@code CompositeSubscription} to add
     */
    void addParent(CompositeSubscription parent) {
        cancel.add(new Remover(this, parent));
    }

    /**
     * Remove a child subscription from a composite when unsubscribing.
     */
    private static final class Remover implements Subscription {

        static final AtomicIntegerFieldUpdater<Remover> ONCE_UPDATER
                = AtomicIntegerFieldUpdater.newUpdater(Remover.class, "once");
        final Subscription s;
        final CompositeSubscription parent;
        volatile int once;

        Remover(Subscription s, CompositeSubscription parent) {
            this.s = s;
            this.parent = parent;
        }

        @Override
        public boolean isUnsubscribed() {
            return s.isUnsubscribed();
        }

        @Override
        public void unsubscribe() {
            if (ONCE_UPDATER.compareAndSet(this, 0, 1)) {
                parent.remove(s);
            }
        }
    }
}
