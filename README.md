# cuberact-json

Fast JSON parser with highly customizable input, output and builder.

### Goals

- fully support [__rfc4627__](https://www.ietf.org/rfc/rfc4627.txt)
- fast json parse
- highly customizable (__input__, __output__, __builder__) 
- thread-safe (with default JsonBuilderDom)
- __JsonObject__ and __JsonArray__ are serializable
- working as __OSGi bundle__
- highly readable code
- small jar (<50kB)
- no dependencies on third party code
- Java 8

## Examples

##### Parse to DOM

```java
JsonParser jsonParser = new JsonParser(); // this instance is thread-safe

Json json = jsonParser.parse("[1,2,3,4]");  //json is JsonArray
Json json = jsonParser.parse("{\"name\" : \"John\", \"age\" : 19}"); //json is JsonObject
Json json = jsonParser.parse(inputAsStream);

String prettyJsonAsString = jsonArray.toString(JsonFormatter.PRETTY());
String packedJsonAsString = jsonObject.toString(JsonFormatter.PACKED());
```

##### Parse inputs

```java
JsonParser parser = new JsonParser();

//String - JsonInputCharSequence will be used
Json json1 = parser.parse("[1]");

//StringBuilder - JsonInputCharSequence will be used
Json json2 = parser.parse(new StringBuilder("[1]"));

//CharBuffer - JsonInputCharSequence will be used
Json json3 = parser.parse(CharBuffer.wrap("[1]"));

//InputStream - JsonInputReader will be used
Json json4 = parser.parse(new StringReader("[1]"));

//char[] - JsonInputCharArray will be used
Json json5 = parser.parse("[1]".toCharArray());
```

##### Output and formatter

```java
JsonParser jsonParser = new JsonParser(); // this instance is thread-safe
Json json = jsonParser.parse("{ \"array\" : [1,2,3] }");

String jsonAsString1 = json.toString(); // default is JsonFormatter.PRETTY();
String jsonAsString2 = json.toString(JsonFormatter.PRETTY());
String jsonAsString3 = json.toString(JsonFormatter.PACKED());

JsonOutput output = new JsonOutputStringBuilder();
json.toOutput(JsonFormatter.PRETTY(), output);
StringBuilder jsonAsStringBuilder = output.getResult();
```

## Configuration

##### Maven

```xml
<dependency>
  <groupId>org.cuberact</groupId>
  <artifactId>cuberact-json</artifactId>
  <version>1.4.0</version>
</dependency>
```

##### Gradle

```groovy
compile 'org.cuberact:cuberact-json:1.4.0'
```

##### Ivy

```xml
<dependency org="org.cuberact" name="cuberact-json" rev="1.4.0">
  <artifact name="cuberact-json" type="jar" />
</dependency>
```

## Unit tests

100% classes, 100% lines covered 

## Benchmark

from [cuberact-json-benchmark](https://github.com/cuberact/cuberact-json-benchmark)

```

   CUBERACT_JSON - org.cuberact:cuberact-json:1.3.0
       FAST_JSON - com.alibaba:fastjson:1.2.38
JACKSON_DATABIND - com.fasterxml.jackson.core:jackson-databind:2.9.1
            GSON - com.google.code.gson:gson:2.8.2

--------------------------------------------
BENCHMARK - JSON DESERIALIZATION FROM STRING
--------------------------------------------
Warm up:
INPUT: JSON as String - data type: RANDOM, size: 1812474 chars
       CUBERACT_JSON - 0.010620712 sec [170.655 MegaChar/s] min/avg/max = 0.008120000 / 0.010620600 / 0.033439000 sec
           FAST_JSON - 0.016158347 sec [112.170 MegaChar/s] min/avg/max = 0.010520000 / 0.016159467 / 0.107519000 sec
    JACKSON_DATABIND - 0.017382380 sec [104.271 MegaChar/s] min/avg/max = 0.012312000 / 0.017381867 / 0.069055000 sec
                GSON - 0.018951908 sec [ 95.635 MegaChar/s] min/avg/max = 0.014576000 / 0.018951467 / 0.067391000 sec

System.gc() and sleep for 5 sec

BENCHMARK - iteration 1/1 ************************************************************
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /small-real-data.json, size: 14572 chars
       CUBERACT_JSON - 0.000240367 sec [ 60.624 MegaChar/s] min/avg/max = 0.000081000 / 0.000239900 / 0.001224000 sec
           FAST_JSON - 0.000348077 sec [ 41.864 MegaChar/s] min/avg/max = 0.000111000 / 0.000347630 / 0.003821000 sec
    JACKSON_DATABIND - 0.000271419 sec [ 53.688 MegaChar/s] min/avg/max = 0.000136000 / 0.000270940 / 0.001485000 sec
                GSON - 0.000293293 sec [ 49.684 MegaChar/s] min/avg/max = 0.000118000 / 0.000292810 / 0.000850000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /middle-real-data.json, size: 169076 chars
       CUBERACT_JSON - 0.001137122 sec [148.688 MegaChar/s] min/avg/max = 0.000577000 / 0.001136670 / 0.004675000 sec
           FAST_JSON - 0.001731583 sec [ 97.642 MegaChar/s] min/avg/max = 0.000716000 / 0.001731170 / 0.005339000 sec
    JACKSON_DATABIND - 0.001116286 sec [151.463 MegaChar/s] min/avg/max = 0.000555000 / 0.001115820 / 0.002657000 sec
                GSON - 0.001756006 sec [ 96.284 MegaChar/s] min/avg/max = 0.001180000 / 0.001755580 / 0.004295000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /big-real-data.json, size: 1861784 chars
       CUBERACT_JSON - 0.006664650 sec [279.352 MegaChar/s] min/avg/max = 0.005668000 / 0.006664660 / 0.018479000 sec
           FAST_JSON - 0.009360177 sec [198.905 MegaChar/s] min/avg/max = 0.007028000 / 0.009360200 / 0.036095000 sec
    JACKSON_DATABIND - 0.006629972 sec [280.813 MegaChar/s] min/avg/max = 0.005260000 / 0.006629660 / 0.023503000 sec
                GSON - 0.009993983 sec [186.290 MegaChar/s] min/avg/max = 0.008864000 / 0.009994040 / 0.025775000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 118 chars
       CUBERACT_JSON - 0.000031869 sec [  3.703 MegaChar/s] min/avg/max = 0.000006000 / 0.000031360 / 0.000097000 sec
           FAST_JSON - 0.000032414 sec [  3.640 MegaChar/s] min/avg/max = 0.000008000 / 0.000031920 / 0.000213000 sec
    JACKSON_DATABIND - 0.000102147 sec [  1.155 MegaChar/s] min/avg/max = 0.000041000 / 0.000101680 / 0.000197000 sec
                GSON - 0.000045289 sec [  2.605 MegaChar/s] min/avg/max = 0.000018000 / 0.000044840 / 0.000128000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 2002 chars
       CUBERACT_JSON - 0.000068768 sec [ 29.112 MegaChar/s] min/avg/max = 0.000020000 / 0.000068240 / 0.000173000 sec
           FAST_JSON - 0.000098263 sec [ 20.374 MegaChar/s] min/avg/max = 0.000036000 / 0.000097730 / 0.000246000 sec
    JACKSON_DATABIND - 0.000142123 sec [ 14.086 MegaChar/s] min/avg/max = 0.000054000 / 0.000141640 / 0.000497000 sec
                GSON - 0.000099535 sec [ 20.114 MegaChar/s] min/avg/max = 0.000044000 / 0.000099080 / 0.000146000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 14595 chars
       CUBERACT_JSON - 0.000199683 sec [ 73.091 MegaChar/s] min/avg/max = 0.000117000 / 0.000199190 / 0.000457000 sec
           FAST_JSON - 0.000341835 sec [ 42.696 MegaChar/s] min/avg/max = 0.000145000 / 0.000341340 / 0.000561000 sec
    JACKSON_DATABIND - 0.000304217 sec [ 47.976 MegaChar/s] min/avg/max = 0.000134000 / 0.000303730 / 0.001131000 sec
                GSON - 0.000427626 sec [ 34.130 MegaChar/s] min/avg/max = 0.000297000 / 0.000427080 / 0.000777000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 1488247 chars
       CUBERACT_JSON - 0.007698602 sec [193.314 MegaChar/s] min/avg/max = 0.006248000 / 0.007698660 / 0.025807000 sec
           FAST_JSON - 0.010835532 sec [137.349 MegaChar/s] min/avg/max = 0.008440000 / 0.010835760 / 0.031855000 sec
    JACKSON_DATABIND - 0.010372973 sec [143.474 MegaChar/s] min/avg/max = 0.008744000 / 0.010372600 / 0.029423000 sec
                GSON - 0.013286424 sec [112.013 MegaChar/s] min/avg/max = 0.011104000 / 0.013287000 / 0.032399000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 14464760 chars
       CUBERACT_JSON - 0.067420139 sec [214.547 MegaChar/s] min/avg/max = 0.063552000 / 0.067422400 / 0.090623000 sec
           FAST_JSON - 0.089333917 sec [161.918 MegaChar/s] min/avg/max = 0.084160000 / 0.089333120 / 0.119871000 sec
    JACKSON_DATABIND - 0.112141875 sec [128.986 MegaChar/s] min/avg/max = 0.105792000 / 0.112144640 / 0.132735000 sec
                GSON - 0.124362672 sec [116.311 MegaChar/s] min/avg/max = 0.119424000 / 0.124362880 / 0.147199000 sec

RESULT: *****************************************************************************

1. [wins: 6]          CUBERACT_JSON - 0.083461200 sec [215.851 MegaChar/s]
2. [wins: 0]              FAST_JSON - 0.112081798 sec [160.732 MegaChar/s]
3. [wins: 2]       JACKSON_DATABIND - 0.131081012 sec [137.435 MegaChar/s]
4. [wins: 0]                   GSON - 0.150264828 sec [119.889 MegaChar/s]
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