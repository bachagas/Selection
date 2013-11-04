/**
 * 
 */
package test;

import garbagecollection.CollectGarbage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import testgenerator.InstanceGeneratorA;
import testgenerator.InstanceGeneratorB;
import testgenerator.InstanceReader;
import algorithm.Selection;
import algorithm.TimeoutException;

/**
 * @author macurvello
 *
 */
public class RunAlgorithms {

	private static final String TEST_INSTANCE_A_FILENAME_PREFIX = "test/instanceA";
	private static final String TEST_INSTANCE_B_FILENAME_PREFIX = "test/instanceB";
	public static final int N_TEST_RUNS = 10;
	public static final int N_KS = 4;
	public static final long RUN_TIME_TOLERANCE_MS = 60000;
	
	
	private static double log(double x, double base) {
		return Math.log(x)/Math.log(base);
	}
	
	private static double runAlgorithm(int algorithmID, int[] inputArray, int k) throws InterruptedException, ExecutionException {
		// Estrutura de controle de execução dos algoritmos em threads
		
		Collection<Double> runTimes = new ArrayList<Double>(RunAlgorithms.N_TEST_RUNS);
		// Roda algoritmo algorithmID N_TEST_RUNS vezes.
		int timeoutOccurrences = 0;
//		int[] kEsimo = new int[1];
//		kEsimo[0] = 0;
		int nRuns = 1;
		if( algorithmID == Selection.QUICK_SELECT ) {
			nRuns = RunAlgorithms.N_TEST_RUNS;
		}
		for( int testRunIndex = 0; testRunIndex < nRuns; testRunIndex++) {	
			double runTime = 0;
			long endTime = 0;
			CollectGarbage.collectGarbage();
			long startTime = (new Date()).getTime();			
			try{
				int kEsimo = Selection.exec_alg(algorithmID, inputArray, k);
				endTime = (new Date()).getTime();
				runTime = (double) (endTime - startTime);
				runTimes.add(runTime);
			}
			catch(TimeoutException e) {
				timeoutOccurrences++;
			}
		}
		
		double averageRunTime = 0.0;
		if( timeoutOccurrences == nRuns ) {
			averageRunTime = Double.POSITIVE_INFINITY;
		}
		else {
			// Extrai a média dos tempos
			double validRuns = (double) (nRuns - timeoutOccurrences);
			double runTimesSum = 0.0;
			Iterator<Double> iterator = runTimes.iterator();
			while ( iterator.hasNext() ) {
				double next = iterator.next();
				runTimesSum += next;
			}
			
			averageRunTime = runTimesSum/validRuns;
		}
		
		return averageRunTime;
	}
	
	private static Map<String, String> runAlgorithms(int inputArray[]) throws InterruptedException, ExecutionException {
		int instanceSize = inputArray.length;
		
		final int k1 = 5;
		final int k2 = (int) log(instanceSize, 2);
		final int k3 = (int) Math.sqrt(instanceSize);
		final int k4 = instanceSize/2;
		int k[] = new int[RunAlgorithms.N_KS];
		k[0] = k1;
		k[1] = k2;
		k[2] = k3;
		k[3] = k4;
		
		// Armazena a relação entre o algoritmo, o k usado e o tempo de execução médio.
		// As keys deste map são os IDs dos algoritmos. Cada value é uma dupla {k, tempoMedioDeExecucao} (por acaso, também representada por um map).
		Map<String, String> algorithmTimeMap = new HashMap<String, String>();
		
		// Execução do algoritmo kn-Select
		String size_time = "";
		for( int kIndex = 0; kIndex < RunAlgorithms.N_KS; kIndex++) {
			double averageRunTime = RunAlgorithms.runAlgorithm(Selection.KN_SELECT, inputArray, k[kIndex]);
			size_time += "\t" + inputArray.length + "\t" + averageRunTime;
			algorithmTimeMap.put(Selection.KN_SELECT + "\t" + k[kIndex], size_time);
			size_time = "";
		}
		System.out.println();
		
		// Execução do algoritmo Sort Select
		size_time = "";
		for( int kIndex = 0; kIndex < RunAlgorithms.N_KS; kIndex++) {
			double averageRunTime = RunAlgorithms.runAlgorithm(Selection.SORT_SELECT, inputArray, k[kIndex]);
			size_time += "\t" + inputArray.length + "\t" + averageRunTime;
			algorithmTimeMap.put(Selection.SORT_SELECT + "\t" + k[kIndex], size_time);
			size_time = "";
		}
		System.out.println();
		
		// Execução do algoritmo Kth Smallest
		size_time = "";
		for( int kIndex = 0; kIndex < RunAlgorithms.N_KS; kIndex++) {
			double averageRunTime = RunAlgorithms.runAlgorithm(Selection.KTH_SMALLEST, inputArray, k[kIndex]);
			size_time += "\t" + inputArray.length + "\t" + averageRunTime;
			algorithmTimeMap.put(Selection.KTH_SMALLEST + "\t" + k[kIndex], size_time);
			size_time = "";
		}
		System.out.println();
		
		// Execução do algoritmo Quick Select
		size_time = "";
		for( int kIndex = 0; kIndex < RunAlgorithms.N_KS; kIndex++) {
			double averageRunTime = RunAlgorithms.runAlgorithm(Selection.QUICK_SELECT, inputArray, k[kIndex]);
			size_time += "\t" + inputArray.length + "\t" + averageRunTime;
			algorithmTimeMap.put(Selection.QUICK_SELECT + "\t" + k[kIndex], size_time);
			size_time = "";
		}
		System.out.println();
		
		return algorithmTimeMap;
	}
	
