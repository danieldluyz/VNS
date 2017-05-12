import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
	
	public ListeJobs ns1(ListeJobs list)
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
//			System.out.println("Voy a mover el : "+resp.getJob(r).getNumero());
//			int auxx=resp.getJob(r).getNumero();
			boolean improve=false;
			Ordonnancement o= new Ordonnancement(resp, flowshop.getNbMachines());
			int actuel_Cmax=o.getDateDisponibilite(flowshop.getNbMachines()-1);
//			System.out.println("Lista actual: ");
//			resp.afficher();
//			System.out.println("El Cmax actual es: "+ actuel_Cmax);
			if(revisee[r]==false)
			{
				revisee[r]=true;
				int n=0;
				while(improve==false && n<listeNEH.nombreJobs())
				{
					ListeJobs or=resp.clone();
					resp.insert(resp.getJob(r), n);
					n++;
//					System.out.println("Mover el trabajo "+auxx+" a la posicion "+n);
//					resp.afficher();
					Ordonnancement aux= new Ordonnancement(resp, flowshop.getNbMachines());
					int Cmax_aux= aux.getDateDisponibilite(flowshop.getNbMachines()-1);
//					System.out.println("El nuevo Cmax es: "+ Cmax_aux);
					if(Cmax_aux>=actuel_Cmax)
					{
						resp=or;
					}
					else
					{
						improve=true;
					}
//					System.out.println("Me quedo con : ");
//					resp.afficher();
				}
			}
			else
			{
				r=(int)(Math.random()*(revisee.length-1));
			}
			tous_revise=tous_r(revisee);
		}
