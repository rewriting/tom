package modele;
import java.util.ArrayList;

//on part sur la base que le contact de deux cellules et d'un fil se fait sur un pixel du contour de la deux cellules
//a changer pour la partie deux du projet
public class Contact {
	private ArrayList<Integer> list;
	
	public Contact(){
		list = new ArrayList<Integer>();
	}
	
	public Contact(int x1){
		list = new ArrayList<Integer>();
		list.add(x1);
	}
	
	public void ajouterContact(int val){
		this.list.add(val);
	}
	
	public void clear(){
		this.list.clear();
	}
	
	public ArrayList<Integer> getListContacts(){
		return this.list;
	}
	
	public int getContactFromIndex(int i){
		return this.list.get(i);
		//tster les cas d'erreurs
	}
}
