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

import java.util.*; // nécessaire pour exo 2

public class ListeJobs implements Cloneable, Iterable<Job> {
    private List<Job> liste; 				// liste des éléments
    
    // constructeur par défaut
    public ListeJobs() { 
    	liste = new ArrayList<Job>(); 		// liste vide
    }
    
    public Job getJob(int i) {
    	return liste.get(i);
    }
    
    public void ajouterJob(Job j) {
    	liste.add(j);
    }
    
    public void ajouterJob(Job j, int i) {		// ajoute à la position i
    	liste.add(i, j);
    }
    
    public void supprimerJob(Job j) {
    	liste.remove(j);
    }
    
    public void supprimerJob(int i) {
    	liste.remove(i);
    }

    public int nombreJobs() {
    	return liste.size();
    }
    
    public int position(Job j) {
    	return liste.indexOf(j);
    }

	public void afficher() { 			// affiche la liste des jobs
    	System.out.print("( ");
        for (Job j : liste) {
        	System.out.print(j.getNumero() + " ");
        }
    	System.out.println(")");
    }
    
    public void trierDureesDecroissantes() { // renvoie une liste triée selon critère NEH
        Collections.sort(liste, Collections.reverseOrder()); // on trie selon durées décroissantes
    }
    
    // pour utiliser le "foreach"
    public Iterator<Job> iterator() {        
        Iterator<Job> iJob = liste.iterator();
        return iJob; 
    }
    
    // pour créer une copie
    public ListeJobs clone() {
    	ListeJobs l = null;
    	try {
    		l = (ListeJobs) super.clone();
    	} 
    	catch(CloneNotSupportedException cnse) {
    		cnse.printStackTrace(System.err);
    	}
    	// copie de la liste des jobs : nécessaire !
    	l.liste = new ArrayList<Job>();
    	for (Job j : liste) {
    		l.liste.add(j.clone());
    	}
    	return l;
    }
    
    public ListeJobs swap(Job j1, Job j2) {
    	
		int pos1 = position(j1);
		int pos2 = position(j2);
		
		if (pos1!=-1 && pos2!=-1) {
			ajouterJob(j2, pos1);
			supprimerJob(pos1+1);
			
			ajouterJob(j1,pos2);
			supprimerJob(pos2+1);
		}
		
		return this;
	}
    
    public ListeJobs swap(int pos1, int pos2) {
    	
    	Job j1 = getJob(pos1);
    	Job j2 = getJob(pos2);
    	
		if (j1!=null && j2!=null) {
			ajouterJob(j2, pos1);
			supprimerJob(pos1+1);
			
			ajouterJob(j1,pos2);
			supprimerJob(pos2+1);
		}
		
		return this;
	}
    
    public ListeJobs insert(Job j1, int pos) {
    	
    	if (position(j1)!=-1){
    		supprimerJob(j1);
    		ajouterJob(j1, pos);
    	}
   
    	return this;
    }
 
    public boolean equals(Object obj) {
    	
    	if (obj instanceof ListeJobs) {
    		ListeJobs ls = (ListeJobs) obj;
    		if (this.liste.equals(ls.liste)) return true;
    	}
    	
    	return false;
    }
}