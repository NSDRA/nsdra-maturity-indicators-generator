# FAIR Maturity Indicators Generator for NanoSafety Data Reusability 

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/00de6e47d08749388312ffaf71080e56)](https://www.codacy.com/gh/ammar257ammar/nsdra-maturity-indicators-generator/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ammar257ammar/nsdra-maturity-indicators-generator&amp;utm_campaign=Badge_Grade) ![GitHub top language](https://img.shields.io/github/languages/top/ammar257ammar/nsdra-maturity-indicators-generator) ![Lines of code](https://img.shields.io/tokei/lines/github/ammar257ammar/nsdra-maturity-indicators-generator) ![GitHub](https://img.shields.io/github/license/ammar257ammar/nsdra-maturity-indicators-generator) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/ammar257ammar/nsdra-maturity-indicators-generator) [![DOI](https://zenodo.org/badge/363753906.svg)](https://zenodo.org/badge/latestdoi/363753906)

This repository aims at providing a tool to generate maturity indicators description (in Markdown and Nanopublication formats) from Java properties files and following the [FairMetrics](https://github.com/FAIRMetrics) templates.

## Usage

### Using the precompiled JAR

You can download the precompiled JAR file from the releases tab and run the tool using the following command:

```bash
java -jar maturity-indicators-generator-v1.0.jar -s SOURCE_PATH -d DEST_PATH
```

SOURCE_PATH: is the path of the maturity indicators defined as Java properties files. There is 11 lists of maturity indicators created to assess reusability of NanoSafety data can be downloaded from https://github.com/NSDRA/nsdra-maturity-indicators-props

DEST_PATH: is the path where the generated maturity indicators (Markdown & Nanopublication) will be placed, along with a JSON file describing the lists (needed for the JSON-LD generator web app).

### Build the project from source

1- clone this repository and "cd" into its directory ([maven](https://maven.apache.org/download.cgi) is needed for the following commands).

```bash
mvn package -DskipTests 
```

2- Run the built JAR file using the following command:

```bash
java -jar maturity-indicators-generator-v1.0-jar-with-dependencies.jar -s SOURCE_PATH -d DEST_PATH
```

SOURCE_PATH: is the path of the maturity indicators defined as Java properties files. There is 11 lists of maturity indicators created to assess reusability of NanoSafety data can be downloaded from https://github.com/NSDRA/nsdra-maturity-indicators-props

DEST_PATH: is the path where the generated maturity indicators (Markdown & Nanopublication) will be placed, along with a JSON file describing the lists (needed for the [JSON-LD generator web app](https://github.com/NSDRA/nsdra-jsonld-metadata-generator-webapp)).
