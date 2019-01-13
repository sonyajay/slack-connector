package org.mule.extension.slack.internal.utils;

import org.mule.runtime.extension.api.runtime.streaming.PagingProvider;

import java.util.Iterator;
import java.util.List;

public class PagingProviderIterator<C, T> implements Iterator<T> {

    private final C connection;
    private final PagingProvider<C, T> pagingProvider;
    private List<T> currentPage;
    private int currentPos = 0;

    public PagingProviderIterator(C connection, PagingProvider<C, T> pagingProvider) {
        this.connection = connection;
        this.pagingProvider = pagingProvider;
    }

    @Override
    public boolean hasNext() {
        synchronized (pagingProvider) {
            if (currentPage == null || currentPos + 1 == currentPage.size()) {
                currentPage = pagingProvider.getPage(connection);
                currentPos = 0;
            }
        }

        return !currentPage.isEmpty();
    }

    @Override
    public T next() {
        synchronized (pagingProvider) {
            if (currentPos + 1 == currentPage.size()) {
                currentPage = pagingProvider.getPage(connection);
                currentPos = 0;
            }
        }
        return currentPage.get(currentPos++);
    }
}