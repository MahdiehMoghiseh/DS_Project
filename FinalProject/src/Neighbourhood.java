public class Neighbourhood {
    String name;
    double[][] coordinates = new double[4][2];
    double minx;
    double maxx;
    double miny;
    double maxy;
    Neighbourhood(String name, double[] p1, double[] p2, double[] p3, double[] p4){
        this.name = name;
        this.coordinates[0] = p1;
        this.coordinates[1] = p2;
        this.coordinates[2] = p3;
        this.coordinates[3] = p4;
        this.minx = p1[0];
        this.maxx = p1[0];
        this.miny = p1[1];
        this.maxy = p1[1];
        for (int i=0;i<4;i++){
            if (coordinates[i][0]<minx){
                minx = coordinates[i][0];
            }
            if (coordinates[i][1]<miny){
                miny = coordinates[i][1];
            }
            if (coordinates[i][0]>maxx){
                maxx = coordinates[i][0];
            }
            if (coordinates[i][1]>maxy){
                maxy = coordinates[i][1];
            }
        }
    }
    public boolean IsThere(double[] p){
        return p[0] <= maxx && p[0] >= minx && p[1] <= maxy && p[1] >= miny;
    }
}

class Bank {
    double[] coordinates;
    String name;
    Node branches;
    int noBranches;
    public Bank(String name, double[] p){
        this.name = name;
        this.coordinates = new double[2];
        this.coordinates[0]=p[0];
        this.coordinates[1]=p[1];
        this.noBranches = 0;
    }
}
class Branch extends Bank {
    String Bname;
    Bank main;
    public Branch(String name, String Bname, double[] p,Bank main){
        super(name,p);
        this.Bname = Bname;
        this.main = main;
    }
}