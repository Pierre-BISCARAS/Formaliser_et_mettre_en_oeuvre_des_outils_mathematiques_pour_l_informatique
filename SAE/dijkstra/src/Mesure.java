import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import MG3D.geometrie.Maillage;
import MG3D.geometrie.Sommet;



public class Mesure {
    
    
    
    
    /** 
     * @param args
     */
    public static void main(String[] args){
        boolean trouver = true;
        while (trouver){
            try {
                Scanner sc = new Scanner(System.in);

                System.out.println("Quelle est le fichier que vous voulez ouvrir ?");
                String fichier = sc.next();
                Maillage m = new Maillage("./maillages/"+fichier);

                System.out.println("Quelle est votre sommet de départ ?");
                String sommetDepart = sc.next();
                int sommetDepartInt = Integer.parseInt(sommetDepart);

                System.out.println("Quelle est votre sommet d'arrivée ?");
                String sommetArrivee = sc.next();
                int sommetArriveeInt = Integer.parseInt(sommetArrivee);

                trouver = false;
                sc.close();
                ArrayList<Integer> chemin = Dijkstra(m,sommetDepartInt,sommetArriveeInt);

                System.out.println(longueurChemin(m, chemin));
                creeFichier(fichier,chemin,cheminCorrdonne(m,chemin)); 

            }
            catch (NullPointerException e){
                System.out.println(e);   
            }
        }
    }

    
    /** 
     * @param maillage
     * @param a
     * @param b
     * @return double
     */
    public static double distanceEuclidienneEntreSommets(Maillage maillage, int a, int b){
        double dx=maillage.getSommet(a).getX()-maillage.getSommet(b).getX();
        double dy=maillage.getSommet(a).getY()-maillage.getSommet(b).getY();
        double dz=maillage.getSommet(a).getZ()-maillage.getSommet(b).getZ();
        return Math.sqrt(dx*dx+dy*dy+dz*dz);
    }


    
    /** 
     * @param maillages
     * @param sommetDepart
     * @param sommetarrivee
     * @return ArrayList<Integer>
     */
    public static ArrayList<Integer> Dijkstra(Maillage maillages, int sommetDepart, int sommetarrivee) {
        double[] parcouru = new double[maillages.getNbSommets()];
        int[] percedent = new int[maillages.getNbSommets()];
        ArrayList<Integer> pasEncoreVue = new ArrayList<Integer>();
        for (int i = 0; i<parcouru.length;i++) {
            parcouru[i] = 999999;
            percedent[i] = 0;
            pasEncoreVue.add(new Integer(i));  
        }

        parcouru[sommetDepart] = 0;

        while (pasEncoreVue.size() != 0) {
            int s1 = pasEncoreVue.get(0);
            for (int j = 0; j<pasEncoreVue.size(); j++) {
                if (parcouru[pasEncoreVue.get(j)] < parcouru[s1]) {
                    s1 = pasEncoreVue.get(j);
                }
            }

            pasEncoreVue.remove(new Integer(s1));
            
            Sommet s = maillages.getSommet(s1);
            for (int k = 0; k<s.getNbVoisins(); k++) {
                int s2 = s.getVoisin(k);
                if (parcouru[s2]>parcouru[s1]+distanceEuclidienneEntreSommets(maillages, s1,s2)) {
                    parcouru[s2] = parcouru[s1] + distanceEuclidienneEntreSommets(maillages, s1, s2);
                    percedent[s2] = s1; 
                }
            }
        }
        
        ArrayList<Integer> chemin = new ArrayList<Integer>() ;
        
        int som = sommetarrivee;

        while (som!=sommetDepart) {
            chemin.add(0,som);
            som = percedent[som];
        }
        chemin.add(0,sommetDepart);
        return chemin;  
    }

    
    /** 
     * @param m
     * @param chemin
     * @return double
     */
    public static double longueurChemin(Maillage m, ArrayList<Integer> chemin) {
        int chemin1 = chemin.get(0);
        double total = 0;
        for (int i=1;i<chemin.size();i++) {
            total += distanceEuclidienneEntreSommets(m, chemin1, chemin.get(i));
            chemin1 = chemin.get(i); 
        }
        return total;
    }

    
    /** 
     * @param m
     * @param chemin
     * @return ArrayList<Double>
     */
    public static ArrayList<Double> cheminCorrdonne(Maillage m, ArrayList<Integer> chemin) {
        ArrayList<Double> coord = new ArrayList<Double>();
        for (int i=0; i<chemin.size(); i++) {
            coord.add(m.getSommet(chemin.get(i)).getX());
            coord.add(m.getSommet(chemin.get(i)).getY());
            coord.add(m.getSommet(chemin.get(i)).getZ());
        }
        return coord;
    }

    
    /** 
     * @param nom
     * @param chemin
     * @param ArrayListcoord
     */
    public static void creeFichier(String nom, ArrayList<Integer> chemin, ArrayList<Double>coord) {
        File file = new File("./resultat.txt");
       
        try {
            if (file.createNewFile()){
              System.out.println("Fichier créé!");
                PrintWriter writer = new PrintWriter("resultat.txt", "UTF-8");
                writer.println(nom);
                writer.println(chemin);
                writer.println(coord);
                writer.close();
            }else{
              System.out.println("Fichier existe déjà.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
