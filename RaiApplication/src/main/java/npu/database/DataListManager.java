package npu.database;

import it.unipr.ce.dsg.s2p.org.json.JSONException;
import it.unipr.ce.dsg.s2p.org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 *Class <code>DataListManager</code> extends <code>Hashtable</code>
 *used to hold the single Data row of database.
 *<p>
 * 
 * @author Le, Tuan
 *
 */

public class DataListManager extends
								Hashtable<String, SingleData>{
	private static final long serialVersionUID = -6530442422393120638L;
	/** Create new DataListManager
	 * 
	 *
	*/
	public DataListManager(){
		super();
	}
	
	 /**
     * Create a new DataListManager
     * 
     * @param initialCapacity the initial capacity of the data list
     * @param loadFactor the load factor of the data list
     */
    public DataListManager(int initialCapacity, float loadFactor) {
        super (initialCapacity, loadFactor);

    }
    
    /**
     * Create a new DataListManager
     *  
     * @param initialCapacity the initial capacity of the peer list
     */
    public DataListManager(int initialCapacity) {
        super (initialCapacity);

    }

    /**
     * Create a new DataListManager
     * 
     * @param map the map whose mappings are to be placed in this data list.
     */
    public DataListManager(
            Map<? extends String, ? extends SingleData> map) {
        super (map);

    }
    
    /**
     * Get the list with max <code>number</code> descriptor
     * 
     * @param number the number max of element returned in the list
     * @return PeerListManager PeerListManager
     */
    public DataListManager getRandomDatas(int number) {

        if (number > 0) {
            //create new list
        	DataListManager newDataList = new DataListManager(number);
            Iterator<String> iter = this .keySet().iterator();

            //create array list to contain random key generated 
            ArrayList<Integer> arrayRdmNumber = new ArrayList<Integer>(
                    number);
            //get the size of peer list
            int nKeys = this .size();
            //initialize random number
            int rdmNumber = 0;

            //create a list of random number
            while (arrayRdmNumber.size() < number) {

                rdmNumber = (int) (Math.random() * nKeys);
                //if rdmNumber not exists add 
                if (!arrayRdmNumber.contains(rdmNumber))
                    arrayRdmNumber.add(rdmNumber);

            }

            // initialize index for while  
            int i = 0;
            while (iter.hasNext()) {

                //set current key
                String key = iter.next();
                //if key is equal to random key add it into the list
                if (arrayRdmNumber.contains(i)) {

                    newDataList.put(key, this .get(key));
                }
                i++;
            }

            return newDataList;

        } else
            return null;

    }//end getRandomDatas
    
    /**
     * Read list from the InputStream (es. file)
     * 
     * @param istream InputStream
     * @return boolean return true if the InputStream has been read
     */
    synchronized public boolean readList(InputStream istream) {

        //read the stream
        BufferedReader buffer = new BufferedReader(
                new InputStreamReader(istream));

        try {

            //create a json object
            JSONObject jsonObj = new JSONObject(buffer.readLine());
            buffer.close();

            JSONObject paramsPD;
            Iterator<String> peerKeys = jsonObj.keys();

            //parse json peer list
            while (peerKeys.hasNext()) {

                String peerKey = peerKeys.next();

                paramsPD = jsonObj.getJSONObject(peerKey);

                SingleData peerD = new SingleData();

                peerD.setRowid(paramsPD.getString("rowid"));
                peerD.setVersion(paramsPD.getString("version"));
                peerD.setTypes(paramsPD.getString("types"));
                peerD.setGivenname(paramsPD.getString("givenname"));
                peerD.setMiddlename(paramsPD.getString("middlename"));
                peerD.setFamilyname(paramsPD.getString("familyname"));
                peerD.setGender  (paramsPD.getString("gender"));
                peerD.setSpinphone(paramsPD.getString("spinphone"));
                peerD.setPhone(paramsPD.getString("phone"));
                peerD.setSpinemail(paramsPD.getString("spinemail"));
                peerD.setEmail(paramsPD.getString("email"));
                peerD.setSpinim(paramsPD.getString("spinim"));
                peerD.setIm(paramsPD.getString("im"));
                peerD.setSpinaddr(paramsPD.getString("spinaddr"));
                peerD.setStreet(paramsPD.getString("street"));
                peerD.setPobox(paramsPD.getString("pobox"));
                peerD.setCity(paramsPD.getString("city"));
                peerD.setState(paramsPD.getString("state"));
                peerD.setZipcode(paramsPD.getString("zipcode"));
                peerD.setCountry(paramsPD.getString("country"));
                peerD.setSpinsns(paramsPD.getString("spinsns"));
                peerD.setSns(paramsPD.getString("sns"));
                peerD.setSpinorg1(paramsPD.getString("spinorg1"));
                peerD.setOrg1(paramsPD.getString("org1"));
                peerD.setSpinorg2(paramsPD.getString("spinorg2"));
                peerD.setOrg2(paramsPD.getString("org2"));
                peerD.setNotes(paramsPD.getString("notes"));
                peerD.setTime(paramsPD.getString("time"));
                peerD.setPhoto (paramsPD.getString("photo"));



                this .put(peerKey, new SingleData(peerD));

            }

        } catch (IOException e) {
            return false;
        } catch (JSONException e) {
            return false;
        }

        return true;
    }
    
    /**
     * Write list to OutputStream (es. file). The format is JSON.
     * 
     * @param ostream OutputStream
     * @return boolean return true if the OutputStream has been write
     */
    synchronized public boolean writeList(OutputStream ostream) {

        try {
            JSONObject peerList = new JSONObject(this );

            //File newFile = new File(filePath+"cachelist.json");
            PrintStream printList = new PrintStream(ostream);
            printList.println(peerList.toString());
            printList.close();

        } catch (Exception e) {
            new RuntimeException(e);
            return false;
        }

        return true;
    }
    
}//end class

