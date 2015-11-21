package search;

import java.io.*;
import java.util.*;

/**
 * This class encapsulates an occurrence of a keyword in a document. It stores the
 * document name, and the frequency of occurrence in that document. Occurrences are
 * associated with keywords in an index hash table.
 * 
 * @author Sesh Venugopal
 * 
 */
class Occurrence {
	/**
	 * Document in which a keyword occurs.
	 */
	String document;
	
	/**
	 * The frequency (number of times) the keyword occurs in the above document.
	 */
	int frequency;
	
	/**
	 * Initializes this occurrence with the given document,frequency pair.
	 * 
	 * @param doc Document name
	 * @param freq Frequency
	 */
	public Occurrence(String doc, int freq) {
		document = doc;
		frequency = freq;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + document + "," + frequency + ")";
	}
}

/**
 * This class builds an index of keywords. Each keyword maps to a set of documents in
 * which it occurs, with frequency of occurrence in each document. Once the index is built,
 * the documents can searched on for keywords.
 *
 */
public class LittleSearchEngine {
	
	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
	 * an array list of all occurrences of the keyword in documents. The array list is maintained in descending
	 * order of occurrence frequencies.
	 */
	HashMap<String,ArrayList<Occurrence>> keywordsIndex;
	
	/**
	 * The hash table of all noise words - mapping is from word to itself.
	 */
	HashMap<String,String> noiseWords;
	
	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
		noiseWords = new HashMap<String,String>(100,2.0f);
	}
	
	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all keywords,
	 * each of which is associated with an array list of Occurrence objects, arranged
	 * in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile Name of file that has a list of all the document file names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) 
	throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.put(word,word);
		}
		
		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String,Occurrence> kws = loadKeyWords(docFile);
			mergeKeyWords(kws);
		}
		
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword occurrences
	 * in the document. Uses the getKeyWord method to separate keywords from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String,Occurrence> loadKeyWords(String docFile) 
	throws FileNotFoundException {
		// COMPLETE THIS METHOD
		//System.out.println("hi from loadkeywords");
		//System.out.println("file: " +docFile);
		
		String keyword = "";
		
		HashMap<String,Occurrence> keywordHash = new HashMap<String,Occurrence>(1000,2.0f);
		
		Occurrence plinko; 
		
		Scanner sc = new Scanner(new File(docFile));
		
		while (sc.hasNext()) {//iterating through a single text document
			String word = sc.next();
			//System.out.println(":::::"+word+"::::::");
			keyword = getKeyWord(word);
			if(keyword==null){//if not a keyword, keep going
				continue;
			}else{//this is a keyword
				
				if(keywordHash.containsKey(keyword)){//first, check if keyword already there. If so, simply advance counter
					plinko = keywordHash.get(keyword);
					
					plinko.frequency++;
					//System.out.println("*  ** * ***** ** * * * * * * * * Keyword " + keyword + " already exits in docFile name "+docFile+" so advanced frequency count to "+plinko.frequency);
				
				
				}else{//just add to table with a freq of 1
				
				plinko = new Occurrence(docFile, 1);
				keywordHash.put(keyword, plinko);
				//System.out.println("# # # # # #  #  #  #  # ## # # #  # #Added to keyword " + keyword + " to list with docFile name "+docFile+" and frequency set to 1");
				
				}
				
				
			}
			
		}//end while
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return keywordHash;
	}
	
	/**
	 * Merges the keywords for a single document into the master keywordsIndex
	 * hash table. For each keyword, its Occurrence in the current document
	 * must be inserted in the correct place (according to descending order of
	 * frequency) in the same keyword's Occurrence list in the master hash table. 
	 * This is done by calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeyWords(HashMap<String,Occurrence> kws) {
		// COMPLETE THIS METHOD
		ArrayList<Occurrence> occs;
		
		//System.out.println("A list of keywords: ");
		for(String keyword: kws.keySet()){//iterates through keywords in hashmap kws
			
			
			if(keywordsIndex.containsKey(keyword)){
				//then call the insertLast occurrence method and so forth. . . . 
				occs = keywordsIndex.get(keyword);
				occs.add(kws.get(keyword));
				insertLastOccurrence(occs);
				keywordsIndex.put(keyword, occs);
				
			}else{
				//just add the keyword and corresponding occurrence to the master keywordsIndex
				occs = new ArrayList<Occurrence>();
				occs.add(kws.get(keyword));
				insertLastOccurrence(occs);
				keywordsIndex.put(keyword, occs);
			}
			
			
			
		}//end for loop
		
		//prints keywords
		//System.out.println("Printing out from the Global keywords index table:");
		//for(String keyword: keywordsIndex.keySet()){
		//	System.out.println(keyword+", and the value is: "+keywordsIndex.get(keyword));
		//}
		
	}
	
	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of any
	 * TRAILING punctuation, consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyWord(String word) {
		// COMPLETE THIS METHOD
		word = word.toLowerCase();
		//System.out.println("From getKeyWord: " + word);
		
		//if first char is punctuation, we're done
		if(word.charAt(0)=='.'||word.charAt(0)==','||word.charAt(0)=='?'||
					word.charAt(0)==':'||word.charAt(0)==';'||word.charAt(0)=='!'){
						//System.out.println("Puctuation at front: return null");
						return null;
					}
		
		//strip punctuation from end
		while(true){
			if(word.charAt(word.length()-1)=='.'||word.charAt(word.length()-1)==','||word.charAt(word.length()-1)=='?'||
					word.charAt(word.length()-1)==':'||word.charAt(word.length()-1)==';'||word.charAt(word.length()-1)=='!'){
				
				word = word.substring(0, word.length()-1);
				//System.out.println("Stripping: " + word);
			}else{
				break;
			}
			
		}//end while
		
		//System.out.println("End result Stripping:" + word);
		
		//make sure whatever's left is a letter
		
		for(int i=0;i<word.length();i++){
			if(!Character.isLetter(word.charAt(i))){
				//System.out.println("Word will be returned null as '"+word.charAt(i)+ "' is not a letter");
				return null;
			}
		}
		
		if(noiseWords.containsKey(word)){
			//System.out.println(word + " failed! It is a noise word, Retuning null-------------------------");
			return null;
		}else{
			//System.out.println(word + " passed! It will be added to the keywords table++++++++++++++++++++");
			return word;
		}
		
		
		
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		//return null;
	}
	
	/**
	 * Inserts the last occurrence in the parameter list in the correct position in the
	 * same list, based on ordering occurrences on descending frequencies. The elements
	 * 0..n-2 in the list are already in the correct order. Insertion of the last element
	 * (the one at index n-1) is done by first finding the correct spot using binary search, 
	 * then inserting at that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary search process,
	 *         null if the size of the input list is 1. This returned array list is only used to test
	 *         your code - it is not used elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		// COMPLETE THIS METHOD
		
		
		
		if(occs.size()==1){
			//System.out.println("Returning nul++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
			return null;
		}
		
		
		
		int targetFreq = occs.get(occs.size()-1).frequency;//get the last addition's frequency
		Occurrence temp = occs.get(occs.size()-1);
		
		//System.out.println("Inserting "+temp+" into "+occs);
		
		//System.out.println("THis is the freq "+freq);
	
		
		int low = 0;
		int high = occs.size()-2;
		int middle;
		
		ArrayList<Integer> returnedValues = new ArrayList<Integer>();//array of midpoints to return
		
		while(low <= high){
			
			middle = (low+high)/2;
			returnedValues.add(middle);
			
			//System.out.println("High: "+high+", Middle: "+middle+", Low: "+low);
			//System.out.println("Returned values: "+returnedValues);
	
	if(targetFreq==occs.get(middle).frequency){
				
				occs.add(middle, temp);
				occs.remove(occs.size()-1);
				return returnedValues;
				
				
			}
			
			
		if(high==low&&low==middle){
			//System.out.println("Hi from hi low middle!");
			
			if(targetFreq >occs.get(middle).frequency){
					occs.add(middle, temp);
					occs.remove(occs.size()-1);
					return returnedValues;
				}
			
			occs.add(middle+1, temp);
			occs.remove(occs.size()-1);
			return returnedValues;
				
				
			}
			
			
		
			
			if(targetFreq > occs.get(middle).frequency){
				high = middle -1;
			}
			else if(targetFreq < occs.get(middle).frequency){
				low = middle +1;
			}else{
				
				break;
			}
		
		}//end while
		
		
		//////////
		
		if(returnedValues.get(returnedValues.size()-1)==0){//
			if(temp.frequency < occs.get(0).frequency){
				occs.add(1, temp);
				occs.remove(occs.size()-1);
				return returnedValues;
			}
		}
		
		occs.add(returnedValues.get(returnedValues.size()-1), temp);
		
		occs.remove(occs.size()-1);
		return returnedValues;
		
		
		
		
		
		
		
		
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		//return null;
	}
	
	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
	 * document. Result set is arranged in descending order of occurrence frequencies. (Note that a
	 * matching document will only appear once in the result.) Ties in frequency values are broken
	 * in favor of the first keyword. (That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2
	 * also with the same frequency f1, then doc1 will appear before doc2 in the result. 
	 * The result set is limited to 5 entries. If there are no matching documents, the result is null.
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of NAMES of documents in which either kw1 or kw2 occurs, arranged in descending order of
	 *         frequencies. The result size is limited to 5 documents. If there are no matching documents,
	 *         the result is null.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		// COMPLETE THIS METHOD
		//System.out.println("Hey ho from TOP5!");
		kw1 = kw1.toLowerCase();
		kw2 = kw2.toLowerCase();
		
		//if first keyword found, but not second, then just make top five from kw1
		if(keywordsIndex.containsKey(kw1) && !keywordsIndex.containsKey(kw2)){
			ArrayList<Occurrence> arr1 = keywordsIndex.get(kw1);
			ArrayList<String> str = new ArrayList<String>();
			
			if(arr1.size()>=5){
				for(int i = 0;i<5;i++){
					str.add(arr1.get(i).document);
				}
			}else{
				for(int i = 0;i<arr1.size();i++){
					str.add(arr1.get(i).document);
				}
			}//end else
			
			//System.out.println("Here is the resulting string arraylist: "+str);
			return str;
		}//end if first kwd but not second
		
		//if second keyword found, but not first
		if(!keywordsIndex.containsKey(kw1) && keywordsIndex.containsKey(kw2)){
			ArrayList<Occurrence> arr2 = keywordsIndex.get(kw2);
			ArrayList<String> str = new ArrayList<String>();
			
			if(arr2.size()>=5){
				for(int i = 0;i<5;i++){
					str.add(arr2.get(i).document);
				}
			}else{
				for(int i = 0;i<arr2.size();i++){
					str.add(arr2.get(i).document);
				}
			}//end else
			
			//System.out.println("Here is the resulting string arraylist: "+str);
			return str;
		}//end if 2nd kwd but not 1st
		
		
		
		ArrayList<Occurrence> arr1 = keywordsIndex.get(kw1);
		ArrayList<Occurrence> arr2 = keywordsIndex.get(kw2);
		ArrayList<String> str = new ArrayList<String>();
		ArrayList<String> temp = new ArrayList<String>();
		int arr1index = 0, arr2index = 0;
		int arr1length = arr1.size(), arr2length = arr2.size();
		//System.out.println(kw1+", and the value is: "+arr1+", and the length of the occurence array is: "+arr1length);
		//System.out.println(kw2+", and the value is: "+arr2+", and the length of the occurence array is: "+arr2length);
		
		
		//System.out.println(arr1.get(0).frequency);
		//System.out.println(arr1.get(0).document);
		
		while(arr1index<arr1length && arr2index<arr2length){
	
			
			//if the current item in arr1 has a higher frequency than arr2
			if(arr1.get(arr1index).frequency > arr2.get(arr2index).frequency){
			
					//add to temp advance index, continue
				if(!temp.contains(arr1.get(arr1index).document)){
					temp.add(arr1.get(arr1index).document);
				}

				arr1index++;
					continue;
			
				
			}//end if arr1>arr2
			
			//if the current item in arr2 has a higher frequency than arr1,
			if(arr1.get(arr1index).frequency < arr2.get(arr2index).frequency){
				
					//add to temp adv index, continue
				if(!temp.contains(arr2.get(arr2index).document)){
					temp.add(arr2.get(arr2index).document);
					}
					arr2index++;
					continue;
				
				
			}//end if arr1<arr2
			
			
			//must have same freq, so add both, advance both, with arr1 first
			if(!temp.contains(arr1.get(arr1index).document)){
			
			temp.add(arr1.get(arr1index).document);
			}
			
			
			arr1index++;
			
			if(!temp.contains(arr2.get(arr2index).document)){
			temp.add(arr2.get(arr2index).document);
			}
			
			
			arr2index++;
			continue;
			

			
		}//while ends
		
		
		//System.out.println("The first while loop ended, and Here is what we've got in temp: "+temp);
		
		while(arr1index<arr1length){
			if(!temp.contains(arr1.get(arr1index).document)){
			temp.add(arr1.get(arr1index).document);}
			arr1index++;
		}
		
		while(arr2index<arr2length){
			if(!temp.contains(arr2.get(arr2index).document)){
			temp.add(arr2.get(arr2index).document);}
			arr2index++;
		}
		//System.out.println("Rest of crap added to temp: " +temp);
		
		int tempSize = temp.size();
		
		for(int i = 0;i<tempSize;i++){
			
				str.add(temp.get(i));
			
			
			if(str.size()==5){
				break;
			}
		}//end for loop
		
		//System.out.println("This should be final: "+str);
		
		// THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		return str;
	}
}
