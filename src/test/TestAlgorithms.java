package test;
import org.apache.commons.lang3.ArrayUtils;

import algorithm.Selection;
import algorithm.TimeoutException;

public class TestAlgorithms {
	static int exemplo[] = {5,4,3,2,1};
	static int v[];
	
	static int[] read_vector(String input)
	{
		String[] strArray = input.split(",");
		int[] intArray = new int[strArray.length];
		for(int i = 0; i < strArray.length; i++) {
		    intArray[i] = Integer.parseInt(strArray[i]);
		}
		return intArray;
	}
	
    public static void main(String[] args) {
        int alg = 0;
        int k = 0;
        v = exemplo;
        System.out.println("*** Trabalho 1 de PAA (2013.2) - Algoritmos de Selecao - 18/09/2013 ***");
        
        //Parser dos parametros:
        try {
        	for(int i = 0; i < args.length; i++) {
        		if (args[i].equalsIgnoreCase("-k")) k = Integer.parseInt(args[++i]);
    		    else if (args[i].equalsIgnoreCase("-a")) alg = alg | (1<<(Integer.parseInt(args[++i])-1));
    		    else if (args[i].equalsIgnoreCase("-v")) v = read_vector(args[++i]);
    		    else throw new Exception();
    		}
        	if (k ==0) throw new Exception();
        	if (alg == 0) alg = 0xF;
        } catch (Exception e) {
        	System.out.println("Uso: Select -k n [-a 1|2|3|4] [-v 1,2,3...]");
        	System.out.println("     -k para informar o elemento a elecionar (parametro obrigatorio)");
        	System.out.println("     -v para entrada do vetor separado por virgulas (nao informado = executa sobre vetor de exemplo {5,4,3,2,1} ) - Obs: nao usar espacos apos virgulas.");
        	System.out.println("     -a para selecao do(s) algoritmo(s) a ser(em) executado(s):");
        	System.out.println("        1 = K*Select");
        	System.out.println("        2 = SortSelect");
        	System.out.println("        3 = Kth-Smallest");
        	System.out.println("        4 = QuickSelect");
        	System.out.println("        5 = MergeSort");
        	System.out.println("        6 = InsertionSort");
        	System.out.println("        7 = QuickSort");
        	System.out.println("        8 = Partition");
        	System.out.println("        Nao informado = executa todos de selecao (1,2,3 e 4)");
            System.exit(1);
        }

        System.out.println("Vetor = " + ArrayUtils.toString(v));
        System.out.print("k = "); System.out.println(k);

		if (k>v.length) {
			System.out.println("ATENCAO: K deve ser <= tamanho do Vetor!");
			return;
		} else {
			try {
		        if ((alg & 1)>0) Selection.exec_alg(1, v, k);
		        if ((alg & 2)>0) Selection.exec_alg(2, v, k);
		        if ((alg & 4)>0) Selection.exec_alg(3, v, k);
		        if ((alg & 8)>0) Selection.exec_alg(4, v, k);
		        if ((alg & 16)>0) Selection.exec_alg(5, v, k);
		        if ((alg & 32)>0) Selection.exec_alg(6, v, k);
		        if ((alg & 64)>0) Selection.exec_alg(7, v, k);
		        if ((alg & 128)>0) Selection.exec_alg(8, v, k);
			}
			catch( TimeoutException e) {
				System.out.println("### Timeout ### k=" + k + ", n=" + v.length);
				
			}
		}
    }

}