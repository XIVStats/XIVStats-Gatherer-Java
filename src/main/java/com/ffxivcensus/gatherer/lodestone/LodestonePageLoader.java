package com.ffxivcensus.gatherer.lodestone;

import java.io.IOException;

import org.jsoup.nodes.Document;

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
     * @throws CharacterDeletedException
     */
    Document getCharacterPage(final int characterId) throws IOException, InterruptedException, CharacterDeletedException;
    
    /**
     * Fetches a Characters Minions page, where available.
     * @param characterId
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws CharacterDeletedException
     */
    Document getMinionPage(final int characterId) throws IOException, InterruptedException, CharacterDeletedException;
    
    /**
     * Fetches a Characters Mounts page, where available.
     * @param characterId
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws CharacterDeletedException
     */
    Document getMountPage(final int characterId) throws IOException, InterruptedException, CharacterDeletedException;
    
    /**
     * Fetches a tooltop page from a 'data-tooltip_href' reference 
     * @param href Value from the data-tooltip_href value
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    Document getTooltipPage(final String href) throws IOException, InterruptedException;

}
