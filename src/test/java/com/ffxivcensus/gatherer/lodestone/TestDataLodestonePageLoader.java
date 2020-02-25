package com.ffxivcensus.gatherer.lodestone;

import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TestDataLodestonePageLoader implements LodestonePageLoader {

    @Override
    public Document getCharacterPage(int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        Document doc;
        try {
            doc = Jsoup.parse(
                              new File(
                                       this.getClass().getResource(
                                                                   String.format("/data/lodestone/Character-%d.html", characterId))
                                           .toURI()),
                              null);
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return doc;
    }
    
    @Override
    public Document getClassJobPage(int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        Document doc;
        try {
            doc = Jsoup.parse(
                              new File(
                                       this.getClass().getResource(
                                                                   String.format("/data/lodestone/Character-%d-Class-Jobs.html", characterId))
                                           .toURI()),
                              null);
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return doc;
    }

    @Override
    public Document getMinionPage(int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        Document doc;
        try {
            doc = Jsoup.parse(
                              new File(
                                       this.getClass().getResource(
                                                                   String.format("/data/lodestone/Character-%d-Minions.html", characterId))
                                           .toURI()),
                              null);
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return doc;
    }

    @Override
    public Document getMountPage(int characterId) throws IOException, InterruptedException, CharacterDeletedException {
        Document doc;
        try {
            doc = Jsoup.parse(
                              new File(
                                       this.getClass().getResource(
                                                                   String.format("/data/lodestone/Character-%d-Mounts.html", characterId))
                                           .toURI()),
                              null);
        } catch(Exception e) {
            throw new RuntimeException();
        }
        return doc;
    }

    @Override
    public Document getTooltipPage(String href) throws IOException, InterruptedException {
        // TODO Provide a test stub here?
        return null;
    }

}
