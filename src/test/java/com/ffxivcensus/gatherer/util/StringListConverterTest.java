package com.ffxivcensus.gatherer.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StringListConverterTest {

    private StringListConverter converter;

    @Before
    public void setUp() {
        converter = new StringListConverter();
    }

    @After
    public void tearDown() {
        converter = null;
    }

    @Test
    public void testConvertToDatabaseColumn() {
        ArrayList<String> list = new ArrayList<>();
        list.add("first");
        list.add("second");
        list.add("third");

        String output = converter.convertToDatabaseColumn(list);

        assertEquals("first,second,third", output);
    }

    @Test
    public void testConvertToDatabaseColumnEmpty() {
        ArrayList<String> list = new ArrayList<>();

        String output = converter.convertToDatabaseColumn(list);

        assertEquals(null, output);
    }

    @Test
    public void testConvertToDatabaseColumnNull() {
        String output = converter.convertToDatabaseColumn(null);

        assertEquals(null, output);
    }

    @Test
    public void testConvertToEntityAttribute() {
        String input = "first,second,third";

        List<String> output = converter.convertToEntityAttribute(input);

        assertEquals(3, output.size());
        assertEquals("first", output.get(0));
        assertEquals("second", output.get(1));
        assertEquals("third", output.get(2));
    }

    @Test
    public void testConvertToEntityAttributeBlank() {
        String input = "";

        List<String> output = converter.convertToEntityAttribute(input);

        assertEquals(0, output.size());
    }

    @Test
    public void testConvertToEntityAttributeNull() {
        String input = null;

        List<String> output = converter.convertToEntityAttribute(input);

        assertEquals(0, output.size());
    }

}
