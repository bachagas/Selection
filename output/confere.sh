#!/usr/bin/bash

wc -l resultados.csv
cat instance*.out.csv | grep -v Algorithm | wc -l
