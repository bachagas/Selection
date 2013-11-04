/**
 * 
 */
package testgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author macurvello
 * Classe que lê um arquivo que representa uma instância de teste e retorna um array de números para ser lido pelo algoritmo.
 */

//TODO Different naming convention checks for A and B instances?

public class InstanceReader {

	private final String fileName;
	
	
	/**
	 * @param fileName
	 */
	public InstanceReader(String fileName) {
		this.fileName = fileName;
	}
	
	public InstanceReader(String fileNamePrefix, int instanceIndex) {
		this.fileName = fileNamePrefix + "_" + instanceIndex;
	}

	public int[] readInstanceA() throws IOException {
		final int instanceIndex = this.instanceIndex();
		final int arraySize = 1000 * (int) Math.pow(2, instanceIndex);
		
		int numArray[] = this.readInstance(arraySize);
		
		return numArray;
	}
	
	public int[] readInstanceB() throws IOException {
		final int arraySize = InstanceGeneratorB.INSTANCE_SIZE;
		
		int numArray[] = this.readInstance(arraySize);
		
		return numArray;
	}
	
	private int instanceIndex() throws IOException {
		String strArray[] = this.fileName.split(".+_");
		if( strArray.length != 2 ) {
			throw new IOException("Test instance file name ('" + this.fileName + "') does not follow test instance file naming convention. Expected: '.+_[0-9]+'");
		}
		
		final int instanceIndex;
		try {
			instanceIndex = Integer.parseInt(strArray[1]);
		}
		catch(NumberFormatException e) {
			throw new IOException("Test instance file name ('" + this.fileName + "') does not follow test instance file naming convention. Expected: '.+_[0-9]+'");
		}
		
		return instanceIndex;
	}
	
	private int[] readInstance(int arraySize) throws IOException {
		File instanceFile = new File(this.fileName);
		if( !instanceFile.exists() ) {
			throw new IOException("Test instance file '" + this.fileName + "' does not exist.");
		}
		
		FileInputStream fileInStream = new FileInputStream(instanceFile);
		InputStreamReader inStreamReader = new InputStreamReader(fileInStream);
		BufferedReader bufReader = new BufferedReader(inStreamReader);
		
		int numArray[] = new int[arraySize];
		int num;
		String line = bufReader.readLine();
		if( line == null ) {
			bufReader.close();
			inStreamReader.close();
			fileInStream.close();
			throw new IOException("Test instance file empty. File: '" + this.fileName + "'");
		}
		for( int i = 0; (line != null && i < arraySize); i++ ) {
			try {
				num = Integer.parseInt(line);
			}
			catch(NumberFormatException e) {
				bufReader.close();
				inStreamReader.close();
				fileInStream.close();
				throw new IOException("Test instance file contents unexpected. No valid integer found in line '" + (i + 1) + "', found '" + line + "' instead. File: '" + this.fileName + "'");
			}
			numArray[i] = num;
			line = bufReader.readLine();
			
			if( line == null && i + 1 < arraySize) {
				bufReader.close();
				inStreamReader.close();
				fileInStream.close();
				throw new IOException("Size of test instance unexpected. Size read: '" + (i + 1) + "', size expected: '" + arraySize + "'. File: '" + this.fileName + "'");
			}
		}
		
		bufReader.close();
		inStreamReader.close();
		fileInStream.close();
		
		return numArray;
	}
}

