package s4.B193368;
import java.lang.*;
import s4.specification.*;


/*package s4.specification;
  //ここは、１回、２回と変更のない外部仕様である。
  public interface FrequencerInterface {     // This interface provides the design for frequency counter.
  void setTarget(byte  target[]); // set the data to search.
  void setSpace(byte  space[]);  // set the data to be searched target from.
  int frequency(); //It return -1, when TARGET is not set or TARGET's length is zero
  //Otherwise, it return 0, when SPACE is not set or SPACE's length is zero
  //Otherwise, get the frequency of TAGET in SPACE
  int subByteFrequency(int start, int end);
  // get the frequency of subByte of taget, i.e target[start], taget[start+1], ... , target[end-1].
  // For the incorrect value of START or END, the behavior is undefined.
  }
*/



public class Frequencer implements FrequencerInterface{
    // Code to start with: This code is not working, but good start point to work.
    byte [] myTarget;
    byte [] mySpace;
    boolean targetReady = false;
    boolean spaceReady = false;

    int []  suffixArray; // Suffix Arrayの実装に使うデータの型をint []とせよ。


    // The variable, "suffixArray" is the sorted array of all suffixes of mySpace.                                    
    // Each suffix is expressed by a integer, which is the starting position in mySpace. 
                            
    // The following is the code to print the contents of suffixArray.
    // This code could be used on debugging.                                                                

    private void printSuffixArray() {
        if(spaceReady) {
            for(int i=0; i< mySpace.length; i++) {
                int s = suffixArray[i];
                for(int j=s;j<mySpace.length;j++) {
                    System.out.write(mySpace[j]);
                }
                System.out.write('\n');
            }
        }
    }

    private int suffixCompare(int i, int j) {
        // suffixCompareはソートのための比較メソッドである。
        // 次のように定義せよ。
        // comparing two suffixes by dictionary order.
        // suffix_i is a string starting with the position i in "byte [] mySpace".
        // Each i and j denote suffix_i, and suffix_j.                            
        // Example of dictionary order                                            
        // "i"      <  "o"        : compare by code                              
        // "Hi"     <  "Ho"       ; if head is same, compare the next element    
        // "Ho"     <  "Ho "      ; if the prefix is identical, longer string is big  
        //  
        //The return value of "int suffixCompare" is as follows. 
        // if suffix_i > suffix_j, it returns 1   
        // if suffix_i < suffix_j, it returns -1  
        // if suffix_i = suffix_j, it returns 0;   

        // ここにコードを記述せよ
        String suffix_i = new String(mySpace).substring(i);
        String suffix_j = new String(mySpace).substring(j);
        int rt = suffix_i.compareTo(suffix_j);
       
        
        if(rt < 0){//iが先
            return -1;
        }else if(rt > 0){//jが先
            return 1;
        }else{
            return 0;
        }
        // この行は変更しなければいけない。
    }

    public void setSpace(byte []space) { 
        // suffixArrayの前処理は、setSpaceで定義せよ。
        mySpace = space; if(mySpace.length>0) spaceReady = true;
        // First, create unsorted suffix array.
        suffixArray = new int[space.length];
        // put all suffixes in suffixArray.
        for(int i = 0; i< space.length; i++) {
            suffixArray[i] = i; // Please note that each suffix is expressed by one integer.      
        }
        //                                            
        // ここに、int suffixArrayをソートするコードを書け。
        /*バブルソートのfor文をクイックソートに書き換える
        for(int i=0;i<suffixArray.length-1;i++){
            for(int j=suffixArray.length-1;j>i;j--){
                //if (suffixArray[j - 1] > suffixArray[j]) {
                if (suffixCompare(suffixArray[j - 1], suffixArray[j]) == 1) {
                    // 入れ替え
                    int tmp = suffixArray[j - 1];
                    suffixArray[j - 1] = suffixArray[j];
                    suffixArray[j] = tmp;
                }
            }
        }*/
        quick_sort(suffixArray, 0, suffixArray.length-1);
        // 　順番はsuffixCompareで定義されるものとする。    
    }
    
    public void quick_sort(int[] d, int left, int right){
        if(left>=right){
            return;
        }
        int p=left;
        int l=left, r=right, tmp;
        while(l<=r){
            while(suffixCompare(d[l],d[p])==-1){
                l++;
            }
            while(suffixCompare(d[r],d[p])==1){
                r--;
            }
            if(l<=r){
                tmp=d[l]; d[l]=d[r]; d[r]=tmp;
                l++; r--;
            }
        }
        quick_sort(d, left, r);//ピボットより左側をクイックソート
        quick_sort(d, l, right);//ピボットより右側をクイックソート
    }
    

    // Suffix Arrayを用いて、文字列の頻度を求めるコード
    // ここから、指定する範囲のコードは変更してはならない。

    public void setTarget(byte [] target) {
        myTarget = target; if(myTarget.length>0) targetReady = true;
    }

    public int frequency() {
        if(targetReady == false) return -1;
        if(spaceReady == false) return 0;
        return subByteFrequency(0, myTarget.length);
    }



    public int subByteFrequency(int start, int end) {
        // This method be work as follows, but much more efficient
        /*   int spaceLength = mySpace.length;
           int count = 0;                                        
           for(int offset = 0; offset< spaceLength - (end - start); offset++) {
            boolean abort = false; 
            for(int i = 0; i< (end - start); i++) {
             if(myTarget[start+i] != mySpace[offset+i]) { abort = true; break; }
            }
            if(abort == false) { count++; }
           }
        */
        int first = subByteStartIndex(start, end);
        int last1 = subByteEndIndex(start, end);
        return last1 - first;
    }
    // 変更してはいけないコードはここまで。

