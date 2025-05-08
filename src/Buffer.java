public class Buffer {
    Page page=null;
    int timestamp=0;
    int pincount=0;
    Boolean dirty=false;

public Buffer(){

}
public Page clear(){
    System.out.println("remove "+this.page.pageid);
     this.page=null;
    this.timestamp=0;
    this.pincount=0;
     this.dirty=false;
     return null;

}
public void update(int timestamp){
    this.timestamp=timestamp;
    pincount+=1;

}
public void insert(int timestamp,Page page){
    this.timestamp=timestamp;
    this.page=page;
    pincount+=1;
    System.out.println("read "+this.page.pageid);

}
public Page write(){
    System.out.println("write "+this.page.pageid);
    return clearwithwrite();

}
public Page clearwithwrite(){
    System.out.println("remove "+this.page.pageid);
    Page release=this.page;
    this.page=null;
    this.timestamp=0;
    this.pincount=0;
    this.dirty=false;
    return release;

}
public Page remove(){
    //check if it is dirty or not
    if (dirty){
       return write();

    }else{
        return clear();

    }

}
public void release(){
    pincount-=1;
}
public void releasewithmodification(){
    //need to update the timestamp? when it is release with modification
    pincount-=1;
    dirty=true;
}

}
