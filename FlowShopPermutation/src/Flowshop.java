/*
* Nom de classe : Flowshop
*
* Description :
*
* Version : 1.0
*
* Date : 21/09/2010
*
* Auteur : Chams LAHLOU
*/

import java.util.*; // nécessaire pour exo 2
import java.io.*; //

public class Flowshop {
    private int nbJobs; 		// nombre de jobs
    private int nbMachines; 	// nombre de machines
    private Job[] jobs; 		// tableau des jobs
    
    // constructeur par défaut
    public Flowshop() {
    	nbJobs = 0;
    	nbMachines = 0;
    	jobs = null;
    }
    
    // crée un problème à m machines à partir d'un tableau de jobs
    public Flowshop(Job[] t, int m) {
    	nbMachines = m;
    	nbJobs = t.length;
    	jobs = new Job[nbJobs]; // on réserve la place pour les jobs
        for(int i = 0; i < nbJobs; i++) {
        	jobs[i] = t[i];
        }
    }
    
    // crée un problème à partir d'un fichier
    public Flowshop(String s) {
    	try {
    		Scanner scanner = new Scanner(new FileReader(s));
    		
    		// lecture du nombre de jobs
    		if (scanner.hasNextInt()) {
    			nbJobs = scanner.nextInt();
    		}
    		
    		// lecture du nombre de machines
    		if (scanner.hasNextInt()) {
    			nbMachines = scanner.nextInt();
    		}
    		
    		jobs = new Job[nbJobs]; // on réserve la place pour les jobs
    		int []d = new int[nbMachines];
    		int i = 0; // indice du job
    		int j = 0; // indice de l'opération
    		while (scanner.hasNextInt()) {
    			d[j] = scanner.nextInt();
    			System.out.println(j + " " + d[j]);
    			if (j < nbMachines - 1) {
    				j++; // opération suivante
    			}
    			else { // sinon on crée le job et on passe au suivant
    				jobs[i] = new Job(i, d);
    				i++;
    				j = 0;
    			}
    		}
    		scanner.close();
    	}
    	catch (IOException e) {
    		System.err.println("Erreur : " + e.getMessage()) ;
    		System.exit(2) ;
    	}
    }
    
    public int getNbJobs() {
    	return nbJobs;
    }
    
    public int getNbMachines() {
        return nbMachines;
    }
    
    public Job getJob(int i) {
    	return jobs[i];
    }
    
    // crée une liste correspondant au tableau des jobs
    public ListeJobs creerListeJobs() {
    	ListeJobs l = new ListeJobs();
    	for (int i = 0; i < nbJobs; i++) {
    		l.ajouterJob(jobs[i].clone());
    	}
    	return l;
    }
    
    /************************************************
    / exo 5
    /
    x************************************************/
    
    public ListeJobs creerListeNEH() { // renvoie une liste selon l'ordre NEH
		
    	ListeJobs lj = new ListeJobs();
		
		for (int i = 0; i < jobs.length; i++) {
			Job j = jobs[i];
			lj.ajouterJob(j);
		}
		
		lj.trierDureesDecroissantes();
		
		ListeJobs liste = new ListeJobs();
		liste.ajouterJob(lj.getJob(0));
		lj.supprimerJob(0);
		
		for (int j=0;j<lj.nombreJobs();j++) {
			ListeJobs listeClone = liste.clone();
			Job job = lj.getJob(j);
			
			int position = -1;
			int dureeMinimal = Integer.MAX_VALUE;
			
			for (int i = 0; i < listeClone.nombreJobs()+1; i++) {
				listeClone.ajouterJob(job, i);
				Ordonnancement o = new Ordonnancement(listeClone, nbMachines);
				
				if (o.getDuree()<dureeMinimal) {
					position = i;
					dureeMinimal = o.getDuree();
				}
				
				listeClone.supprimerJob(job);
			}
			
			liste.ajouterJob(job, position);
			
		}
		return liste;
    }
    
    /************************************************
    / exo 6
    /************************************************/
    
    // calcul de r_kj
    public int calculerDateDispo(int k, int j) {
		
    	int dispo = 0;
    	
    	for (int i = 0; i < k; i++) {
			dispo += jobs[j].getDureeOperation(i);
		}
    	
    	return dispo;
    }
    
    // calcul de q_kj
    public int calculerDureeLatence(int k, int j) {
		
    	int latence = 0;
    	
    	for (int i = k+1; i < nbMachines; i++) {
			latence += jobs[j].getDureeOperation(i);
		}
    	
    	return latence;
    	
    }
    
    // calcul de la somme des durées des opérations exécutées sur la machine k
    public int calculerDureeJobs(int k) {
		
    	int duree = 0;
		
    	for (int i = 0; i < jobs.length; i++) {
			duree += ((Job) jobs[i]).getDureeOperation(k);
		}
    	
    	return duree;
    }
    
    public int calculerBorneInf(ListeJobs lJobs) {
		   	
    	int borneInf = Integer.MIN_VALUE;
    	
    	for (int i = 0; i < nbMachines; i++) {
			int borne = calculerBorneInfMachine(i);
			if (borne > borneInf) borneInf = borne;
		}
    	
    	return borneInf;
	}
    
