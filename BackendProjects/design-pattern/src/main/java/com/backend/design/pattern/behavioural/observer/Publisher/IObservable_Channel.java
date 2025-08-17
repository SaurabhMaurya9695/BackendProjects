package com.backend.design.pattern.behavioural.observer.Publisher;

import com.backend.design.pattern.behavioural.observer.Subscriber.IObserver_Subscriber;

/**
 * The {@code IObservable_Channel} interface defines the contract for a publisher
 * in the Observer design pattern. Classes implementing this interface allow
 * subscribers to register, unregister, and receive notifications when the
 * observable state changes.
 *
 * <p>Implementations of this interface are responsible for maintaining a list
 * of observers and notifying them of any updates.</p>
 *
 * @author Saurabh
 * @see IObserver_Subscriber
 */
public interface IObservable_Channel {

    /**
     * Subscribes a new observer to this channel. The observer will receive
     * notifications when {@link #notifyChannel()} is called.
     *
     * @param subscriber the observer to be added to the subscription list
     */
    void subscribeChannel(IObserver_Subscriber subscriber);

    /**
     * Removes an existing observer from this channel. After removal, the
     * observer will no longer receive notifications.
     *
     * @param subscriber the observer to be removed from the subscription list
     */
    void removeChannel(IObserver_Subscriber subscriber);

    /**
     * Notifies all subscribed observers of a change or event in this channel.
     * This method should iterate over all current subscribers and call their
     * update method or equivalent.
     */
    void notifyChannel();
}
