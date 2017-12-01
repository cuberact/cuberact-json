# cuberact-json

Fast JSON parser with highly customizable input, output and builder.

### Goals

- fully support [__rfc4627__](https://www.ietf.org/rfc/rfc4627.txt)
- fast json parse
- highly customizable (__input__, __builder__, __formatter__, __output__) 
- thread-safe (with default JsonBuilderTree)
- __JsonObject__ and __JsonArray__ are serializable
- working as __OSGi bundle__
- highly readable code
- small jar
- Java 8

## Examples

##### Parse to JsonObject and JsonArray

```java
JsonParser jsonParser = new JsonParser(); // this instance is thread-safe

JsonArray jsonArray = jsonParser.parse("[1,2,3,4]");
JsonObject jsonObject = jsonParser.parse("{\"name\" : \"John\", \"age\" : 19}");
Json json = jsonParser.parse(inputAsStream);

String prettyString = jsonArray.toString(JsonFormatter.PRETTY());
String packedString = jsonObject.toString(JsonFormatter.PACKED());
```

##### Parse from many types - JsonInput

```java
JsonParser parser = new JsonParser();

//as string - JsonInputCharSequence
Json json1 = parser.parse("[1]");

//as string builder - JsonInputCharSequence
Json json2 = parser.parse(new StringBuilder("[1]"));

//as char buffer - JsonInputCharSequence
Json json3 = parser.parse(CharBuffer.wrap("[1]"));

//as input stream - JsonInputReader
Json json4 = parser.parse(new StringReader("[1]"));

//as char array - custom JsonInputCharArray
Json json5 = parser.parse("[1]".toCharArray());
```

##### Custom JsonBuilder - parse to java.util.Map and java.util.List
```java
JsonBuilder customJsonBuilder = new JsonBuilder<Map<String, Object>, List<Object>>() {

    @Override
    public Map<String, Object> createObject() {
        return new LinkedHashMap<>();
    }

    @Override
    public List<Object> createArray() {
        return new ArrayList<>();
    }

    @Override
    public void addObjectToObject(Map<String, Object> object, String attr, Map<String, Object> subObject) {
        object.put(attr, subObject);
    }

    @Override
    public void addArrayToObject(Map<String, Object> object, String attr, List<Object> subArray) {
        object.put(attr, subArray);
    }

    @Override
    public void addStringToObject(Map<String, Object> object, String attr, String value) {
        object.put(attr, value);
    }

    @Override
    public void addBooleanToObject(Map<String, Object> object, String attr, Boolean value) {
        object.put(attr, value);
    }

    @Override
    public void addNullToObject(Map<String, Object> object, String attr) {
        object.put(attr, null);
    }

    @Override
    public void addNumberToObject(Map<String, Object> object, String attr, JsonNumber value) {
        object.put(attr, JsonNumberConverterIntFloat.REF.convert(value));
    }

    @Override
    public void addObjectToArray(List<Object> array, Map<String, Object> subObject) {
        array.add(subObject);
    }

    @Override
    public void addArrayToArray(List<Object> array, List<Object> subArray) {
        array.add(subArray);
    }

    @Override
    public void addStringToArray(List<Object> array, String value) {
        array.add(value);
    }

    @Override
    public void addBooleanToArray(List<Object> array, Boolean value) {
        array.add(value);
    }

    @Override
    public void addNullToArray(List<Object> array) {
        array.add(null);
    }

    @Override
    public void addNumberToArray(List<Object> array, JsonNumber value) {
        array.add(JsonNumberConverterIntFloat.REF.convert(value));
    }
};

JsonParser jsonParser = new JsonParser(customJsonBuilder);
Map<String, Object> map = jsonParser.parse("{ \"array\" : [1,2,3] }");
```

##### JsonOutput and JsonFormatter

```java
JsonParser jsonParser = new JsonParser(); // this instance is thread-safe
Json json = jsonParser.parse("{ \"array\" : [1,2,3] }");

String jsonAsString1 = json.toString(); // default is JsonFormatter.PRETTY();
String jsonAsString2 = json.toString(JsonFormatter.PRETTY());
String jsonAsString3 = json.toString(JsonFormatter.PACKED());

JsonOutput output = new JsonOutputStringBuilder();
json.toOutput(JsonFormatter.PRETTY(), output);
StringBuilder jsonAsStringBuilder = output.result();
```

## Configuration

##### Maven

```xml
<dependency>
  <groupId>org.cuberact</groupId>
  <artifactId>cuberact-json</artifactId>
  <version>1.2.0</version>
</dependency>
```

##### Gradle

```groovy
compile 'org.cuberact:cuberact-json:1.2.0'
```

##### Ivy

```xml
<dependency org="org.cuberact" name="cuberact-json" rev="1.2.0">
  <artifact name="cuberact-json" type="jar" />
</dependency>
```


## Benchmark

from [cuberact-json-benchmark](https://github.com/cuberact/cuberact-json-benchmark)

```

   CUBERACT_JSON - org.cuberact:cuberact-json:1.2.0
       FAST_JSON - com.alibaba:fastjson:1.2.38
JACKSON_DATABIND - com.fasterxml.jackson.core:jackson-databind:2.9.1
            GSON - com.google.code.gson:gson:2.8.2

--------------------------------------------
BENCHMARK - JSON DESERIALIZATION FROM STRING
--------------------------------------------
Warm up:
INPUT: JSON as String - data type: RANDOM, size: 1812474 chars
       CUBERACT_JSON - 0.012589924 sec [143.962 MegaChar/s] min/avg/max = 0.008140000 / 0.012590600 / 0.049663000 sec
           FAST_JSON - 0.018723549 sec [ 96.802 MegaChar/s] min/avg/max = 0.010384000 / 0.018723600 / 0.128447000 sec
    JACKSON_DATABIND - 0.018185014 sec [ 99.669 MegaChar/s] min/avg/max = 0.012872000 / 0.018183733 / 0.076671000 sec
                GSON - 0.019798526 sec [ 91.546 MegaChar/s] min/avg/max = 0.014816000 / 0.019798133 / 0.067647000 sec

System.gc() and sleep for 5 sec

BENCHMARK - iteration 1/1 ************************************************************
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /small-real-data.json, size: 14572 chars
       CUBERACT_JSON - 0.000371144 sec [ 39.262 MegaChar/s] min/avg/max = 0.000088000 / 0.000370680 / 0.002837000 sec
           FAST_JSON - 0.000587461 sec [ 24.805 MegaChar/s] min/avg/max = 0.000118000 / 0.000586990 / 0.008287000 sec
    JACKSON_DATABIND - 0.000405503 sec [ 35.936 MegaChar/s] min/avg/max = 0.000107000 / 0.000405040 / 0.004747000 sec
                GSON - 0.000474259 sec [ 30.726 MegaChar/s] min/avg/max = 0.000199000 / 0.000473740 / 0.001601000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /middle-real-data.json, size: 169076 chars
       CUBERACT_JSON - 0.002286637 sec [ 73.941 MegaChar/s] min/avg/max = 0.000617000 / 0.002286560 / 0.003069000 sec
           FAST_JSON - 0.003363936 sec [ 50.261 MegaChar/s] min/avg/max = 0.001503000 / 0.003363970 / 0.004139000 sec
    JACKSON_DATABIND - 0.002269286 sec [ 74.506 MegaChar/s] min/avg/max = 0.000621000 / 0.002269270 / 0.002905000 sec
                GSON - 0.003581370 sec [ 47.210 MegaChar/s] min/avg/max = 0.001815000 / 0.003581290 / 0.004115000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /big-real-data.json, size: 1861784 chars
       CUBERACT_JSON - 0.008477870 sec [219.605 MegaChar/s] min/avg/max = 0.006064000 / 0.008477680 / 0.025167000 sec
           FAST_JSON - 0.011212709 sec [166.042 MegaChar/s] min/avg/max = 0.008100000 / 0.011212380 / 0.031647000 sec
    JACKSON_DATABIND - 0.008776398 sec [212.135 MegaChar/s] min/avg/max = 0.005804000 / 0.008776280 / 0.021807000 sec
                GSON - 0.010682445 sec [174.284 MegaChar/s] min/avg/max = 0.008208000 / 0.010682560 / 0.031103000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 118 chars
       CUBERACT_JSON - 0.000033453 sec [  3.527 MegaChar/s] min/avg/max = 0.000011000 / 0.000032950 / 0.000091000 sec
           FAST_JSON - 0.000049809 sec [  2.369 MegaChar/s] min/avg/max = 0.000010000 / 0.000049250 / 0.000164000 sec
    JACKSON_DATABIND - 0.000113331 sec [  1.041 MegaChar/s] min/avg/max = 0.000036000 / 0.000112780 / 0.000259000 sec
                GSON - 0.000054996 sec [  2.146 MegaChar/s] min/avg/max = 0.000022000 / 0.000054570 / 0.000450000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 2002 chars
       CUBERACT_JSON - 0.000078550 sec [ 25.487 MegaChar/s] min/avg/max = 0.000030000 / 0.000078050 / 0.000184000 sec
           FAST_JSON - 0.000120039 sec [ 16.678 MegaChar/s] min/avg/max = 0.000084000 / 0.000119490 / 0.000283000 sec
    JACKSON_DATABIND - 0.000194682 sec [ 10.283 MegaChar/s] min/avg/max = 0.000102000 / 0.000194230 / 0.000477000 sec
                GSON - 0.000128877 sec [ 15.534 MegaChar/s] min/avg/max = 0.000048000 / 0.000128390 / 0.000309000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 14595 chars
       CUBERACT_JSON - 0.000301602 sec [ 48.392 MegaChar/s] min/avg/max = 0.000160000 / 0.000301100 / 0.000451000 sec
           FAST_JSON - 0.000434465 sec [ 33.593 MegaChar/s] min/avg/max = 0.000232000 / 0.000433950 / 0.000563000 sec
    JACKSON_DATABIND - 0.000522994 sec [ 27.907 MegaChar/s] min/avg/max = 0.000295000 / 0.000522510 / 0.001057000 sec
                GSON - 0.000597080 sec [ 24.444 MegaChar/s] min/avg/max = 0.000304000 / 0.000596570 / 0.004279000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 1488247 chars
       CUBERACT_JSON - 0.009662127 sec [154.029 MegaChar/s] min/avg/max = 0.006552000 / 0.009662320 / 0.027615000 sec
           FAST_JSON - 0.010298010 sec [144.518 MegaChar/s] min/avg/max = 0.008776000 / 0.010298120 / 0.028655000 sec
    JACKSON_DATABIND - 0.013591926 sec [109.495 MegaChar/s] min/avg/max = 0.008616000 / 0.013591760 / 0.034975000 sec
                GSON - 0.016073377 sec [ 92.591 MegaChar/s] min/avg/max = 0.011448000 / 0.016073680 / 0.036799000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 14464760 chars
       CUBERACT_JSON - 0.069896075 sec [206.947 MegaChar/s] min/avg/max = 0.064192000 / 0.069898080 / 0.115967000 sec
           FAST_JSON - 0.093160949 sec [155.266 MegaChar/s] min/avg/max = 0.088000000 / 0.093162880 / 0.116991000 sec
    JACKSON_DATABIND - 0.112016577 sec [129.131 MegaChar/s] min/avg/max = 0.104768000 / 0.112014400 / 0.143231000 sec
                GSON - 0.126488492 sec [114.356 MegaChar/s] min/avg/max = 0.117248000 / 0.126489920 / 0.149887000 sec

RESULT: *****************************************************************************

1. [wins: 7]          CUBERACT_JSON - 0.091107458 sec [197.735 MegaChar/s] 
2. [wins: 0]              FAST_JSON - 0.119227378 sec [151.099 MegaChar/s] 
3. [wins: 1]       JACKSON_DATABIND - 0.137890697 sec [130.648 MegaChar/s] 
4. [wins: 0]                   GSON - 0.158080896 sec [113.962 MegaChar/s]
```

## License

__cuberact-json__ is released under the [Apache 2.0 license](LICENSE).

```
Copyright 2017 Michal Nikodim

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```