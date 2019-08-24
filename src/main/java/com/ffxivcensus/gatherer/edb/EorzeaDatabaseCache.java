package com.ffxivcensus.gatherer.edb;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ffxivcensus.gatherer.lodestone.LodestonePageLoader;
import com.ffxivcensus.gatherer.lodestone.ProductionLodestonePageLoader;

/**
 * Provides a wrapper around accessing Mounts & Minions from the web servers, caching responses so that we don't have to wait on HTTP
 * requests for anything we've already looked up.
 * 
 * @author matthew.hillier
 */
public class EorzeaDatabaseCache {

    private static final Logger LOG = LoggerFactory.getLogger(EorzeaDatabaseCache.class);
    private LodestonePageLoader loader = new ProductionLodestonePageLoader();
    private Map<String, String> minions = new ConcurrentHashMap<>();
    private Map<String, String> mounts = new ConcurrentHashMap<>();

    public String getMinionNameFromTooltip(String dataTooltipHref) throws IOException, InterruptedException {
        String id = dataTooltipHref.substring(dataTooltipHref.lastIndexOf("/") + 1);
        String name = minions.get(id);
        if(name == null) {
            Document doc = loader.getTooltipPage(dataTooltipHref);
            Elements headers = doc.getElementsByClass("minion__header__label");
            if(!headers.isEmpty()) {
                name = headers.get(0).text();
                minions.put(id, name);
                LOG.debug("Cached minion '" + name + "' under ID " + id);
            }
        }
        return name;
    }

    public String getMountNameFromTooltip(String dataTooltipHref) throws IOException, InterruptedException {
        String id = dataTooltipHref.substring(dataTooltipHref.lastIndexOf("/") + 1);
        String name = mounts.get(id);
        if(name == null) {
            Document doc = loader.getTooltipPage(dataTooltipHref);
            Elements headers = doc.getElementsByClass("mount__header__label");
            if(!headers.isEmpty()) {
                name = headers.get(0).text();
                mounts.put(id, name);
                LOG.debug("Cached mount '" + name + "' under ID " + id);
            }
        }
        return name;
    }

    public void setLodestonePageLoader(LodestonePageLoader loader) {
        this.loader = loader;
    }

}
