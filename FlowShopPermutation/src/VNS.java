import java.util.ArrayList;


public class VNS {

	private Flowshop flowshop;
	private ListeJobs listeNEH;
	
	public VNS(Flowshop fs){
		flowshop = fs;
		listeNEH = fs.creerListeNEH();	
	}
	
	public ArrayList<ListeJobs> createNeighborhood(ListeJobs liste, int numOps){
		
		ArrayList<ListeJobs> neighborhood = new ArrayList<ListeJobs>();
		
		for (int i = 0; i < numOps; i++) {
			int j1 = (int) (Math.random()*liste.nombreJobs());
			int j2 = (int) (Math.random()*liste.nombreJobs());
			
			while(j1==j2){
				j2 = (int) (Math.random()*liste.nombreJobs());
			}
			
			// We assign different probabilities to the swap and insert methods as we read on [1]
			int op = (int) (Math.random()*3);
			
			ListeJobs newListe = liste.clone();
			
			//Swap
			if (op==0){
				newListe = newListe.swap(j1, j2);
				neighborhood.add(newListe);
			}
			//Insert
			else {
				newListe = newListe.insert(newListe.getJob(j1), j2);
				neighborhood.add(newListe);
			}
	
		}
		
		return neighborhood;
	}
	
	public ListeJobs localMinimum(ListeJobs neighborhood) {
		
		Ordonnancement ordonnancement = new Ordonnancement(neighborhood, flowshop.getNbMachines());
		
		return null;
	}
	
	public static void main(String[] args) {
		
		Job j1 = new Job(1, new int[]{5,9,8,10,1});
		Job j2 = new Job(2, new int[]{9,3,10,1,8});
		Job j3 = new Job(3, new int[]{9,4,5,8,6});
		Job j4 = new Job(4, new int[]{4,8,8,7,2});
		
		Flowshop fs = new Flowshop(new Job[]{j1,j2,j3,j4}, 5);
		
		VNS vns = new VNS(fs);
		
		System.out.println("Original : ");
		vns.listeNEH.afficher();
		
		ArrayList<ListeJobs> neighborhood = vns.createNeighborhood(vns.listeNEH, 10);
		
		System.out.println("Vecindario : ");
		for (int i = 0; i < neighborhood.size(); i++) {
			neighborhood.get(i).afficher();
		}
		
	}
	
}