    private int targetCompare(int i, int j, int k) {
        // suffixArrayを探索するときに使う比較関数。
        // 次のように定義せよ
        String suffix_i = new String(mySpace).substring(i);
        String target_j_k = new String(myTarget).substring(j,k);
        int rt = suffix_i.compareTo(target_j_k);
        // suffix_i is a string in mySpace starting at i-th position.
        // target_j_k is a string in myTarget start at j-th postion ending k-th position.
        // comparing suffix_i and target_j_k.
        // if the beginning of suffix_i matches target_i_k, it return 0.
        // The behavior is different from suffixCompare on this case.
        // if suffix_i > target_j_k it return 1;
        // if suffix_i < target_j_k it return -1;
        // It should be used to search the appropriate index of some suffix.
        // Example of search 
        // suffix          target
        // "o"       >     "i"
        // "o"       <     "z"
        // "o"       =     "o"
        // "o"       <     "oo"
        // "Ho"      >     "Hi"
        // "Ho"      <     "Hz"
        // "Ho"      =     "Ho"
        // "Ho"      <     "Ho "   : "Ho " is not in the head of suffix "Ho"
        // "Ho"      =     "H"     : "H" is in the head of suffix "Ho"
        //
        // ここに比較のコードを書け
        if(suffix_i.startsWith(target_j_k)){
            return 0;
        }
        if(rt < 0){//iが先
            return -1;
        }else if(rt > 0){//jが先
            return 1;
        }else{
            return 0;
        }

        //return 0; // この行は変更しなければならない。
    }


    private int subByteStartIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現が始まる位置を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho
           1: Ho
           2: Ho Hi Ho
           3:Hi Ho
           4:Hi Ho Hi Ho
           5:Ho
           6:Ho Hi Ho
           7:i Ho
           8:i Ho Hi Ho
           9:o
           A:o Hi Ho
        */

        // It returns the index of the first suffix 
        // which is equal or greater than target_start_end.                         
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho", it will return 5.                           
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                 
        // if target_start_end is "Ho ", it will return 6.                
        //                                                                          
        // ここにコードを記述せよ。
        for(int i=0;i<suffixArray.length;i++){
            if(targetCompare(suffixArray[i],start,end) == 0){
                return i;
            }
        }
        //                                                                         
        return suffixArray.length; //このコードは変更しなければならない。          
    }

    private int subByteEndIndex(int start, int end) {
        //suffix arrayのなかで、目的の文字列の出現しなくなる場所を求めるメソッド
        // 以下のように定義せよ。
        /* Example of suffix created from "Hi Ho Hi Ho"
           0: Hi Ho                 //5
           1: Ho                      //8
           2: Ho Hi Ho           //2
           3:Hi Ho               //6
           4:Hi Ho Hi Ho             //0
           5:Ho                    //9
           6:Ho Hi Ho            //3
           7:i Ho                 //7
           8:i Ho Hi Ho       //1
           9:o                      //A
           A:o Hi Ho             //4
        */
        // It returns the index of the first suffix 
        // which is greater than target_start_end; (and not equal to target_start_end)
        // Assuming the suffix array is created from "Hi Ho Hi Ho",                   
        // if target_start_end is "Ho", it will return 7 for "Hi Ho Hi Ho".  
        // Assuming the suffix array is created from "Hi Ho Hi Ho",          
        // if target_start_end is"i", it will return 9 for "Hi Ho Hi Ho".    
        //                                                                   
        //　ここにコードを記述せよ
        
        for(int i=suffixArray.length-1;i>=0;i--){
            if(targetCompare(suffixArray[i],start,end) == 0){
                return i+1;
            }
        }

        //                                                                   
        return suffixArray.length; // この行は変更しなければならない、       
    }


    // Suffix Arrayを使ったプログラムのホワイトテストは、
    // privateなメソッドとフィールドをアクセスすることが必要なので、
    // クラスに属するstatic mainに書く方法もある。
    // static mainがあっても、呼びださなければよい。
    // 以下は、自由に変更して実験すること。
    // 注意：標準出力、エラー出力にメッセージを出すことは、
    // static mainからの実行のときだけに許される。
    // 外部からFrequencerを使うときにメッセージを出力してはならない。
    // 教員のテスト実行のときにメッセージがでると、仕様にない動作をするとみなし、
    // 減点の対象である。
    public static void main(String[] args) {
        Frequencer frequencerObject;
        try {
            frequencerObject = new Frequencer();
            frequencerObject.setSpace("Hi Ho Hi Ho".getBytes());
            frequencerObject.printSuffixArray(); // you may use this line for DEBUG
            /* Example from "Hi Ho Hi Ho"    
               0: Hi Ho                      
               1: Ho                         
               2: Ho Hi Ho                   
               3:Hi Ho                       
               4:Hi Ho Hi Ho                 
               5:Ho                          
               6:Ho Hi Ho                    
               7:i Ho                        
               8:i Ho Hi Ho                  
               9:o                           
               A:o Hi Ho                     
            */

            frequencerObject.setTarget("H".getBytes());
            //                                         
            // ****  Please write code to check subByteStartIndex, and subByteEndIndex
            //

            int result = frequencerObject.frequency();
            System.out.print("Freq = "+ result+" ");
            if(4 == result) { System.out.println("OK"); } else {System.out.println("WRONG"); }
        }
        catch(Exception e) {
            System.out.println("STOP");
        }
    }
}

