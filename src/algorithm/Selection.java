package algorithm;
import java.util.Date;

import org.apache.commons.lang3.*;

import test.RunAlgorithms;

public class Selection {
	
	public static final int KN_SELECT = 1;
	public static final int SORT_SELECT = 2;
	public static final int KTH_SMALLEST = 3;
	public static final int QUICK_SELECT = 4;
	public static final int MERGE_SORT = 5;
	public static final int INSERTION_SORT = 6;
	public static final int QUICK_SORT = 7;
	public static final int PARTITION = 8;
	
	public static int exec_alg(int alg, int v[], int k) throws TimeoutException {
		int kesimo = 0;
		boolean selecionou = false, ordenou = false;
		long startReadTime = (new Date()).getTime();
		System.out.println( "Início da execução do algoritmo. Tempo inicial: " + startReadTime );
		switch( alg ) {
		    case 1:
		    	System.out.println("Executando algoritmo 1 - O(K*n) - K*Select");
				kesimo = kn_select(v, k);
				selecionou = true;
	            break;
	            
		    case 2:
		    	System.out.println("Executando algoritmo 2 - O(n*log n) - MergeSort e depois seleciona");
				kesimo = sort_select(v, k);
				selecionou = true;
	            break;
	            
		    case 3:
		    	System.out.println("Executando algoritmo 3 - O(n) - Mediana Das Medianas");
				kesimo = kth_smallest(v, k);
				selecionou = true;
	            break;
	            
		    case 4:
		    	System.out.println("Executando algoritmo 4 - O(n) - QuickSelect");
				kesimo = quick_select(v, k);
				selecionou = true;
	            break;
	            
		    case 5:
	            System.out.println("Executando algoritmo 5 - MergeSort - O(n*log n)");
        		merge_sort(v, 0, v.length-1);
        		ordenou = true;
	        	break;
	        	
		    case 6:
		    	System.out.println("Executando algoritmo 6 - InsertionSort - O(n^2)");
	        	insertion_sort(v, 0, v.length-1);
	        	ordenou = true;
	        	break;
	        	
		    case 7:
        		System.out.println("Executando algoritmo 7 - QuickSort - O(n^2) no pior caso; usa partition");
	        	quick_sort(v, 0, v.length-1);
	        	ordenou = true;
	        	break;
	        	
		    case 8:
	        	System.out.println("Executando algoritmo 8 - Partition");
	        	int p = partition(v, 0, v.length-1, k);
	        	System.out.println("O vetor particionado e " + ArrayUtils.toString(v) + " - o pivot e i=" + p);
	        	break;
	        	
		    default:
		    	System.out.println("Ops! Algoritmo nao definido...");
		}
		if (selecionou) System.out.println("O kesimo menor elemento e " + kesimo);
		if (ordenou) System.out.println("O vetor ordenado e " + ArrayUtils.toString(v));
		
		System.out.println( "Término da execução do algoritmo. Tempo total: " + ((new Date()).getTime() - startReadTime) );
		
		return kesimo;
	}
	
	public static int kn_select(int v[], int k) throws TimeoutException {
		/* Seleciona o k-esimo menor elemento de v em O(K*n) */
		long startTime = (new Date()).getTime();
		int imin = 0, j = 1, min = v[0];
		while (j <= k){
			imin = j-1;
			min = v[imin];
			for(int i = j-1; i < v.length; i++)
				if (v[i] < min) {
					min = v[i];
					imin = i;
				}
			//Troca j-esimo menor para a posicao j-1 do vetor:
			int temp = v[j-1];
			v[j-1] = v[imin];
			v[imin] = temp;
			j++;
			
			if( (new Date().getTime()) - startTime > RunAlgorithms.RUN_TIME_TOLERANCE_MS ) {
				throw new TimeoutException();
			}
		}
		return min;
	}
	
	public static int sort_select(int v[], int k) {
		/* Seleciona o k-esimo menor elemento de v em O(n*log n) ordenando o vetor com merge_sort */
		merge_sort(v,0, v.length-1);
		return v[k-1];
	}
	
