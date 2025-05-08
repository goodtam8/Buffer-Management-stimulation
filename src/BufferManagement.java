
import java.util.Scanner;
import java.util.ArrayList;
//need to show we write it to the disk??
public class BufferManagement {
    Buffer[] bufferpool;
    Disk disk;

    public BufferManagement(int i, int size) {
        bufferpool = new Buffer[i];
        for (int j = 0; j < bufferpool.length; j++) {
            bufferpool[j] = new Buffer();
        }
        disk=new Disk(size);
    }

    public static int[] castarray(String[] arr) {
        int[] intarray = new int[3];
        for (int i = 0; i < intarray.length; i++) {
            intarray[i] = Integer.parseInt(arr[i]);
        }
        return intarray;
    }

    public void release(Page page) {

        for (int i = 0; i < bufferpool.length; i++) {

            if (bufferpool[i].page.pageid == page.pageid) {
                //search whether the page is already in buffer
                bufferpool[i].release();
                return;
            }
        }

    }

    public void releasewithmodif(Page page) {

        for (int i = 0; i < bufferpool.length; i++) {

                if (bufferpool[i].page.pageid == page.pageid) {
                    //search whether the page is already in buffer
                    bufferpool[i].releasewithmodification();
                    return;
                }
            }

    }

    public void request(int pid, int operation, Page page) throws Exception {
        for (int i = 0; i < bufferpool.length; i++) {
            if(bufferpool[i].page!=null){
            if (bufferpool[i].page.pageid == pid) {
                //search whether the page is already in buffer
                bufferpool[i].update(operation);
                return;
                //terminate the operation
            }
        }}
        //if the page is not in bufferpool search for space first
        Boolean space = search();
        if (space == true) {
            //if it has space
            read(operation, page);
        } else {
            //if no
            replacement(operation, page);


        }

    }

    public void replacement(int operation, Page page) throws Exception {
        //finding which page in the buffer have pin count 0
        ArrayList<Buffer> fulfilled = new ArrayList<>();
        for (int i = 0; i < bufferpool.length; i++) {
            if (bufferpool[i].pincount == 0) {
                fulfilled.add(bufferpool[i]);
            }

        }
        Buffer removed = replacement(fulfilled);
        //search for where is this buffer get the index which will be free
        int replace = search(removed);
        Page hasbeenremove=bufferpool[replace].remove();
        if(hasbeenremove!=null){
            //mean the file has been modify and update the disk
            disk.disklot[hasbeenremove.pageid-1]=hasbeenremove;

        }

        //insert the new page
        bufferpool[replace].insert(operation, page);


    }

    public int search(Buffer i) throws Exception {
        for (int j = 0; j < bufferpool.length; j++) {
            if (bufferpool[j] == i) {
                return j;
            }
        }
        throw new Exception("an unexpected error occured");
    }

    public Buffer replacement(ArrayList<Buffer> list) {
        //using lru
        int min = list.get(0).timestamp;
        Buffer pagetoberemove = list.get(0);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).timestamp < min) {
                min = list.get(i).timestamp;
                pagetoberemove = list.get(i);
            }
        }
        return pagetoberemove;
    }

    public void read(int operation, Page page) throws Exception {
        int indextoinsert = indexsearch();
        bufferpool[indextoinsert].insert(operation, page);


    }

    public int indexsearch() throws Exception {
        for (int i = 0; i < bufferpool.length; i++) {
            if (bufferpool[i].page == null) {
                return i;
            }

        }
        throw new Exception("there is an unexpected error");
    }

    public Boolean search() {
        for (int i = 0; i < bufferpool.length; i++) {
            if (bufferpool[i].page == null) {
                return true;//have space
            }
        }
        return false;// does not have space do replacement
    }

    public void idencase(String operation, Disk disk, int stamp) throws Exception {
        String[] splitstring = operation.split(" ", 2);
        int pid = Integer.parseInt(splitstring[1]);

        switch (splitstring[0]) {
            case "request":
                request(pid, stamp, disk.disklot[pid-1]);
                return;
            case "release":
                releasewithmodif(disk.disklot[pid-1]);
                return;
            case "release*":
                release(disk.disklot[pid-1]);

                return;
            default:
                System.out.println("invalid operation");
        }
    }


    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        // Read the first line of input
        String line1input = in.nextLine();

        // Split the input line into an array of strings
        String[] splitstring = line1input.split(" ");

        // Remove any extra whitespace (though split should handle it)
        for (int i = 0; i < splitstring.length; i++) {
            splitstring[i] = splitstring[i].trim();
        }

        // Convert the string array to an integer array
        int[] infoarray = castarray(splitstring);

        // Create instances of BufferManagement and Disk
        BufferManagement manage = new BufferManagement(infoarray[0], infoarray[1]);

        // List to hold operations
        ArrayList<String> operation = new ArrayList<>();

        // Read the number of operations from the first line
        int numberOfRequests = infoarray[2];

        // Read each operation from the input
        for (int i = 0; i < numberOfRequests; i++) {
            String operate = in.nextLine();
            operation.add(operate);
        }

        // Process each operation
        for (int i = 0; i < operation.size(); i++) {
            manage.idencase(operation.get(i), manage.disk, i);
        }

        // Close the scanner
        in.close();
    }}