	/**
	 * Este método persiste os resultados dos tempos medidos no seguinte formato:
	 * 1 - Para cada arquivo de instância de teste de entrada, existirá um arquivo de saída.
	 * 2 - Cada linha do arquivo de saída conterá: O ID do algoritmo, o k usado, o tempo médio de execução, separados por '\t'.
	 * 3 - Caso o tempo medido seja maior que a tolerância estipulada, o tempo será descrito pela string "Infinity", em vez de por um número
	 * 
	 * @param result
	 * @param instanceIndex
	 * @throws IOException
	 */
	private static void persistResult( String instanceType, Map<String, String> result, int instanceIndex ) throws IOException {
		if( instanceType != "A" && instanceType != "B" ) {
			throw new IOException("Unrecognized instance type: '" + instanceType + "'");
		}
		
		//Nome do arquivo de saida: <arquivo de entrada>.out.csv
		String outFileName = null;
		if( instanceType == "A" ) {
			outFileName = "output/instanceA_"+ instanceIndex + ".out.csv";
		}
		else {
			outFileName = "output/instanceB_"+ instanceIndex + ".out.csv";
		}
		
		File outFile = new File(outFileName);
		if( outFile.exists() ) {
			boolean fileDeletionSuccess = outFile.delete();
			if( !fileDeletionSuccess ) {
				throw new IOException("Could not delete previously created output file:'" + outFileName + "'");
			}
		}
		
		boolean fileCreationSuccess = outFile.createNewFile();
		if( !fileCreationSuccess ) {
			throw new IOException("Could not create output file:'" + outFileName + "'");
		}
		FileOutputStream outFileOutStream = new FileOutputStream(outFile);
		
		final String header = "Algorithm ID\tk\tInstance Size\tAverage Run Time\n";
		byte[] headerByteArray = header.getBytes();
		try {
			outFileOutStream.write(headerByteArray);
			
			Set<String> algorithmMapKeySet = result.keySet();
			Iterator<String> algorithmMapKeySetIt = algorithmMapKeySet.iterator();
			while( algorithmMapKeySetIt.hasNext() ) {
				String algorithmID_k = algorithmMapKeySetIt.next();
				
				String size_time = result.get(algorithmID_k);
				String resultLine = algorithmID_k + size_time + "\n";		
				byte[] resultLineByteArray = resultLine.getBytes();
				outFileOutStream.write(resultLineByteArray);	
			}
		}
		catch(IOException e) {
			outFileOutStream.close();
			throw e;
		}
		
		outFileOutStream.close();
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int[] inputArray = null;
		InstanceReader instanceReader = null;
		Map<String, String> resultA = null;
		Map<String, String> resultB = null;
		
		// Lendo instâncias de teste A
		for(int instanceIndex = 1; instanceIndex <= InstanceGeneratorA.N_INSTANCES; instanceIndex++ ) {
			instanceReader = new InstanceReader(RunAlgorithms.TEST_INSTANCE_A_FILENAME_PREFIX, instanceIndex);
			try {
				long startReadTime = (new Date()).getTime();
				System.out.println();
				System.out.println( "Começou a ler instância A" + instanceIndex + ". Tempo inicial: " + startReadTime );
				inputArray = instanceReader.readInstanceA();
				System.out.println( "Terminou de ler instância A" + instanceIndex + ". Tempo total: " + ((new Date()).getTime() - startReadTime) );
				System.out.println();
				
				try {
					resultA = RunAlgorithms.runAlgorithms(inputArray);
				}
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				catch(ExecutionException e) {
					e.printStackTrace();
				}
				
				try {
					// Persistência dos resultados em arquivos de saída
					RunAlgorithms.persistResult("A", resultA, instanceIndex);
				}
				catch(IOException e) {
					System.out.println("Could not generate output file for type-A instance '" + instanceIndex + "'. Error message: '" + e.getMessage() + "'");
					System.out.println("Skipping");
				}
			}
			catch(IOException e) {
				System.out.println("Could not read test intance '" + RunAlgorithms.TEST_INSTANCE_A_FILENAME_PREFIX + instanceIndex + "'. Error message: '" + e.getMessage() + "'");
				System.out.println("Skipping.");
			}
		}
		
		// Lendo instâncias de teste B
		for(int instanceIndex = 1; instanceIndex <= InstanceGeneratorB.N_INSTANCES; instanceIndex++ ) {
			instanceReader = new InstanceReader(RunAlgorithms.TEST_INSTANCE_B_FILENAME_PREFIX, instanceIndex);
			try {
				inputArray = instanceReader.readInstanceB();
				
				try {
					resultB = RunAlgorithms.runAlgorithms(inputArray);
				}
				catch(InterruptedException e) {
					//TODO
					e.printStackTrace();
				}
				catch(ExecutionException e) {
					//TODO
					e.printStackTrace();
				}
				
				try {
					// Persistência dos resultados em arquivos de saída
					RunAlgorithms.persistResult("B", resultB, instanceIndex);
				}
				catch(IOException e) {
					System.out.println("Could not generate output file for type-B instance '" + instanceIndex + "'. Error message: '" + e.getMessage() + "'");
					System.out.println("Skipping");
				}
			}
			catch(IOException e) {
				System.out.println("Could not read test intance '" + RunAlgorithms.TEST_INSTANCE_B_FILENAME_PREFIX + instanceIndex + "'. Error message: '" + e.getMessage() + "'");
				System.out.println("Skipping.");
			}
		}
		
	}

}