	public static int kth_smallest(int v[], int k) {
		/* Seleciona o k-esimo menor elemento de v em O(n) usando a Mediana Das Medianas */
		if (v.length == 1) return v[0];
		else {
			// Step 1) Group the numbers into sets of 5:
			int groups_size = 5;
			int groups_median = (int) Math.ceil((double) groups_size / 2);
			int groups = (int) Math.ceil((double) v.length / groups_size);
			// Step 2) Sort individual groups and find the median of each group:
			for (int i=0; i<groups; i++)
					insertion_sort(v, i*groups_size, Math.min((i+1)*groups_size-1, v.length-1));
			int M[] = new int[groups];
			for (int i=0; i<groups; i++) M[i] = v[Math.min(i*groups_size+groups_median-1, v.length-1)];
			// Step 3) Let “M” be set of medians and find median of “M” using MedianOfMedian (MOM) = kth_smallest (M,|M|/2):
			int MoM = kth_smallest(M,(int) Math.ceil((double) M.length / 2));
			// Step 4) Partition original data around the MOM such that values less than it are in set “L” and values greater than it are in set “R”:
			int p = partition(v, 0, v.length-1, MoM);
			/* Step 5) If |L| = k-1, then return MOM else
			 *         If |L| > k-1, then return kth_smallest(L,k)
			 *         Else return kth_smallest(R,k-|L|-1) */
			if (p == k-1) return MoM;
			else if (p > k-1) return kth_smallest(ArrayUtils.subarray(v, 0, p), k);
			else return kth_smallest(ArrayUtils.subarray(v, p, v.length), k-p);
		}
	}
	
	public static int quick_select(int v[], int k) {
		/* Seleciona o k-esimo menor elemento de v em O(n) usando o particionamento com um pivot aleatorio */
		if (v.length == 1) return v[0];
		else {
			int pivot = (int) ( v.length * Math.random() );
			int p = partition(v, 0, v.length-1, v[pivot]);
			if (p == k-1) return v[pivot];
			else if (p > k-1) return quick_select(ArrayUtils.subarray(v, 0, p), k);
			else return quick_select(ArrayUtils.subarray(v, p, v.length), k-p);
		}
	}
	
	public static void merge_sort(int v[],int ini, int fim) {
        if (ini < fim) {
        	int meio = (int) Math.floor((fim - ini)/2);
            merge_sort(v,ini,ini+meio);
            merge_sort(v,ini+meio+1,fim);
            //Merge utilizando vetores temporarios:
            int v_esq[] = ArrayUtils.subarray(v, ini, ini+meio+1);
            int v_dir[] = ArrayUtils.subarray(v, ini+meio+1, fim+1);
            int esq = 0, dir = 0;
            for (int i=ini; i<=fim; i++) {
            	if (dir >= v_dir.length) v[i] = v_esq[esq++];
            	else if (esq >= v_esq.length) v[i] = v_dir[dir++];
            	else if (v_esq[esq] <= v_dir[dir]) v[i] = v_esq[esq++];
            	else v[i] = v_dir[dir++];
            }
        }
	}
	
	public static void insertion_sort(int v[], int ini, int fim) {
        for (int i=ini+1; i<=fim; i++) {
        	//insere v[i] no segmento já ordenado [0,i-1]:
        	int elem = v[i], j = i-1;
        	while ((j>=ini)&&(elem<v[j])) {
        		v[j+1] = v[j];
        		j--;
        	}
        	v[j+1] = elem;
        }
	}
	
	public static void quick_sort(int v[],int ini, int fim) {
        int meio;
        if (ini < fim) {
            meio = partition(v, ini, fim, v[(fim-ini)/2]);
            quick_sort(v, ini, meio);
            quick_sort(v, meio + 1, fim);
        }
	}
	
	public static int partition(int []A, int ini, int fim, int pivot) {
		/* Particiona "in place" (sem vetor temporario) o vetor A em duas partes:
		 * - todos os elementos <= pivot a esquerda de i
		 * - todos os elementos > pivot a direita de i
		 * - retorna o indice i do ultimo elemento <= ao pivot, com v[i] = pivot caso este exista no vetor
		 */
		//int x = A[pivot];
		int i = ini;
		int j = fim;
		while (i < j) {
			while ((j>=ini)&&(A[j] > pivot)) j--;
			while ((i<=fim)&&(A[i] < pivot)) i++;
			if (i < j) {
				int temp = A[i];
				A[i] = A[j];
				A[j] = temp;
			}
		}
		return Math.max(ini, j);
	}
 
}
