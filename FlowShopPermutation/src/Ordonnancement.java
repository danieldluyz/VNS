/*
* Nom de classe : Ordonnancement
*
* Description :
*
* Version : 1.0
*
* Date : 21/09/2010
*
* Auteur : Chams LAHLOU
*/

//import java.util.*; // nécessaire pour exo 2

public class Ordonnancement implements Cloneable {
	private ListeJobs sequence;		// ordre des jobs dans l'ordonnancement
	private int nbMachines;			// nombre de machines
	private int duree;				// duree totale
	private int[] dateDisponibilite;// date de disponibilite sur chaque machine
	
	// constructeur par défaut
	public Ordonnancement() {
		sequence = new ListeJobs();
		nbMachines = 0;
		duree = 0;
		dateDisponibilite = null;
	}
	
	// crée un ordonnancement vierge sur m machines
	public Ordonnancement(int m) {
		sequence = new ListeJobs();
		nbMachines = m;
		duree = 0;
		dateDisponibilite = new int[nbMachines];
		for (int i = 0; i < nbMachines; i++) {
			dateDisponibilite[i] = 0; // machines disponibles à l'instant 0
		}
	}
	
	/************************************************
	/ exo 4
	/************************************************/
	
	// crée un ordonnancement à partir d'une liste de jobs sur m machines
	// les jobs sont exécutés dans l'ordre de la liste
	public Ordonnancement(ListeJobs l, int m) {
		nbMachines = m;
		duree = 0;
		dateDisponibilite = new int[nbMachines];
		sequence = new ListeJobs();
		for (int i = 0; i < nbMachines; i++) {
			dateDisponibilite[i] = 0; // machines disponibles à l'instant 0
		}
		for (int i = 0; i < l.nombreJobs(); i++) {
			ordonnancerJob(l.getJob(i));
		}
		duree = dateDisponibilite[nbMachines-1];
	}
	
	public int getDuree() {
		return dateDisponibilite[nbMachines-1];
	}
	
	public ListeJobs getSequence() {
		return sequence;
	}
	
	public int getDateDisponibilite(int i) {
		return dateDisponibilite[i];
	}
	
	public void ajouterJob(Job j) {
		ordonnancerJob(j);
	}

	public void initialiser() { // mise à zéro de l'ordonnancement
		for (Job j : sequence) {
			for (int i = 0; i < nbMachines; i++) {
				j.setDateDebut(i, -1); // opérations non exécutées
			}
		}
		duree = 0;
		for (int i = 0; i < nbMachines; i++) {
			dateDisponibilite[i] = 0; // machines disponibles à l'instant 0
		}
	}
	
	public void afficher() { // affiche l'ordonnancement
		sequence.afficher();
		for (Job j : sequence) {
			System.out.print("Job " + j.getNumero() + " : ");
			for (int i = 0; i < nbMachines; i++) {
				System.out.print("(op "+ i +" à t = " 
					+ j.getDateDebut(i) + ") ");
			}
			System.out.println();
		}
		System.out.println("Cmax = " + duree);
	}
	
	public Ordonnancement clone() {
		Ordonnancement o = null;
		try {
			o = (Ordonnancement) super.clone();
		}
		catch(CloneNotSupportedException cnse) {
			cnse.printStackTrace(System.err);
		}
		// copie de la liste des jobs : nécessaire !
		o.sequence = (ListeJobs) sequence.clone();
		// copie du tableau
		o.dateDisponibilite = dateDisponibilite.clone();
		return o;
	}
	
	/************************************************
	/ exo 2
	/************************************************/
	
	public void ordonnancerJob(Job j) { // ordonnance un job en fonction de ce qui est déjà ordonnancé
		
		j.setDateDebut(0, dateDisponibilite[0]);
		dateDisponibilite[0] += j.getDureeOperation(0);
		
		for (int i = 1; i < j.getNbOperations(); i++) {
			int date = getDateDisponibilite(i);
			if (getDateDisponibilite(i) >= getDateDisponibilite(i-1)) {
				j.setDateDebut(i, date);
				dateDisponibilite[i] += j.getDureeOperation(i);
			}
			else {
				j.setDateDebut(i, getDateDisponibilite(i-1));
				dateDisponibilite[i] = dateDisponibilite[i-1] + j.getDureeOperation(i);
			}	
		}
		
		sequence.ajouterJob(j);
	}
	
	/************************************************
	/ exo 3
	/************************************************/
	
	public void ordonnancer(ListeJobs l) { // ordonnance les jobs de la liste
		int numero = l.clone().nombreJobs();
		for (int i = 0; i < numero; i++) {
			ordonnancerJob(l.getJob(i));
		}
	}

}