#!/usr/bin/bash

echo "Output, Instancia, Algoritmo, K, N, Tempo de exeucao"

for arq in $(ls -1 instance*.out.csv); do
  #echo $arq
  awk -F"\t" '
    BEGIN {
      OFS=","
    }
    {
      if (NR != 1) print FILENAME, substr(FILENAME,1,index(FILENAME,".")-1), $1, $2, $3, $4
    }' $arq
done
