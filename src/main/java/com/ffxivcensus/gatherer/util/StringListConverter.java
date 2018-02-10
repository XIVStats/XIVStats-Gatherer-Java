package com.ffxivcensus.gatherer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;

/**
 * Converter class for JPA 2.1 that converts a String list into a Comma-Separated string for the purposes of mashalling and unmarshalling
 * from a single database column.
 * Used during JPA transactions with MySQL.
 * 
 * @author matthew.hillier
 */
public class StringListConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(final List<String> attribute) {
        return attribute != null ? String.join(",", attribute) : null;
    }

    @Override
    public List<String> convertToEntityAttribute(final String dbData) {
        return dbData != null ? new ArrayList<>(Arrays.asList(dbData.split(","))) : new ArrayList<>();
    }

}
