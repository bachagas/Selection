package testgenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/*
 * Esta classe tem como objetivo gerar 15 inst�ncias de teste para os algoritmos implementados.
 * Uma inst�ncia de teste � simplesmente um array de tamanho n, contendo n n�meros naturais positivos. Esse array ser� representado por um arquivo com n linhas, cada uma contendo um n�mero.
 * A classe @InstanceReader ser� respons�vel por ler esses arquivos e entregar um array Java como entrada dos algoritmos implementados.
 * As inst�ncias de teste geradas por esta classe seguir�o as seguintes diretivas:
 * 	1 - A i-�sima inst�ncia ter� um tamanho n=1000*(2^i)
 *	2 - Cada inst�ncia conter� uma permuta��o aleat�ria dos n primeiros n�meros naturais positivos.
 *		Exemplos para n=4: [1,4,3,2], [4,2,1,3]
 */

public class InstanceGeneratorA {

	public static final int N_INSTANCES = 15; // Cuidado, este n�mero n�o pode ultrapassar 30, devido �s limita��es de armazenamento de um int
	
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
		
		// Array auxiliar que conter� os n�meros de 1 a size (por acaso ordenados).
		int[] auxArray = new int[size];
		for(int i = 0; i < size; i++) {
			auxArray[i] = i + 1;
		}
		
		for(int i = size; i > 0; i--) {
			// Randomiza��o do �ndice do vetor auxiliar
			int currRandomIndex = random.nextInt(i); // A cada loop, diminuo a range que vou procurar no array auxiliar
			
			int randomNum = auxArray[currRandomIndex];
			
			// Inicializa��o da posi��o utilizada com o elemento que, no pr�ximo loop, n�o far� parte do conjunto de n�meros a ser consultado pelo gerador aleat�rio. Para evitar repeti��o.
			auxArray[currRandomIndex] = auxArray[i - 1];
			
			// Escrevo o n�mero aleat�rio em uma linha do arquivo
			String randomNumberString = randomNum + "\n";
			byte[] byteArray = randomNumberString.getBytes();
			instanceFileOutStream.write(byteArray);
		}
		
		instanceFileOutStream.close();
		
	}
	
}
