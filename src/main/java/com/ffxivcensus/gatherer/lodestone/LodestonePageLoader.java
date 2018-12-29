package com.ffxivcensus.gatherer.lodestone;

import java.io.IOException;

import org.jsoup.nodes.Document;

public interface LodestonePageLoader {

    Document getCharacterPage(final int characterId) throws IOException, InterruptedException, CharacterDeletedException;

}
