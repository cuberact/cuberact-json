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
  <version>1.5.0</version>
</dependency>
```

##### Gradle

```groovy
compile 'org.cuberact:cuberact-json:1.5.0'
```

##### Ivy

```xml
<dependency org="org.cuberact" name="cuberact-json" rev="1.5.0">
  <artifact name="cuberact-json" type="jar" />
</dependency>
```

## Unit tests

100% classes and 100% lines covered 

## Benchmark

from [cuberact-json-benchmark](https://github.com/cuberact/cuberact-json-benchmark)

```

   CUBERACT_JSON - org.cuberact:cuberact-json:1.5.0
       FAST_JSON - com.alibaba:fastjson:1.2.38
JACKSON_DATABIND - com.fasterxml.jackson.core:jackson-databind:2.9.1
            GSON - com.google.code.gson:gson:2.8.2

--------------------------------------------
BENCHMARK - JSON DESERIALIZATION FROM STRING
--------------------------------------------
Warm up:
INPUT: JSON as String - data type: RANDOM, size: 1812474 chars
       CUBERACT_JSON - 0.011798873 sec [153.614 MegaChar/s] min/avg/max = 0.007388000 / 0.011799267 / 0.031743000 sec
           FAST_JSON - 0.016374044 sec [110.692 MegaChar/s] min/avg/max = 0.010680000 / 0.016375200 / 0.117183000 sec
    JACKSON_DATABIND - 0.017211777 sec [105.304 MegaChar/s] min/avg/max = 0.012360000 / 0.017212000 / 0.074239000 sec
                GSON - 0.020013233 sec [ 90.564 MegaChar/s] min/avg/max = 0.014984000 / 0.020013467 / 0.056991000 sec

System.gc() and sleep for 5 sec

