package org.cuberact.json;

import org.cuberact.json.builder.JsonBuilder;
import org.cuberact.json.builder.JsonBuilderTree;
import org.cuberact.json.number.NumberConverter;
import org.cuberact.json.number.NumberConverterLongDouble;

/**
 * @author Michal Nikodim (michal.nikodim@gmail.com)
 */
public class JsonGlobalConfig {

    public static JsonBuilder DEFAULT_BUILDER = JsonBuilderTree.REF;
    public static NumberConverter DEFAULT_NUMBER_CONVERTER = NumberConverterLongDouble.REF;
}
