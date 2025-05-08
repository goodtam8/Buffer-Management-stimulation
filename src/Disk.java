public class Disk {
    public Disk(int i){
        disklot=new Page[i];
        for (int j=0;j<disklot.length;j++){
            disklot[j]=new Page(j+1);
        }
    }
    Page[]disklot;
}
