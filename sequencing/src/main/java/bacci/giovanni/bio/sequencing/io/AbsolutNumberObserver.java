package bacci.giovanni.bio.sequencing.io;

public class AbsolutNumberObserver implements StreamerObserver {

	public void update(long seqNum) {
		if((seqNum % 10000) == 0){
			System.out.println("\rScanned " + seqNum + " sequences");
		}
		
	}

}
