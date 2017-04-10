/*
* Nom de classe : Job
*
* Description :
*
* Version : 1.0
*
* Date : 21/09/2010
*
* Auteur : Chams LAHLOU
*/


public class Job implements java.lang.Comparable<Job>, Cloneable {
    // ajout de "implements java.lang.Comparable" pour tri dans exo 2
    // ajout de "Cloneable" pour la copie
    
    private int numero;					// identificateur du job
    private int nbOperations;			// nombre d'opérations du job
    private int[] dureesOperations;		// durées des opérations
    private int duree;					// somme des durées des opérations
    private int[] datesDebut;			// dates au plus tôt sur chaque machine
    
    // constructeur par défaut
    public Job() {
    	numero = 0;
    	nbOperations = 0;
    	dureesOperations = null;
    	datesDebut = null;
    	calculerDuree();
    }
    
    // crée un job de numéro n à partir d'un tableau des durées des opérations
    public Job(int n, int[] d) {
    	numero = n;
    	nbOperations = d.length;
    	dureesOperations = new int[nbOperations];
    	datesDebut = new int[nbOperations];
    	
    	for(int i = 0; i < nbOperations; i++) {
    		dureesOperations[i] = d[i];
    		datesDebut[i] = -1;
    	}
    	
    	calculerDuree();
    }
    
    public int getNumero() {
    	return numero;
	}
	
    public int getNbOperations() {
    	return nbOperations;
    }
    
    public int getDureeOperation(int i) {
    	return dureesOperations[i];
    }
    
    public void afficher() {
    	System.out.println("Job " + numero + " de durée totale " + duree + ":");
    	System.out.println("Durées des opérations : ");
    	for (int i = 0; i < nbOperations; i++) {
    		System.out.print("(op " + i + " : " + dureesOperations[i] + ") ");
    	}
    	System.out.println();
    	System.out.println("Dates de début des opérations : ");
    	for (int i = 0; i < nbOperations; i++) {
    		System.out.print("(op " + i + " : " + datesDebut[i] + ") ");
    	}
    	System.out.println();
    }
    
    public int getDateDebut(int i) {
    	return datesDebut[i];
    }
    
    public void setDateDebut(int i, int t) {
    	datesDebut[i] = t;
    }
    
    public int getDuree() {
    	return duree;
    }
    
    public Job clone() {
    	Job j = null;
    	try {
    		j = (Job) super.clone();
    	}
    	catch(CloneNotSupportedException cnse) {
    		cnse.printStackTrace(System.err);
    	}
    	// copie des tableaux : nécessaire !
    	j.dureesOperations = dureesOperations.clone();
    	j.datesDebut = datesDebut.clone();
    	return j;
	}
	
    /* on a besoin de définir un "ordre naturel" pour utiliser les fonctions 
    * "sort()" et "reverseOrder()".
    * On utilise la durée des jobs pour les comparer
    */
    
    public int compareTo(Job obj) {
    	int nombre1 = ((Job) obj).getDuree();
    	int nombre2 = this.duree;
    	if (nombre1 > nombre2) {
    		return -1;
    	}
    	else if (nombre1 == nombre2) {
    		return 0;
    	}
    	else {
    		return 1;
    	}
    }
    
    public boolean equals(Object o) {
    	if (!(o instanceof Job)) {
    	    return false;
    	  }
    	  Job other = (Job) o;
    	  return this.numero == other.numero;
    }
    
    /************************************************
    / exo 1
    /************************************************/
    
    private void calculerDuree() { // calcule de la durée totale du job
		int sum = 0;
		for (int i = 0; i < nbOperations; i++) {
			sum += dureesOperations[i];
		}
		this.duree=sum;
    }
}