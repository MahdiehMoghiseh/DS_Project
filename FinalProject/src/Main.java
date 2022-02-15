
import java.util.Scanner;

public class Main {
    static Node root;
    static Trie nNodes;
    static int countN;
    static Trie bNodes;
    static int countB;
    static Trie.T_Node rootN;
    static Trie.T_Node rootB;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while (true){
            System.out.println("Please enter your order :");
            System.out.println("add neighbourhood -> enter 1");
            System.out.println("add bank -> enter 2");
            System.out.println("add branch -> enter 3");
            System.out.println("delete branch -> enter 4");
            System.out.println("banks in a neighbourhood -> enter 5");
            System.out.println("branches of a bank -> enter 6");
            System.out.println("nearest bank -> enter 7");
            System.out.println("nearest branch -> enter 8");
            System.out.println("available banks -> enter 9");
            System.out.println("Bank with most branches -> enter 10");
            System.out.println("exit -> enter 0");
            int order = input.nextInt();
            Main m = new Main();
            if (order==0){
                break;
            }
            switch (order){
                case 1 : m.addN();break;
                case 2 : m.addB();break;
                case 3 : m.addBr();break;
                case 4 : m.delBr();break;
                case 5 : m.listB();break;
                case 6 : m.listBrs();break;
                case 7 : m.nearB();break;
                case 8 : m.nearBr();break;
                case 9 : m.availB();break;
                case 10 : m.mostBrs();break;
                default:break;
            }
        }
    }
    // methods
    ////////////////////////////////////////////////////////ADD NEIGHBOURHOOD///////////////////////////////////////////
    public void addN(){
        System.out.println("Please enter the neighbourhood name : ");
        String name = input.next();
        System.out.println("Please enter first coordinates of neighbourhood : ");
        double x = input.nextDouble();
        double y = input.nextDouble();
        double[] p1 = new double[2];
        p1[0]=x;
        p1[1]=y;
        System.out.println("Please enter second coordinates of neighbourhood : ");
        x = input.nextDouble();
        y = input.nextDouble();
        double[] p2 = new double[2];
        p2[0]=x;
        p2[1]=y;
        System.out.println("Please enter third coordinates of neighbourhood : ");
        x = input.nextDouble();
        y = input.nextDouble();
        double[] p3 = new double[2];
        p3[0]=x;
        p3[1]=y;
        System.out.println("Please enter forth coordinates of neighbourhood : ");
        x = input.nextDouble();
        y = input.nextDouble();
        double[] p4 = new double[2];
        p4[0]=x;
        p4[1]=y;
        Neighbourhood n = new Neighbourhood(name,p1,p2,p3,p4);
        if (nNodes==null){
            nNodes = new Trie();
            rootN = new Trie.T_Node();
        }
        nNodes.insert2(name,p1,p2,p3,p4,rootN);
        countN++;
        System.out.println("Successfully added!");
    }
    ////////////////////////////////////ADD BANK////////////////////////////////////////////////////////////////////////
    public void addB(){
        System.out.println("Please enter bank name : ");
        String name = input.next();
        System.out.println("Please enter coordinates of bank : ");
        double[] p = new double[2];
        p[0] = input.nextDouble();
        p[1] = input.nextDouble();
        Bank b = new Bank(name,p);
        if (bNodes==null){
            bNodes = new Trie();
            rootB = new Trie.T_Node();
        }
        else {
            if (bNodes.search(name,rootB)!=null){
                System.out.println("There is a bank here, try again!");
                return;
            }
        }
        bNodes.insert(name,b,rootB);
        countB++;
        if (root==null){
            root = new Node(b,true);
        }
        else {
            root.add(root,p,0,b,true);
        }
        System.out.println("Successfully added!");
    }
    /////////////////////////////////////////////////////ADD BRANCH/////////////////////////////////////////////////////
    public void addBr(){
        System.out.println("Please enter branch name : ");
        String name = input.next();
        System.out.println("Please enter bank name : ");
        String nameB = input.next();
        if (bNodes.search(nameB,rootB)==null){
            System.out.println("This bank doesn't exist, try again!");
            return;
        }
        System.out.println("Please enter coordinates of bank : ");
        double[] p = new double[2];
        p[0] = input.nextDouble();
        p[1] = input.nextDouble();
        Bank main = bNodes.search(nameB,rootB);
        Branch b = new Branch(name,nameB,p,main);
        if (root.search(p,root,0)!=null){
            System.out.println("There is a bank here, try again!");
            return;
        }
        if (main.branches==null){
            main.branches = new Node(b,false);
        }
        else {
            main.branches.add(main.branches, p,0,b,false);
        }
        main.noBranches++;
        root.add(root,p,0,b,false);
        System.out.println("Successfully added!");
    }
    //////////////////////////////////////////////////////////DELETE BRANCH/////////////////////////////////////////////
    public void delBr(){
        System.out.println("Please enter coordinates of bank : ");
        double[] p = new double[2];
        p[0] = input.nextDouble();
        p[1] = input.nextDouble();
        Node bank = root.search(p,root,0);
        if (bank.isMain){
            System.out.println("There is main bank here, try again!");
            return;
        }
        Node node = root.search(p,root,0);
        if (node==null){
            System.out.println("There isn't any bank, try again!");
            return;
        }
        root = root.delete(root,p,0);
        // delete from its main bank
        if (node.b instanceof Branch){
            Bank main = ((Branch) node.b).main;
            main.branches = main.branches.delete(main.branches,p,0);
            main.noBranches--;
        }
        System.out.println("Successfully deleted!");
    }
    //////////////////////////////////////////////////////////LIST BANKS////////////////////////////////////////////////
    public void listB(){
        System.out.println("Please enter neighbourhood's name : ");
        String name = input.next();
        if (nNodes==null){
            System.out.println("This neighbourhood doesn't exist, try again!");
            return;
        }
        Neighbourhood n = nNodes.search2(name,rootN);
        if (n==null){
            System.out.println("This neighbourhood doesn't exist, try again!");
            return;
        }
        // use range search
        rangeSearch(n,root,0);
    }
    public void rangeSearch(Neighbourhood n,Node root,int d){
        int index = d % 2;
        if (root==null){
            return;
        }
        Bank b = root.b;
        if (n.IsThere(b.coordinates)){
            System.out.println("Bank name : "+b.name);
            System.out.println("Bank coordinates : ("+b.coordinates[0]+","+b.coordinates[1]+")");
            System.out.println("----------------------------------");
            rangeSearch(n,root.left,d+1);
            rangeSearch(n,root.right,d+1);
        }
        else {
            double centerX = (n.minx + n.maxx)/2;
            double centerY = (n.maxy + n.miny)/2;
            // if on x axis
            if (index == 0){
                if (b.coordinates[index]<centerX){
                    rangeSearch(n,root.right,d+1);
                }
                else{
                    rangeSearch(n,root.left,d+1);
                }
            }
            else {
                if (b.coordinates[index]<centerY){
                    rangeSearch(n,root.right,d+1);
                }
                else {
                    rangeSearch(n,root.left,d+1);
                }
            }
        }
    }
    ////////////////////////////////////////////////////////LIST BRANCHES///////////////////////////////////////////////
    public void listBrs(){
        System.out.println("Please enter bank's name : ");
        String name = input.next();
        Bank b = bNodes.search(name,rootB);
        if (b==null){
            System.out.println("This bank doesn't exist, try again!");
            return;
        }
        printBrs(b.branches);
    }
    public void printBrs(Node root){
        if (root==null){
            return;
        }
        System.out.println("Branch name : "+root.b.name);
        System.out.println("Branch coordinates : ("+root.b.coordinates[0]+","+root.b.coordinates[1]+")");
        System.out.println("----------------------------------");
        printBrs(root.left);
        printBrs(root.right);
    }
    ///////////////////////////////////////////////////AVAILABLE BANKS//////////////////////////////////////////////////
    public void availB(){
        System.out.println("Please enter R : ");
        double r = input.nextDouble();
        System.out.println("Please enter coordinates : ");
        double[] point = new double[2];
        point[0] = input.nextDouble();
        point[1] = input.nextDouble();
        availBHelp(root,r,point);
    }
    public void availBHelp(Node root,double r,double[] point){
        if (root==null){
            return;
        }
        if (distance(point,root.b.coordinates)<=r){
            System.out.println("Bank name : "+root.b.name);
            System.out.println("Bank coordinates : ("+root.b.coordinates[0]+","+root.b.coordinates[1]+")");
            System.out.println("----------------------------------");
        }
        if (root.left != null){
            availBHelp(root.left,r,point);
        }
        if (root.right!=null){
            availBHelp(root.right,r,point);
        }
    }
    //////////////////////////////////////////////////////NEAREST BANK//////////////////////////////////////////////////
    public void nearB(){
        System.out.println("Please enter coordinates : ");
        double[] p = new double[2];
        p[0] = input.nextDouble();
        p[1] = input.nextDouble();
        Bank answer = nearest(root,p,null,0,0);
        System.out.println("Nearest bank is : "+answer.name+" => ("+answer.coordinates[0]+","+answer.coordinates[1]+")");
    }
    public Bank nearest(Node root , double[] point,Bank near,double minD,int d){
        if (root==null){
            return near;
        }
        int index = d%2;

        if (near == null || distance(root.b.coordinates, point) < distance(point, near.coordinates)){
            near = root.b;
            minD = distance(root.b.coordinates,point);
        }
        if (point[index]<root.b.coordinates[index]){
            near = nearest(root.left,point,near,minD,d+1);
            if (point[index]+minD>=root.b.coordinates[index]){
                near = nearest(root.right,point,near,minD,d+1);
            }
        }
        else {
            near = nearest(root.right,point,near,minD,d+1);
            if (point[index]+minD<=root.b.coordinates[index]){
                near = nearest(root.left,point,near,minD,d+1);
            }
        }
        return near;
    }
    public double distance(double[] point1,double[] point2){
        return Math.sqrt(Math.pow(point1[0]-point2[0],2 )+Math.pow(point1[1]-point2[1],2 ));
    }
    //////////////////////////////////////////NEAREST BRANCH////////////////////////////////////////////////////////////
    public void nearBr(){
        System.out.println("Please enter coordinates : ");
        double[] p = new double[2];
        p[0] = input.nextDouble();
        p[1] = input.nextDouble();
        System.out.println("Please enter bank name : ");
        String bName = input.next();
        Bank b = bNodes.search(bName,rootB);
        if (b==null){
            System.out.println("This bank doesn't exist, try again!");
            return;
        }
        Bank nearest = nearest(b.branches,p,null,0,0);
        System.out.println("nearest branch is : "+nearest.name+" => ("+nearest.coordinates[0]+","+nearest.coordinates[1]+")");
    }
    /////////////////////////////////////////////////MOST BRANCHES//////////////////////////////////////////////////////
    public void mostBrs(){
        Node max = root;
        Node mostB = mostBrsWithPreorder(root,max);
        System.out.println("Bank with the most branches is :  "+mostB.b.name+" with "+mostB.b.noBranches+" branches");
    }
    public Node mostBrsWithPreorder(Node root, Node max){
        if (root==null){
            return null;
        }
        if (root.b.noBranches>max.b.noBranches){
            max = root;
        }
        if (root.left!=null){
            max = mostBrsWithPreorder(root.left,max);
        }
        if (root.right!=null){
            max = mostBrsWithPreorder(root.right,max);
        }
        return max;
    }
}


