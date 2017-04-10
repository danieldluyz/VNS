/*
* Nom de classe : ListeJobs
*
* Description :
*
* Version : 1.0
*
* Date : 21/09/2010
*
* Auteur : Chams LAHLOU
*/

public class Sommet implements java.lang.Comparable<Sommet> {
	
    private Ordonnancement ordo; // ordonnancement en cours
    private ListeJobs nonPlaces; // liste des jobs non placés
    private int bInf; // borne inf
    private int numero; // identificateur
    
    // constructeur par défaut
    public Sommet() {
    	ordo = new Ordonnancement();
    	nonPlaces = new ListeJobs();
    	bInf = Integer.MIN_VALUE;
    	numero = 0;
    }
    
    // construction "normale"
    public Sommet(Ordonnancement o, ListeJobs l, int b, int n) {
    	ordo = o.clone();
    	nonPlaces = l.clone();
    	bInf = b;
    	numero = n;
    }
    
    public Ordonnancement getOrdonnancement() {
    	return ordo;
    }
    
    public ListeJobs getNonPlaces() {
    	return nonPlaces;
    }
    
    public int getBorneInf() {
    	return bInf;
    }
    
    public int getNumero() {
    	return numero;
    }
    
    public void afficher() {
    	System.out.println("Sommet " + numero);
    	//ordo.afficher();
    	//for (Job j : liste)
    	//    j.afficher();
    	System.out.println("Borne inf = " + bInf);
    }
    
    public int compareTo(Sommet obj) {
    	int nombre1 = ((Sommet) obj).getBorneInf();
    	int nombre2 = bInf;
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
}