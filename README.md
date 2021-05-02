# FAIR Maturity Indicators Generator for NanoSafety Data Reusability 

This repository aims at providing a tool to generate maturity indicators description (in Markdown and Nanopublication formats) from Java properties files and following the [FairMetrics](https://github.com/FAIRMetrics) templates.



## Usage



### Using the precompiled JAR

You can download the precompiled JAR file from the releases tab and run the tool using the following command:

```bash
java -jar maturity-indicators-generator-v1.0.jar -s SOURCE_PATH -d DEST_PATH
```



SOURCE_PATH: is the path of the maturity indicators defined as Java properties files. There is 11 lists of maturity indicators created to assess reusability of NanoSafety data can be downloaded from https://github.com/ammar257ammar/fair-maturity-indicators-props

DEST_PATH: is the path where the generated maturity indicators (Markdown & Nanopublication) will be placed, along with a JSON file describing the lists (needed for the JSON-LD generator web app).



### Build the project from source

1- clone this repository and "cd" into its directory ([maven](https://maven.apache.org/download.cgi) is needed for the following commands).

```bash
mvn verify clean --fail-never
mvn package 
```



2- Run the built JAR file using the following command:

```bash
java -jar maturity-indicators-generator-v1.0-jar-with-dependencies.jar -s SOURCE_PATH -d DEST_PATH
```



SOURCE_PATH: is the path of the maturity indicators defined as Java properties files. There is 11 lists of maturity indicators created to assess reusability of NanoSafety data can be downloaded from https://github.com/ammar257ammar/fair-maturity-indicators-props

DEST_PATH: is the path where the generated maturity indicators (Markdown & Nanopublication) will be placed, along with a JSON file describing the lists (needed for the [JSON-LD generator web app](https://github.com/ammar257ammar/fair-maturity-indicators-jsonld-webapp)).