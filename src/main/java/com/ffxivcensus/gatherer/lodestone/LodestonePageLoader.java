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

}
