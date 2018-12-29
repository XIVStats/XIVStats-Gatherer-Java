package com.ffxivcensus.gatherer.lodestone;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpStatus;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loadestone Page Loader that works with the live EU lodestone,
 * 
 * @author matthew.hillier
 */
public class ProductionLodestonePageLoader implements LodestonePageLoader {

    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(ProductionLodestonePageLoader.class);

    /**
     * Base URL used to fetch character data for.
     * Default to {@value}}
     */
    private String baseUrl = "http://eu.finalfantasyxiv.com/lodestone/character/%d/";

    /**
     * Fetches the given Character {@link Document} from the Lodestone.
     * 
     * @param characterId
     * @return A Jsoup Document object of the page.
     * @throws IOException
     * @throws InterruptedException
     * @throws CharacterDeletedException When the server returns a 404 response, a CharacterDeletedException will be thrown by this method.
     */
    @Override
    public Document getCharacterPage(final int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        int attempt = 1;
        Document doc;

        // URL to connect to
        String url = String.format(baseUrl, characterId);

        try {
            doc = Jsoup.connect(url).timeout(5000).get();
        } catch(HttpStatusException httpe) {
            switch (httpe.getStatusCode()) {
                case 429:
                    // Generate random number 1->20*attempt no and sleep for it
                    Random rand = new Random();
                    int max = attempt * 20;
                    int min = (attempt - 1) + 1;
                    int randomNum = rand.nextInt(max - min + 1) + min;
                    LOG.trace("Experiencing rate limiting (HTTP 429) while fetching id " + characterId + " (attempt " + attempt
                              + "), waiting " + randomNum + "ms then retrying...");
                    TimeUnit.MILLISECONDS.sleep(randomNum);
                    doc = getCharacterPage(characterId);
                    break;
                case HttpStatus.SC_NOT_FOUND:
                    LOG.info("Character {} does not exist. (404)", characterId);
                    throw new CharacterDeletedException();
                default:
                    throw new IOException("Unexpected HTTP Status Code: " + httpe.getStatusCode(), httpe);
            }
        }

        return doc;
    }

}
