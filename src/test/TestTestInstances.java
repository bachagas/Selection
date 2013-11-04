/**
 * 
 */
package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import testgenerator.InstanceReader;

/**
 * @author macurvello
 *
 */
public class TestTestInstances {

	/**
	 * @param args
	 * This program tests test instances integrity
	 */
	public static void main(String[] args) {
		// Testando instância A_2
		InstanceReader instReader = new InstanceReader("test/instanceA_2");
		try {
			int intArray[] = instReader.readInstanceA();
			final int size = 4000;
			List<Integer> list = new ArrayList<Integer>(size);
			for (int i = 0; i < intArray.length; i++) {
				int currInt = intArray[i];
				if( list.contains(currInt)) {
					System.out.println();
					System.out.println("Número repetido: '" + currInt + "'");
					return;
				}
				if( currInt < 1 || currInt > size ) {
					System.out.println();
					System.out.println("Número fora dos limites: '" + currInt + "'. Tamanho da instância: '" + size + "'");
					return;
				}
				
				list.add(currInt);
				System.out.print(intArray[i] + ",");
			}
			System.out.println();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		
		// Testando instância B_15
		instReader = new InstanceReader("test/instanceB_15");
		try {
			int intArray[] = instReader.readInstanceB();
			//final int size = InstanceGeneratorB.INSTANCE_SIZE;
			final int instanceIndex = 15;
			final int expectedAnomalies = (int) Math.pow(2, instanceIndex + 2);
			
			//List<Integer> list = new ArrayList<Integer>(size);
			int anomalyCount = 0;
			for (int i = 0; i < intArray.length; i++) {
				int currInt = intArray[i];
				/*
				if( list.contains(currInt)) {
					System.out.println();
					System.out.println("Número repetido: '" + currInt + "'");
					return;
				}
				if( currInt < 1 || currInt > size ) {
					System.out.println();
					System.out.println("Número fora dos limites: '" + currInt + "'. Tamanho da instância: '" + size + "'");
					return;
				}
				*/
				
				if( i + 1 < intArray.length && currInt + 1 != intArray[i+1] ) {
					anomalyCount++;
				}
				
				//list.add(currInt);
				//System.out.print(intArray[i] + ",");
			}
			System.out.println();
			
			// O número de "anomalias" esperadas não é necessariamente igual ao de anomalias encontradas, mas deve ser um número próximo.
			System.out.println("Expected anomalies: '" + expectedAnomalies + "', encountered anomalies: '" + anomalyCount + "'");
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

}