//		Ordonnancement aux= new Ordonnancement(resp, flowshop.getNbMachines());
//		int Cmax_aux= aux.getDateDisponibilite(flowshop.getNbMachines()-1);
//		System.out.println("Lista final es:");
//		resp.afficher();
//		System.out.println("Con un Cmax de: "+Cmax_aux);
		return resp;
	}
	
	public ListeJobs ns2(ListeJobs listeOriginal){
		Ordonnancement ordonnancementOriginal = new Ordonnancement(listeOriginal, flowshop.getNbMachines());
//		System.out.println(ordonnancementOriginal.getDateDisponibilite(flowshop.getNbMachines()-1));
		int pJob = 0;
		ListeJobs liste2 = listeOriginal.clone();
		while (pJob < listeOriginal.nombreJobs()) {
			int sJob = pJob + 1;
			while (sJob < listeOriginal.nombreJobs()) {
				// Positions
				int j1 = (int) (Math.random()*listeOriginal.nombreJobs());
				int j2 = (int) (Math.random()*listeOriginal.nombreJobs());
				
				while(j1==j2){
					j2 = (int) (Math.random()*listeOriginal.nombreJobs());
				}
				
				ListeJobs newListe = listeOriginal.clone();
				
//				System.out.println("pJob : "+pJob+", j1 : "+j1+", sJob : "+sJob+", j2 : "+j2);
				
				newListe = newListe.insert(listeOriginal.getJob(pJob), j1);
				newListe = newListe.insert(listeOriginal.getJob(sJob), j2);
				
//				newListe.afficher();
				
				Ordonnancement nouveauOrdonnancement = new Ordonnancement(newListe, flowshop.getNbMachines());
//				System.out.println("***"+nouveauOrdonnancement.getDateDisponibilite(flowshop.getNbMachines()-1));
				
				if (nouveauOrdonnancement.getDateDisponibilite(flowshop.getNbMachines()-1) < ordonnancementOriginal.getDateDisponibilite(flowshop.getNbMachines()-1)) {
					liste2 = newListe.clone();
					sJob = listeOriginal.nombreJobs();
					pJob = listeOriginal.nombreJobs();
				}
				sJob++;
			}
			pJob++;
		}
		return liste2;
	}
	
	public ListeJobs ns3(ListeJobs listeOriginal){
		ArrayList<ListeJobs> listeJobs = new ArrayList<ListeJobs>();
		
		int phi = 40;
		for (int i = 0; i < phi; i++) {
			// Jobs à changer
			int j1 = (int) (Math.random()*listeOriginal.nombreJobs());
			int j2 = (int) (Math.random()*listeOriginal.nombreJobs());
			int j3 = (int) (Math.random()*listeOriginal.nombreJobs());
			
			while(j1==j2 || j1==j3 || j2==j3){
				j2 = (int) (Math.random()*listeOriginal.nombreJobs());
				j3 = (int) (Math.random()*listeOriginal.nombreJobs());
			}
			
			// Positions
			int s1 = (int) (Math.random()*listeOriginal.nombreJobs());
			int s2 = (int) (Math.random()*listeOriginal.nombreJobs());
			int s3 = (int) (Math.random()*listeOriginal.nombreJobs());
			
			while(s1==s2 || s1==s3 || s2==s3){
				s2 = (int) (Math.random()*listeOriginal.nombreJobs());
				s3 = (int) (Math.random()*listeOriginal.nombreJobs());
			}
			
			ListeJobs newListe = listeOriginal.clone();
			
			newListe = newListe.insert(listeOriginal.getJob(j1), s1);
			newListe = newListe.insert(listeOriginal.getJob(j2), s2);
			newListe = newListe.insert(listeOriginal.getJob(j3), s3);
			
			if(!listeJobs.contains(newListe)) listeJobs.add(newListe);
		}
		
		int cmax = Integer.MAX_VALUE;
		ListeJobs meilleureListe = null;
		for (int i = 0; i < listeJobs.size(); i++) {
			ListeJobs newListe = listeJobs.get(i);
			Ordonnancement nouveauOrdonnancement = new Ordonnancement(newListe, flowshop.getNbMachines());
			if (nouveauOrdonnancement.getDateDisponibilite(flowshop.getNbMachines()-1) < cmax) {
				cmax = nouveauOrdonnancement.getDateDisponibilite(flowshop.getNbMachines()-1);
				meilleureListe = newListe;
			}
		}
		
		return meilleureListe;
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
	
	public ListeJobs variableNeighborhoodSearch(){
		
		int nombreRepetitions = 100000;
		int n = 1;
		int k = 1;
		ListeJobs meilleureListe = listeNEH;
		
		long debut = System.currentTimeMillis(); /* on récupère le temps de départ */
		long fin = System.currentTimeMillis(); /* on récupère le temps de fin (qui sera égal à debut au commencement) */
		while (fin < debut + 600000) { 

			ListeJobs liste = new ListeJobs();
			if (k==1) liste = ns1(meilleureListe);
			else if (k==2) liste = ns2(meilleureListe);
			else if (k==3) liste = ns3(meilleureListe);
			
			Ordonnancement nouveauOrdonnancement = new Ordonnancement(liste, flowshop.getNbMachines());
			Ordonnancement ordonnancementActuel = new Ordonnancement(meilleureListe, flowshop.getNbMachines());
			
			if (nouveauOrdonnancement.getDateDisponibilite(flowshop.getNbMachines()-1) < ordonnancementActuel.getDateDisponibilite(flowshop.getNbMachines()-1)) {
				meilleureListe = liste;
				k = 1;
			}
			else {
				k++;
				if (k==4) k=1;
			}
			
			n++;
			fin = System.currentTimeMillis();     /* on récupère le nouveau temps */
		}
		
		return meilleureListe;
	}
	
	public static void main(String[] args) throws IOException {
		
//		Job j1 = new Job(1, new int[]{5,9,8,10,1});
//		Job j2 = new Job(2, new int[]{9,3,10,1,8});
//		Job j3 = new Job(3, new int[]{9,4,5,8,6});
//		Job j4 = new Job(4, new int[]{4,8,8,7,2});
//		
//		Flowshop fs = new Flowshop(new Job[]{j1,j2,j3,j4}, 5);
		
		try {
			BufferedReader in = new BufferedReader(new FileReader("./test/tai21.txt"));
			
			String line;
			
			line = in.readLine();
			
			String[] ligne = line.split(" ");
			
			int nbJobs = Integer.parseInt(ligne[0]);
			int nbMachines = Integer.parseInt(ligne[1]);
			
			Job[] jobs = new Job[nbJobs];
			int i = 1;
			
			while((line = in.readLine()) != null)
			{
			    ligne = line.split(" ");
			    int[] durees = new int[nbMachines];
			    for (int j = 0; j < ligne.length; j++) {
					durees[j] = Integer.parseInt(ligne[j]);
				}
			    Job job = new Job(i, durees);
			    jobs[i-1] = job;
			    i++;
			}
			in.close();
			
			Flowshop fs = new Flowshop(jobs, nbMachines);
			
			VNS vns = new VNS(fs);
			
			System.out.println("Original : ");
			vns.listeNEH.afficher();
			
			ListeJobs meilleureListe = vns.variableNeighborhoodSearch();
			
			System.out.println("Final : ");
			meilleureListe.afficher();
			Ordonnancement ordonnancement = new Ordonnancement(meilleureListe, vns.flowshop.getNbMachines());
			System.out.println(ordonnancement.getDateDisponibilite(vns.flowshop.getNbMachines()-1));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		Job j1 = new Job(1, new int[]{5,4,4,3});
//		Job j2 = new Job(2, new int[]{5,4,4,6});
//		Job j3 = new Job(3, new int[]{3,2,3,3});
//		Job j4 = new Job(4, new int[]{6,4,4,2});
//		Job j5 = new Job(5, new int[]{3,4,1,5});
//		
//		Flowshop fs = new Flowshop(new Job[]{j1,j2,j3,j4,j5}, 4);
//		
//		VNS vns = new VNS(fs);
//		
////		ListeJobs list=new ListeJobs();
////		list.ajouterJob(j1);
////		list.ajouterJob(j3);
////		list.ajouterJob(j2);
////		list.ajouterJob(j4);
////		vns.listeNEH = list;
//		
//		System.out.println("Original : ");
//		vns.listeNEH.afficher();
//		
//		ListeJobs meilleureListe = vns.variableNeighborhoodSearch();
//		
//		System.out.println("Final : ");
//		meilleureListe.afficher();
//		Ordonnancement ordonnancement = new Ordonnancement(meilleureListe, vns.flowshop.getNbMachines());
//		System.out.println(ordonnancement.getDateDisponibilite(vns.flowshop.getNbMachines()-1));
	}
	
}
