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

import java.util.*;

public class FilePrioriteSommets implements Iterable<Sommet> {
    private PriorityQueue<Sommet> file; // file de priorité des sommets
    
    // constructeur par défaut
    public FilePrioriteSommets() {
    	file = new PriorityQueue<Sommet>();
    }
    
    // renvoie la tête
    public Sommet tete() {
    	return file.peek();
    }
    
    // renvoie et supprime la tête
    public Sommet recupererTete() {
    	return file.poll();
    }
    
    public int taille() {
    	return file.size();
    }
    
    public void ajouterSommet(Sommet s) {
    	file.add(s);
    }
    
    public boolean estVide() {
    	if (file.size() == 0) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    // pour utiliser le "foreach"
    public Iterator<Sommet> iterator() {
        Iterator<Sommet> iSommet = file.iterator();
        return iSommet;
    }
}