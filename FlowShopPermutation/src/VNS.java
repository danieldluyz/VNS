import java.util.ArrayList;
import java.util.Random;


public class VNS {

	private Flowshop flowshop;
	private ListeJobs listeNEH;
	
	public VNS(Flowshop fs){
		flowshop = fs;
		listeNEH = fs.creerListeNEH();	
	}
	
	public ArrayList<ListeJobs> createNeighborhood(ListeJobs liste, int numOps){
		
		ArrayList<ListeJobs> neighborhood = new ArrayList<ListeJobs>();
		
		while (neighborhood.size()<numOps) {
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
				if (!neighborhood.contains(newListe)) neighborhood.add(newListe);
			}
			//Insert
			else {
				newListe = newListe.insert(newListe.getJob(j1), j2);
				if (!neighborhood.contains(newListe)) neighborhood.add(newListe);
			}
		}
		
		return neighborhood;
	}
	
	public ListeJobs NS1(ListeJobs list)
	{
		boolean [] revisee= new boolean[list.nombreJobs()];
		for(int i=0;i<revisee.length;i++)
		{
			revisee[i]=false;
		}
		
		ListeJobs resp= list.clone();
		
		boolean tous_revise=tous_r(revisee);
		
		while(tous_revise==false)
		{
			int r= (int)(Math.random()*(revisee.length));
			System.out.println("Voy a mover el : "+resp.getJob(r).getNumero());
			int auxx=resp.getJob(r).getNumero();
			boolean improve=false;
			Ordonnancement o= new Ordonnancement(resp, flowshop.getNbMachines());
			int actuel_Cmax=o.getDateDisponibilite(flowshop.getNbMachines()-1);
			System.out.println("Lista actual: ");
			resp.afficher();
			System.out.println("El Cmax actual es: "+ actuel_Cmax);
			if(revisee[r]==false)
			{
				revisee[r]=true;
				int n=0;
				while(improve==false && n<listeNEH.nombreJobs())
				{
					ListeJobs or=resp.clone();
					resp.insert(resp.getJob(r), n);
					n++;
					System.out.println("Mover el trabajo "+auxx+" a la posicion "+n);
					resp.afficher();
					Ordonnancement aux= new Ordonnancement(resp, flowshop.getNbMachines());
					int Cmax_aux= aux.getDateDisponibilite(flowshop.getNbMachines()-1);
					System.out.println("El nuevo Cmax es: "+ Cmax_aux);
					if(Cmax_aux>=actuel_Cmax)
					{
						resp=or;
					}
					else
					{
						improve=true;
					}
					System.out.println("Me quedo con : ");
					resp.afficher();
				}
			}
			else
			{
				r=(int)(Math.random()*(revisee.length-1));
			}
			tous_revise=tous_r(revisee);
		}
		Ordonnancement aux= new Ordonnancement(resp, flowshop.getNbMachines());
		int Cmax_aux= aux.getDateDisponibilite(flowshop.getNbMachines()-1);
		System.out.println("Lista final es:");
		resp.afficher();
		System.out.println("Con un Cmax de: "+Cmax_aux);
		return resp;
	}
	public boolean tous_r(boolean[] tb)
	{
		boolean resp=true;
		for(int i=0;i<tb.length;i++)
		{
			if(tb[i]==false)
			{
				resp=false;
			}
		}
		return resp;
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
		
//		System.out.println("Original : ");
//		vns.listeNEH.afficher();
		
//		ArrayList<ListeJobs> neighborhood = vns.createNeighborhood(vns.listeNEH, 10);
//		
//		System.out.println("Vecindario : ");
//		for (int i = 0; i < neighborhood.size(); i++) {
//			neighborhood.get(i).afficher();
//		}
		ListeJobs list=new ListeJobs();
		list.ajouterJob(j1);
		list.ajouterJob(j3);
		list.ajouterJob(j2);
		list.ajouterJob(j4);
		vns.NS1(list);
	}
	
}
