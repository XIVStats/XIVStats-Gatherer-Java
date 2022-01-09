package com.ffxivcensus.gatherer.lodestone;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Interface describing a loader class that can produce {@link Document} objects from a given source.
 * Implementations of this interface should all focus on from a specific source, for example the live loadestone.
 *
 * @author matthew.hillier
 */
public interface LodestonePageLoader {

    /**
     * Fetches a Character page.
     * @param characterId
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws FetchYieldedPageNotFoundException
     */
    Document getCharacterPage(final int characterId) throws IOException, InterruptedException, FetchYieldedPageNotFoundException;

    /**
     * Fetches a Character's Class & Job info, where available.
     * @param characterId
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws FetchYieldedPageNotFoundException
     */
    Document getClassJobPage(final int characterId) throws IOException, InterruptedException, FetchYieldedPageNotFoundException;

    /**
     * Fetches a Characters Minions page, where available.
     * @param characterId
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws FetchYieldedPageNotFoundException
     */
    Document getMinionPage(final int characterId) throws IOException, InterruptedException, FetchYieldedPageNotFoundException;

    /**
     * Fetches a Characters Mounts page, where available.
     * @param characterId
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws FetchYieldedPageNotFoundException
     */
    Document getMountPage(final int characterId) throws IOException, InterruptedException, FetchYieldedPageNotFoundException;

    /**
     * Fetches a tooltop page from a 'data-tooltip_href' reference
     * @param href Value from the data-tooltip_href value
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    Document getTooltipPage(final String href) throws IOException, InterruptedException;

}
