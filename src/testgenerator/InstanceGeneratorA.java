package testgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/*
 * Esta classe tem como objetivo gerar 15 instâncias de teste para os algoritmos implementados.
 * Uma instância de teste é simplesmente um array de tamanho n, contendo n números naturais positivos. Esse array será representado por um arquivo com n linhas, cada uma contendo um número.
 * A classe @InstanceReader será responsável por ler esses arquivos e entregar um array Java como entrada dos algoritmos implementados.
 * As instâncias de teste geradas por esta classe seguirão as seguintes diretivas:
 * 	1 - A i-ésima instância terá um tamanho n=1000*(2^i)
 *	2 - Cada instância conterá uma permutação aleatória dos n primeiros números naturais positivos.
 *		Exemplos para n=4: [1,4,3,2], [4,2,1,3]
 */

public class InstanceGeneratorA {

	public static final int N_INSTANCES = 15; // Cuidado, este número não pode ultrapassar 30, devido às limitações de armazenamento de um int
	
	/**
	 * 
	 */
	public static void generateInstances() {
		int twoPower = 2;
		for( int i = 1; i <= N_INSTANCES; i++ ) {
			try {
				InstanceGeneratorA.generateInstance( i, 1000 * twoPower );
			}
			catch(IOException e) {
				System.out.println("Could not generate test instance of index '" + i + "'. Error message: " + e.getMessage());
				System.out.println("Stopping test instances generation.");
				return;
			}
			twoPower = twoPower * 2;
		}
		
	}
	
	private static void generateInstance(int instanceIndex, int size) throws IOException {
		//TODO Review file naming convention
		String instanceFileName = "test/instanceA_"+ instanceIndex;
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
		
		Date date = new Date();
		Random random = new Random(date.getTime());
		
		// Array auxiliar que conterá os números de 1 a size (por acaso ordenados).
		int[] auxArray = new int[size];
		for(int i = 0; i < size; i++) {
			auxArray[i] = i + 1;
		}
		
		for(int i = size; i > 0; i--) {
			// Randomização do índice do vetor auxiliar
			int currRandomIndex = random.nextInt(i); // A cada loop, diminuo a range que vou procurar no array auxiliar
			
			int randomNum = auxArray[currRandomIndex];
			
			// Inicialização da posição utilizada com o elemento que, no próximo loop, não fará parte do conjunto de números a ser consultado pelo gerador aleatório. Para evitar repetição.
			auxArray[currRandomIndex] = auxArray[i - 1];
			
			// Escrevo o número aleatório em uma linha do arquivo
			String randomNumberString = randomNum + "\n";
			byte[] byteArray = randomNumberString.getBytes();
			instanceFileOutStream.write(byteArray);
		}
		
		instanceFileOutStream.close();
		
	}
	
}
