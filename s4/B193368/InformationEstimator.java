//2020/02/18
package s4.B193368; // Please modify to s4.Bnnnnnn, where nnnnnn is your student ID.
import java.lang.*;
import s4.specification.*;
import java.util.HashMap;
/* What is imported from s4.specification
package s4.specification;
public interface InformationEstimatorInterface{
    void setTarget(byte target[]); // set the data for computing the information quantities
    void setSpace(byte space[]); // set data for sample space to computer probability
    double estimation(); // It returns 0.0 when the target is not set or Target's length is zero;
// It returns Double.MAX_VALUE, when the true value is infinite, or space is not set.
// The behavior is undefined, if the true value is finete but larger than Double.MAX_VALUE.
// Note that this happens only when the space is unreasonably large. We will encounter other problem anyway.
// Otherwise, estimation of information quantity, 
}                        
*/

public class InformationEstimator implements InformationEstimatorInterface{
    // Code to tet, *warning: This code condtains intentional problem*
    byte [] myTarget; // data to compute its information quantity
    byte [] mySpace;  // Sample space to compute the probability
    FrequencerInterface myFrequencer;  // Object for counting frequency頻度をカウントするためのオブジェクト
    //HashMap<String,Double> array;//Map

    byte [] subBytes(byte [] x, int start, int end) {
	// corresponding to substring of String for  byte[] ,
	// It is not implement in class library because internal structure of byte[] requires copy.
	byte [] result = new byte[end - start];
	for(int i = 0; i<end - start; i++) { result[i] = x[start + i]; };
	return result;
    }

    // IQ: information quantity for a count,  -log2(count/sizeof(space))
    double iq(int freq) {
        return  - Math.log10((double) freq / (double) mySpace.length)/ Math.log10((double) 2.0);
    }

    public void setTarget(byte [] target) { myTarget = target;}
    public void setSpace(byte []space) {
        myFrequencer = new Frequencer();
        mySpace = space; myFrequencer.setSpace(space);
        //array = new HashMap<String,Double>();//初期化
    }

    public double estimation(){
    double [] Iq = new double[myTarget.length];
    double value1 = (double) 0.0;
    for( int i = 0 ; i < myTarget.length ; i++ ){
        for( int j = 0 ; j <= i ; j++ ){
            if(j == 0){
                myFrequencer.setTarget(subBytes(myTarget, j, i+1));
                value1 = iq(myFrequencer.frequency());
                Iq[i] = value1;
            }else{
                myFrequencer.setTarget(subBytes(myTarget, j, i+1));
                value1 = Iq[j-1] + iq(myFrequencer.frequency());
                if(Iq[i] > value1) Iq[i] = value1;
            }
        }
    }
    return Iq[myTarget.length-1];
    /*
	boolean [] partition = new boolean[myTarget.length+1];
	int np;
	np = 1<<(myTarget.length-1);
	// System.out.println("np="+np+" length="+myTarget.length);
	double value = Double.MAX_VALUE; // value = mininimum of each "value1".

	for(int p=0; p<np; p++) { // There are 2^(n-1) kinds of partitions.
	    // binary representation of p forms partition.
	    // for partition {"ab" "cde" "fg"}
	    // a b c d e f g   : myTarget
	    // T F T F F T F T : partition:
	    partition[0] = true; // I know that this is not needed, but..
	    for(int i=0; i<myTarget.length -1;i++) {
		partition[i+1] = (0 !=((1<<i) & p));
	    }
	    partition[myTarget.length] = true;

	    // Compute Information Quantity for the partition, in "value1" value1の分割の情報量を計算
	    // value1 = IQ(#"ab")+IQ(#"cde")+IQ(#"fg") for the above example
        double value1 = (double) 0.0;
	    int end = 0;
	    int start = end;
	    while(start<myTarget.length) {
            // System.out.write(myTarget[end]);
            end++;
		while(partition[end] == false) { 
		    // System.out.write(myTarget[end]);
		    end++;
		}
        String target;
        target = new String(subBytes(myTarget,start,end));
		// System.out.print("("+start+","+end+")");
            if(!array.containsKey(target)){//指定したキーが含まれていない場合
                myFrequencer.setTarget(subBytes(myTarget, start, end));
                array.put(target,iq(myFrequencer.frequency()));//targetとその情報量をリストに追加
            }
		value1 = value1 + array.get(target);//指定した文字の情報量をたす
		start = end;
	    }
	    // System.out.println(" "+ value1);

	    // Get the minimal value in "value"最小値を取得
	    if(value1 < value) value = value1;
	}
	return value;*/
    }

    public static void main(String[] args) {
        InformationEstimator myObject;
        double value;
        myObject = new InformationEstimator();
        myObject.setSpace("3210321001230123".getBytes());
        myObject.setTarget("0".getBytes());
        value = myObject.estimation();
        System.out.println(">0 "+value);
        myObject.setTarget("01".getBytes());
        value = myObject.estimation();
        System.out.println(">01 "+value);
        myObject.setTarget("0123".getBytes());
        value = myObject.estimation();
        System.out.println(">0123 "+value);
        myObject.setTarget("00".getBytes());
        value = myObject.estimation();
        System.out.println(">00 "+value);
    }
}
				  
			       

	
    