    public int calculerBorneInfMachine(int k) {
    	
    	int minR = Integer.MAX_VALUE;
    	
    	for (int i = 0; i < jobs.length; i++) {
    		int date = calculerDateDispo(k, i);
    		if (date < minR) minR = date;
		}
    	
    	int p = calculerDureeJobs(k);
    	
    	int minQ = Integer.MAX_VALUE;

    	for (int i = 0; i < jobs.length; i++) {
    		int lat = calculerDureeLatence(k, i);
    		if (lat < minQ) minQ = lat;
		}
    	
    	return minR + p + minQ;
    }

    /************************************************
    / exo 7
    /************************************************/
    
    // calcul de r_kj tenant compte d'un ordo en cours
    public int calculerDateDispo(Ordonnancement o, int k, int j) {
    	int max = 0;
    	for (int i = 0; i<k; i++) {
			int actuel = o.getDateDisponibilite(i) + jobs[j].getDureeOperation(i);
			if (actuel>max) max = actuel;
		}
    	return Math.max(max, o.getDateDisponibilite(k));
	}
    
    // calcul de la somme des durées des opérations d'une liste
    // exécutées sur la machine k
    public int calculerDureeJobs(int k, ListeJobs l) {
    	int duree = 0;
    	for (int i = 0; i < l.nombreJobs(); i++) {
    		duree += l.getJob(i).getDureeOperation(k);
		}
    	return duree;
    }
    
    public int calculerBorneInfMachine(Ordonnancement o, ListeJobs l, int k) {
    	
    	int minR = Integer.MAX_VALUE;
    	for (Job job : o.getSequence()) {
    		int date = calculerDateDispo(o,k,job.getNumero()-1);
    		minR = Math.min(date, minR);
		}
    	
    	int p = calculerDureeJobs(k,l);
    	
    	int minLat = Integer.MAX_VALUE;
    	for (Job job : o.getSequence()) {
    		int lat = calculerDureeLatence(k, job.getNumero());
    		minLat = Math.min(lat, minLat);
		}
    	
    	return minR + p + minLat;
    }

    // calcul de la borne inférieure en tenant compte d'un ordonnancement en cours
    public int calculerBorneInf(Ordonnancement o, ListeJobs l) {
    	int borneInf = Integer.MIN_VALUE;
    	for (int k = 0; k < nbMachines; k++) {
			int borne = calculerBorneInfMachine(o,l,k);
			borneInf = Math.max(borneInf, borne);
		}
    	return borneInf;
	}
    
    /************************************************
	 / exo 8
	 /************************************************/
    
	// procédure par évaluation et séparation
    public void evaluationSeparation() {
    	ListeJobs liste = creerListeNEH();
    	Ordonnancement o = new Ordonnancement(liste, 5);
		int bs = o.getDateDisponibilite(nbMachines-1);
		System.out.println("Borne sup: "+bs);
		
		Ordonnancement ord = new Ordonnancement(5);
	
		ord.ajouterJob(jobs[0]);
		ord.ajouterJob(jobs[2]);
		
		liste = creerListeJobs();
		liste.supprimerJob(jobs[0]);
		liste.supprimerJob(jobs[2]);
		
		System.out.println("Borne inf: "+calculerBorneInf(ord, liste));
		// J'ai eu un problème. Je n'arrive pas à obtenir 46 pour cet exemple 
		// comme montré en cours, mais 58
		// Cela n'est pas correct parce que je n'explorairais jamais l'arbre parce que
		// la borne inf est superièure à la borne sup.
    }
    
    public static void main(String[] args) {
		
    	Job j1 = new Job(1, new int[]{5,9,8,10,1});
		Job j2 = new Job(2, new int[]{9,3,10,1,8});
		Job j3 = new Job(3, new int[]{9,4,5,8,6});
		Job j4 = new Job(4, new int[]{4,8,8,7,2});
		
//		Flowshop fs = new Flowshop(new Job[]{j1,j2,j3,j4}, 5);
		
//		fs.evaluationSeparation();
		
//		ListeJobs liste = fs.creerListeNEH();
//		liste.afficher();	
//		Ordonnancement o = new Ordonnancement(liste, 5);
//		o.afficher();
//		
//		System.out.println(fs.calculerBorneInf(liste));
		
//		int h = fs.calculerDateDispo(3, 2);
//		System.out.println(h);
//		
//		int bi = fs.calculerBorneInf(fs.creerListeJobs());
//		System.out.println(bi);
		
		ListeJobs ls = new ListeJobs();
		
		ls.ajouterJob(j1);
		ls.ajouterJob(j2);
		ls.ajouterJob(j3);
		ls.ajouterJob(j4);
		
		ls.afficher();
		
		ls = ls.swap(j3, j4);
		
		ls.afficher();
		
		ls = ls.insert(j4, 0);
		
		ls.afficher();
		
		ls = ls.insert(j3, 1);
		
		ls.afficher();
		
		ls = ls.swap(j3, j4);
		
		ls.afficher();
		
	}
}