class Node {
    static int k = 2;
    Bank b;
    Node left, right;
    // if isMain is true -> we have main bank here!
    boolean isMain;
    public Node(Bank b,boolean isMain){
        this.b = b;
        this.left = null;
        this.right = null;
        this.isMain = isMain;
    }
    Node add(Node root, double[] point, int d,Bank b,boolean isMain){
        if (root==null){
            return new Node(b,isMain);
        }
        int compare = d % k;
        if (point[compare]<root.b.coordinates[compare]){
            root.left = add(root.left, point, d+1,b,isMain);
        }
        else {
            root.right = add(root.right, point, d+1,b,isMain);
        }
        return root;
    }
    Node min(Node root){
        if (root==null){
            return null;
        }
        while (root.left!=null){
            root = root.left;
        }
        return root;
    }
    Node delete(Node root, double[] p, int d){
        if (root==null){
            return null;
        }
        int index = d % k;
        if (p[index]<root.b.coordinates[index]){
            root.left = delete(root.left,p,d+1);
        }
        else if (p[index]>root.b.coordinates[index]){
            root.right = delete(root.right,p,d+1);
        }
        else {
            if (root.left==null){
                return root.right;
            }
            else if (root.right==null){
                return root.left;
            }
            root = min(root.right);
            root.right = delete(root.right,root.b.coordinates, d+1);
        }
        return root;
    }
    Node search(double[] point, Node root, int d){
        if (root==null){
            return null;
        }
        if (root.b.coordinates[0]==point[0] && root.b.coordinates[1]==point[1]){
            return root;
        }
        int index = d%k;
        if (point[index]<root.b.coordinates[index]){
            return search(point,root.left,d+1);
        }
        return search(point,root.right,d+1);
    }
}

