package com.ffxivcensus.gatherer.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Factory class to create new Gatherer beans which are still managed by the IoC container.
 * 
 * @author matthew.hillier
 */
@Service
public class TaskFactory {

    @Autowired
    private ApplicationContext context;

    /**
     * Creates a new Gatherer object.
     * 
     * @return New Gatherer.
     */
    public GathererTask createGatherer() {
        return (GathererTask) context.getBean("gatherer");
    }

}
