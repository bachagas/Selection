package testgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Esta classe tem como objetivo gerar 25 instâncias de teste para os algoritmos implementados.
 * Uma instância de teste é simplesmente um array de tamanho n, contendo n números naturais positivos. Esse array será representado por um arquivo com n linhas, cada uma contendo um número.
 * A classe @InstanceReader será responsável por ler esses arquivos e entregar um array Java como entrada dos algoritmos implementados.
 * As instâncias de teste geradas por esta classe seguirão as seguintes diretivas:
 * 	1 - Todas as instâncias terão tamanho 2^25
 *	2 - Inicialmente, cada instância conterá os números naturais de 1 a 2^25 ordenados. 
 *	3 - A i-ésima instância fará 2^i trocas aleatórias de posições de números. O objetivo é ter instâncias mais ordenadas (i baixo) e instâncias menos ordenadas (i alto). 
 */

public class InstanceGeneratorB {
	public static final int N_INSTANCES = 25; // Cuidado, este número não pode ultrapassar 30, devido às limitações de armazenamento de um int
	public static final int INSTANCE_SIZE = (int) Math.pow(2, N_INSTANCES);
	private static final int SORTED_ARRAY[] = new int[INSTANCE_SIZE];
	
	/**
	 * 
	 */
	public static void generateInstances() {
		for( int i = 0; i < INSTANCE_SIZE; i++ ) {
			SORTED_ARRAY[i] = i + 1;
		}
		
		for( int i = 1; i <= N_INSTANCES; i++ ) {
			try {
				InstanceGeneratorB.generateInstance( i );
			}
			catch(IOException e) {
				System.out.println("Could not generate test instance of index '" + i + "'. Error message: " + e.getMessage());
				System.out.println("Stopping test instances generation.");
				return;
			}
		}
		
	}
	
	private static void generateInstance(int instanceIndex) throws IOException {
		//TODO Review file naming convention
		String instanceFileName = "test/instanceB_"+ instanceIndex;
		File instanceFile = new File(instanceFileName);
		if( instanceFile.exists() ) {
			boolean fileDeletionSuccess = instanceFile.delete();
			if( !fileDeletionSuccess ) {
				throw new IOException("Could not delete previously created instance file:'" + instanceFileName + "'");
			}
		}
		boolean fileCreationSuccess = instanceFile.createNewFile();
		if( !fileCreationSuccess ) {
			throw new IOException("Could not create instance file:'" + instanceFileName + "'");
		}
		FileOutputStream instanceFileOutStream = new FileOutputStream(instanceFile);
		
		int intArray[] = InstanceGeneratorB.generateArray(instanceIndex);
		for(int i = 0; i < INSTANCE_SIZE; i++) {
			String numberString = intArray[i] + "\n";
			byte[] byteArray = numberString.getBytes();
			instanceFileOutStream.write(byteArray);
		}
		
		instanceFileOutStream.close();
		
	}
	
	private static int[] generateArray(int instanceIndex) {
		Date date = new Date();
		Random random = new Random(date.getTime());
		
		//TODO rever o funcionamento desse copyOf
		int returnArray[] = Arrays.copyOf(SORTED_ARRAY, INSTANCE_SIZE);
		
		int nSwitches = (int) Math.pow(2, instanceIndex);
		for( int i = 0; i < nSwitches; i++ ) {
			int index1 = random.nextInt(INSTANCE_SIZE);
			int index2 = random.nextInt(INSTANCE_SIZE);
			
			int firstElement = returnArray[index1];
			returnArray[index1] = returnArray[index2];
			returnArray[index2] = firstElement;
		}
		
		return returnArray;
	}
}