BENCHMARK - iteration 1/1 ************************************************************
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /small-real-data.json, size: 14572 chars
       CUBERACT_JSON - 0.000299033 sec [ 48.730 MegaChar/s] min/avg/max = 0.000070000 / 0.000298570 / 0.004655000 sec
           FAST_JSON - 0.000427710 sec [ 34.070 MegaChar/s] min/avg/max = 0.000119000 / 0.000427240 / 0.003587000 sec
    JACKSON_DATABIND - 0.000326427 sec [ 44.641 MegaChar/s] min/avg/max = 0.000118000 / 0.000325880 / 0.002029000 sec
                GSON - 0.000401648 sec [ 36.281 MegaChar/s] min/avg/max = 0.000154000 / 0.000401200 / 0.001253000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /middle-real-data.json, size: 169076 chars
       CUBERACT_JSON - 0.001757149 sec [ 96.222 MegaChar/s] min/avg/max = 0.000620000 / 0.001756690 / 0.002355000 sec
           FAST_JSON - 0.002412595 sec [ 70.081 MegaChar/s] min/avg/max = 0.000937000 / 0.002412470 / 0.002827000 sec
    JACKSON_DATABIND - 0.001856387 sec [ 91.078 MegaChar/s] min/avg/max = 0.000698000 / 0.001855920 / 0.004239000 sec
                GSON - 0.002961214 sec [ 57.097 MegaChar/s] min/avg/max = 0.001021000 / 0.002961150 / 0.005403000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: FROM RESOURCE /big-real-data.json, size: 1861784 chars
       CUBERACT_JSON - 0.013002859 sec [143.183 MegaChar/s] min/avg/max = 0.008352000 / 0.013002720 / 0.023951000 sec
           FAST_JSON - 0.012421423 sec [149.885 MegaChar/s] min/avg/max = 0.008100000 / 0.012421260 / 0.023695000 sec
    JACKSON_DATABIND - 0.012190680 sec [152.722 MegaChar/s] min/avg/max = 0.006212000 / 0.012190480 / 0.019199000 sec
                GSON - 0.013716376 sec [135.734 MegaChar/s] min/avg/max = 0.008344000 / 0.013716360 / 0.026431000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 118 chars
       CUBERACT_JSON - 0.000038588 sec [  3.058 MegaChar/s] min/avg/max = 0.000013000 / 0.000038110 / 0.000100000 sec
           FAST_JSON - 0.000041302 sec [  2.857 MegaChar/s] min/avg/max = 0.000007000 / 0.000040810 / 0.000156000 sec
    JACKSON_DATABIND - 0.000083420 sec [  1.415 MegaChar/s] min/avg/max = 0.000030000 / 0.000082890 / 0.000220000 sec
                GSON - 0.000046455 sec [  2.540 MegaChar/s] min/avg/max = 0.000020000 / 0.000045970 / 0.000115000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 2002 chars
       CUBERACT_JSON - 0.000076281 sec [ 26.245 MegaChar/s] min/avg/max = 0.000024000 / 0.000075780 / 0.000149000 sec
           FAST_JSON - 0.000107896 sec [ 18.555 MegaChar/s] min/avg/max = 0.000046000 / 0.000107380 / 0.000221000 sec
    JACKSON_DATABIND - 0.000187412 sec [ 10.682 MegaChar/s] min/avg/max = 0.000074000 / 0.000186900 / 0.000458000 sec
                GSON - 0.000128730 sec [ 15.552 MegaChar/s] min/avg/max = 0.000041000 / 0.000128200 / 0.000196000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 14595 chars
       CUBERACT_JSON - 0.000287320 sec [ 50.797 MegaChar/s] min/avg/max = 0.000095000 / 0.000286840 / 0.000454000 sec
           FAST_JSON - 0.000419046 sec [ 34.829 MegaChar/s] min/avg/max = 0.000173000 / 0.000418540 / 0.000530000 sec
    JACKSON_DATABIND - 0.000453108 sec [ 32.211 MegaChar/s] min/avg/max = 0.000148000 / 0.000452610 / 0.003681000 sec
                GSON - 0.000462652 sec [ 31.546 MegaChar/s] min/avg/max = 0.000152000 / 0.000462190 / 0.000788000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 1488247 chars
       CUBERACT_JSON - 0.010348462 sec [143.813 MegaChar/s] min/avg/max = 0.006636000 / 0.010348300 / 0.020239000 sec
           FAST_JSON - 0.013220267 sec [112.573 MegaChar/s] min/avg/max = 0.009832000 / 0.013220240 / 0.025791000 sec
    JACKSON_DATABIND - 0.012540018 sec [118.680 MegaChar/s] min/avg/max = 0.010072000 / 0.012540160 / 0.025983000 sec
                GSON - 0.016001315 sec [ 93.008 MegaChar/s] min/avg/max = 0.011136000 / 0.016001080 / 0.028703000 sec
-------------------------------------------------------------------------------------
INPUT: JSON as String - data type: RANDOM, size: 14464760 chars
       CUBERACT_JSON - 0.060873279 sec [237.621 MegaChar/s] min/avg/max = 0.053824000 / 0.060875040 / 0.137727000 sec
           FAST_JSON - 0.086748212 sec [166.744 MegaChar/s] min/avg/max = 0.081216000 / 0.086752000 / 0.120191000 sec
    JACKSON_DATABIND - 0.111949464 sec [129.208 MegaChar/s] min/avg/max = 0.101568000 / 0.111948160 / 0.130687000 sec
                GSON - 0.111673074 sec [129.528 MegaChar/s] min/avg/max = 0.103168000 / 0.111672000 / 0.134911000 sec

RESULT: *****************************************************************************

1. [wins: 7]          CUBERACT_JSON - 0.086682971 sec [207.828 MegaChar/s] 
2. [wins: 0]              FAST_JSON - 0.115798451 sec [155.573 MegaChar/s] 
3. [wins: 1]       JACKSON_DATABIND - 0.139586916 sec [129.060 MegaChar/s] 
4. [wins: 0]                   GSON - 0.145391464 sec [123.908 MegaChar/s]
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