class Trie {
     static int size = 26;
     static class T_Node{
        T_Node[] children = new T_Node[size];
        boolean endW;
        Bank bank;
        Neighbourhood neighbourhood;
        T_Node(){
            endW = false;
            for (T_Node t : children){
                t = null;
            }
        }
    }
    // for banks
    public void insert(String key,Bank b,T_Node root){
        int level;
        int len = key.length();
        int index;
        T_Node node = root;
        for (level=0;level<len;level++){
            index = key.charAt(level) - 'a';
            if (node.children[index] == null)
                node.children[index] = new T_Node();

            node = node.children[index];
        }
        node.endW = true;
        node.bank = b;
    }
    // for neighbourhoods
    public void insert2(String key,double[] p1,double[] p2,double[] p3,double[] p4,T_Node root){
        int level;
        int len = key.length();
        int index;
        T_Node node = root;
        for (level=0;level<len;level++){
            index = key.charAt(level) - 'a';
            if (node.children[index] == null)
                node.children[index] = new T_Node();

            node = node.children[index];
        }
        node.endW = true;
        node.neighbourhood = new Neighbourhood(key,p1,p2,p3,p4);
    }
    public Bank search(String key,T_Node root){
        int level;
        int len = key.length();
        int index;
        T_Node node = root;

        for (level = 0; level < len; level++)
        {
            index = key.charAt(level) - 'a';

            if (node.children[index] == null)
                return null;

            node = node.children[index];
        }
        return (node.bank);
    }
    public Neighbourhood search2(String key,T_Node root){
        int level;
        int len = key.length();
        int index;
        T_Node node = root;

        for (level = 0; level < len; level++)
        {
            index = key.charAt(level) - 'a';

            if (node.children[index] == null)
                return null;

            node = node.children[index];
        }
        return (node.neighbourhood);
    }
    public boolean isEmpty(T_Node root){
        for (int i = 0; i < 26; i++)
            if (root.children[i] != null)
                return false;
        return true;
    }

    public T_Node delete(T_Node root,String key, int d){
        if (root==null){
            return null;
        }
        if (d==key.length()){
            if (root.endW){
                root.endW = false;
            }
            if (isEmpty(root)){
                root = null;
            }
            return root;
        }
        int index = key.charAt(d) - 'a';
        root.children[index] = delete(root.children[index], key, d + 1);
        if (isEmpty(root) && !root.endW){
            root = null;
        }
        return root;
    }
}