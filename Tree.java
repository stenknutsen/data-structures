package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file. The root of the 
	 * tree is stored in the root field.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		String token = sc.nextLine();
		
		root = new TagNode(token.substring( 1,  token.length()-1), null, null);
		
		Stack<TagNode> tagStack = new Stack<TagNode>();
		
		tagStack.push(root);
		
		while(sc.hasNextLine()){
			token = sc.nextLine();
			
			if(token.charAt(0)=='<' && token.charAt(1)=='/'){
				tagStack.pop();
			}// end close tag
			
			else if(token.charAt(0)=='<'){
				TagNode temp = new TagNode(token.substring(1, token.length()-1), null, null);
				
				if(tagStack.peek().firstChild==null){
					tagStack.peek().firstChild = temp;
					tagStack.push(temp);
					
				}
				
				else{
					TagNode ptr = tagStack.peek().firstChild;
					while(ptr.sibling!=null){
						ptr = ptr.sibling;
					}
					ptr.sibling = temp;
					tagStack.push(temp);
				}//end open tag
			}
			else{
				TagNode temp = new TagNode(token, null, null);
				
				if(tagStack.peek().firstChild == null){
					tagStack.peek().firstChild = temp;
				}
				else{
					TagNode ptr = tagStack.peek().firstChild;
					
					while(ptr.sibling!=null){
						ptr = ptr.sibling;
					}//end while
					
					ptr.sibling = temp;
				}
			}
		}//end outer while
	}//end build method
	
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		if((oldTag.equals("b")||oldTag.equals("em")||oldTag.equals("p")) && (newTag.equals("b")||newTag.equals("em")||newTag.equals("p"))){
			replaceTag(oldTag, newTag, root);
		}
		if((oldTag.equals("ol")||oldTag.equals("ul")) && (newTag.equals("ol")||newTag.equals("ul"))){
			replaceTag(oldTag, newTag, root);
		}
		
		
	}
	
	private static void replaceTag(String oldTag, String newTag, TagNode root){
		
		if(root==null){
			return;
		}
		
		if(root.tag.equals(oldTag)&&root.firstChild!=null){
			root.tag = newTag;
		}
		
		replaceTag(oldTag, newTag, root.firstChild);
		
		replaceTag(oldTag, newTag, root.sibling);
		
		return;
	}//end replace helper method
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		TagNode prev = root;
		boldRow(prev, row);
		
	}
	
	public static void boldRow(TagNode prev, int row){
		
		if(prev==null){
			return;
		}
		
		if(prev.firstChild!=null&&prev.firstChild.tag.equals("table")){
			System.out.println("Found target at prev.firstchild: " + prev.firstChild.tag);
			TagNode ptr = prev.firstChild;
			ptr = ptr.firstChild;
			for(int i=1;i<row;i++){//go to correct row
				ptr = ptr.sibling;
				//System.out.println("row#: "+ i);
			}
			
			if(ptr==null){
				throw new IllegalArgumentException();
			}
			
			ptr = ptr.firstChild;//go to first column in row
			//System.out.println("this is the column were dealing with: "+ptr.firstChild.tag);
			
			do{
				TagNode ptr2 = ptr.firstChild;
				ptr.firstChild = new TagNode("b", ptr2, null);
				ptr = ptr.sibling;
			}
			while(ptr!=null);//end while
			
			
			
		}
		
		else if(prev.sibling!=null&& prev.sibling.tag.equals("table")){
			//System.out.println("Found target at prev.sibling: " + prev.sibling.tag);
			TagNode ptr = prev.sibling;
			ptr = ptr.firstChild;
			for(int i=1;i<row;i++){//go to correct row
				ptr = ptr.sibling;
				//System.out.println("row#: "+ i);
			}
			
			if(ptr==null){
				throw new IllegalArgumentException();
			}
			
			ptr = ptr.firstChild;//go to first column in row
			//System.out.println("this is the column were dealing with: "+ptr.firstChild.tag);
			
			do{
				TagNode ptr2 = ptr.firstChild;
				ptr.firstChild = new TagNode("b", ptr2, null);
				ptr = ptr.sibling;
			}
			while(ptr!=null);//end while
			
			
		}
		
		//System.out.println("prev= "+ prev.tag);
	
		
		boldRow( prev.sibling, row);
		boldRow( prev.firstChild, row);
		
		return;
		
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		if(tag.equals("b")){
			
			TagNode prev = root;//points to html
			removeTag(prev, "b");
		}else
			if(tag.equals("em")){
				TagNode prev = root;
				removeTag(prev, "em");
			}
			else 
				if(tag.equals("p")){
				TagNode prev = root;
				removeTag(prev, "p");
				
			}
				else
					if(tag.equals("ol")){
						TagNode prev = root;
						removeTag2(prev, "ol");
					}
					else 
						if(tag.equals("ul")){
							TagNode prev = root;
							removeTag2(prev, "ul");
						}
		
		
		
		
		
		
	}//end removeTag
	
	private static void removeTag(TagNode prev, String target){
		if(prev==null){
			return;
		}
		
		if(prev.firstChild!=null&&prev.firstChild.tag.equals(target)){
			//System.out.println("Found target at prev.firstchild: " + prev.firstChild.tag);
			TagNode ptr = prev.firstChild;
			if(ptr.sibling!=null){
				TagNode ptr2 = ptr.firstChild;
				while(ptr2.sibling!=null){
					System.out.println("while loop, where pointer2 is pointing: "+ptr2.tag);
					ptr2=ptr2.sibling;
				}
				ptr2.sibling = ptr.sibling;
				ptr.sibling=null;
				prev.firstChild = ptr.firstChild;
			}else{
				prev.firstChild = ptr.firstChild;
			}
			//ptr.firstChild.sibling = ptr.sibling;
			
			
		}
		else if(prev.sibling!=null&& prev.sibling.tag.equals(target)){
			//System.out.println("Found target at prev.sibling: " + prev.sibling.tag);
			TagNode ptr = prev.sibling;
			TagNode ptr2 = ptr.firstChild;
			if(ptr.sibling!=null){
				while(ptr2.sibling!=null){
					//System.out.println("in while loop sib");
					ptr2=ptr2.sibling;
				}
				ptr2.sibling = ptr.sibling;
				prev.sibling = ptr.firstChild;
				ptr.sibling = null;
				ptr.firstChild = null;
			}
			else{
				prev.sibling = ptr.firstChild;	
				ptr.firstChild = null;
			}
			
			//ptr.firstChild.sibling = ptr.sibling;
			
			
		}
		
		
		//System.out.println("prev= "+ prev.tag);
	
		
		removeTag( prev.sibling, target);
		removeTag( prev.firstChild, target);
		
		return;
		
	}//end private removeTag
	
	private static void removeTag2(TagNode prev, String target){
		if(prev==null){
			return;
		}
		
		if(prev.firstChild!=null&&prev.firstChild.tag.equals(target)){//if target below 
			//System.out.println("Found target at prev.firstchild: " + prev.firstChild.tag);
			TagNode ptr = prev.firstChild;
			if(ptr.sibling!=null){
				TagNode ptr2=ptr.sibling;
				TagNode ptr3=ptr.firstChild;
				//TagNode repTag = ptr.firstChild;
				while(ptr3.sibling!=null){
				
						ptr3.tag = "p";
					
					ptr3 = ptr3.sibling;
				}//while
				ptr3.tag = "p";
				
				ptr3.sibling = ptr2;
				prev.firstChild = ptr.firstChild;
				
				//System.out.println("up here");
				//replaceTag("li","p", repTag );
				
			}else{
				
				TagNode ptr2=ptr.firstChild;
				while(ptr2.sibling!=null){
					
					ptr2.tag = "p";
				
				ptr2 = ptr2.sibling;
			}//while
			ptr2.tag = "p";
			
				prev.firstChild = ptr.firstChild;
				
				
				//System.out.println("down here");
				//replaceTag("li","p",ptr3);
			}
			
			
		}//first if
		else if(prev.sibling!=null&& prev.sibling.tag.equals(target)){
			//System.out.println("Found target at prev.sibling: " + prev.sibling.tag);
			TagNode ptr = prev.sibling;
			
			
			ptr = ptr.firstChild;
			TagNode ptr2 = ptr;
			while(ptr2.sibling!=null){
				
				ptr2.tag = "p";
			
			ptr2 = ptr2.sibling;
		}//while
		ptr2.tag = "p";
			prev.sibling = ptr;
			
			//replaceTag("li","p", ptr);
			
			//prev.sibling = ptr.firstChild;
		
			
			
		}
		
		
		//System.out.println("prev= "+ prev.tag);
	
		
		removeTag2( prev.sibling, target);
		removeTag2( prev.firstChild, target);
		
		return;
		
	}
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		
		
		TagNode prev = root;
		tagTokenizer(word, tag, prev);
		tagTokenizer(word, tag, prev);
		addTag(word, tag, prev);	
		
	}//end add tag
	
	private static int stenOla(String str, String target){
	
		int currIndex = 0;
		int total = 0;
		String token = null;
		str = str.toLowerCase();
		
		//System.out.println("string at stenola macine: "+str);

		
		while(str.length()>=0){
			//System.out.println("this is the string length!!!!! "+str.length());
			//System.out.println("Remaining string::::::"+str+":::::");
			//System.out.println("Target length: "+target.length()+" String length: "+str.length());
			
			
			if(str.length()==target.length()||str.length()==target.length()+1){
				token = str;
			}else if(str.indexOf(' ')<0){
				//System.out.println("returning -1");
				 return -1;
			}
			else{
			token = str.substring(0, str.indexOf(' '));
			}
			//System.out.println("Last index: "+currIndex+" Index for first space after is: "+str.indexOf(' '));
			//System.out.println("this is your tolken from within the while loop. . . ."+token+". . . . . ");
			currIndex = str.indexOf(' ')+1;
			
			//System.out.println("Total index from stenOla: "+total);
			
					if(isWord(token, target)){
						
						return total;
					}
					
						
					total = total + currIndex;	
					
				
			str = str.substring(currIndex,  str.length());
			
			//System.out.println("from end of STENOLA while loop. . . . . . ");
			//System.out.println("curr index: "+currIndex);
			//System.out.println("string length: "+str.length());
			//System.out.println("Target length: "+target.length());
			//System.out.println("Str is:::::"+str);
			//System.out.println("Token is:"+token);
			//System.out.println("Target is::::"+target);
			if((target.length()==str.length()) && (isWord2(str, target))){// yet another fix
				return total;
			}
			if((target.length()+1==str.length()) && (isWord2(str, target))){// yet another fix
				return total;
			}
			if(str.indexOf(' ')<0 && !(isWord2(token, target))){// yet another fix
				return -1;
			}
			//System.out.println("this the index where the new word stats: "+currIndex);
			
			
		}//end while///
		
		
		return -1;
	}//end of the private *and* proprietary stenOla Method
	
//tokenizer**************************								++++++++++++++++++++++
	private static void tagTokenizer(String word, String tag, TagNode prev){
		//System.out.println("At tag token izer");
		//System.out.println("tag node recurs called");
		if(prev==null){
			return;
		}
		
		if(prev.firstChild!=null&&prev.firstChild.firstChild==null){
			//System.out.println("String: "+prev.firstChild.tag);
		}
		
		if(prev.sibling!=null&& prev.sibling.firstChild==null){
			//System.out.println("String:  "+prev.sibling.tag);
		}
	
		//make sure not null so no errors && make sure word matches && make sure target is text only, not tags && make sure not already tagged with b or em
		//String str = prev.firstChild.tag;
		//System.out.println("heres the tag "+str);
		
		if(prev.firstChild!=null &&  !(isWord((prev.firstChild.tag), word)) && (prev.firstChild.tag.toLowerCase()).indexOf(word)!= -1 && prev.firstChild.firstChild==null && !(prev.tag.equals(tag))){
		
			//System.out.println(prev.tag +" found target at prev.firstchild: " + prev.firstChild.tag);
			
			//System.out.println((prev.firstChild.tag.toLowerCase()).indexOf(word));
			
			//if first word in string, re-check and split in two. . .this first bunch of crap might be redundant
			if((prev.firstChild.tag.toLowerCase()).indexOf(word)==0 && stenOla((prev.firstChild.tag.toLowerCase()), word)>=0 ){
				//System.out.println("yep!");
				//System.out.println((prev.firstChild.tag).substring(0, word.length()+1));
				//if no nodes to right of this text
				if(prev.firstChild.sibling==null){
					String newWord = (prev.firstChild.tag).substring(0, word.length()+1);
					String remainingText;
					//System.out.println("Word is: "+newWord);
					if(newWord.charAt(word.length())==' '){
						newWord = newWord.substring(0, word.length());
						remainingText = (prev.firstChild.tag).substring((word.length()), ((prev.firstChild.tag).length()));
						//System.out.println("Truncated Word is: "+newWord);
					}else{
						remainingText = (prev.firstChild.tag).substring(word.length()+1, ((prev.firstChild.tag).length()));
					}
					TagNode ptr = prev.firstChild;
					ptr.tag = newWord;
					TagNode temp = new TagNode(remainingText, null, null);
					ptr.sibling = temp;
				}//end if no nodes to right
				else{//if there are nodes to right
					String newWord = (prev.firstChild.tag).substring(0, word.length()+1);
					String remainingText;
					//System.out.println("Word is: "+newWord);
					if(newWord.charAt(word.length())==' '){
						newWord = newWord.substring(0, word.length());
						remainingText = (prev.firstChild.tag).substring((word.length()), ((prev.firstChild.tag).length()));
						System.out.println("Truncated Word is: "+newWord);
					}else{
						remainingText = (prev.firstChild.tag).substring(word.length()+1, ((prev.firstChild.tag).length()));
					}
					TagNode ptr = prev.firstChild;
					TagNode ptr2= ptr.sibling;
					ptr.tag = newWord;
					TagNode temp = new TagNode(remainingText, null, null);
					temp.sibling = ptr2;
					ptr.sibling = temp;
				}
				
				
				
			}//end if word is first, split in two
			
			else{//word is in middle of text, split in three, or match made to string, but not word;also check for last word;if you get here, the first word is no match, so ignore it;find inxex of first blank space
				//System.out.println("word in middle or match");
				String a = (prev.firstChild.tag.toLowerCase());
				//System.out.println("And here is the string going to the stenOla: "+a);
				String origText = prev.firstChild.tag;
				int indexEnd = stenOla(a, word);
				//System.out.println("back after stenola in the childish part, index end is: "+indexEnd);//taking away 1 her . . .. . . . . .  .. .  
			if(indexEnd<0){///++++++++++++++++++++++++++++++++
				;
			}else{
				int i = indexEnd;
				int k=0;
				if(i+word.length()+1>a.length()){
				 k = i + word.length();
				 }
				else{
					 k = i + word.length()+1;
				}//+++++++++++++++++++++++++++++++++++++++
				//System.out.println("This is k: "+k);
				//System.out.println("String: "+ a + " and this is the index: "+ (i) +" and end of string to be tested is "+(k)+ ". . . substring to be tested::::::"+ a.substring(i, k)+"::::::");
				
				//test to make sure word is okay. . . 
				if(isWord(a.substring(i, k), word)){
					//System.out.println("qualifies!");
					if(prev.firstChild.sibling==null){//if no node to right
						String lastString=null;
						String middleString = origText.substring(i, k);
						String firstString = origText.substring(0, i);
						//System.out.println("From trouble area: sub first string:::::::"+firstString+"::::::::::");
						//System.out.println("From trouble area: sub middlestring:::::::"+middleString+"::::::::::");
						//System.out.println("middle string length is: "+middleString.length());
						//System.out.println("word  length is: "+ word.length());
						if(middleString.length()==word.length()){
							//System.out.println("Herre!++++++++++++++++++++++++++++++++++++++++++++++++");
							lastString = "";
						}else
							
						if(middleString.charAt(word.length())==' '){//<<<<+++++++++++++++++++problem here
							middleString = middleString.substring(0, word.length());
							lastString = origText.substring(k-1, (origText.length()));//changed to k-1 here
							//System.out.println("Truncated Word is: "+middleString);
						}else{
							lastString = origText.substring(k, (origText.length()));//not sure here. . . . if k should be +2 . . . p
						}
						//System.out.println("First::::::"+firstString+":::::::");
						//System.out.println("Middle:::::"+middleString+"::::::");
						
						//System.out.println("Last::::::"+lastString+"::::::");
						TagNode ptr = prev.firstChild;
						prev.firstChild.tag = firstString;
						TagNode temp1 = new TagNode(middleString, null, null);
						TagNode temp2 = new TagNode(lastString, null, null);
						ptr.sibling = temp1;
						temp1.sibling = temp2;
						if(lastString.equals("")){
							temp1.sibling=null;
						}
						
						
					}//end if no node to right
					else{//if there's a node to the right , , , , 
						
							String lastString=null;
							String middleString = origText.substring(i, k);
							String firstString = origText.substring(0, i);
							
							//System.out.println("middleString::::::::"+middleString+":::::::::::");
							//System.out.println("firstString::::::::"+firstString+":::::::::::");
							if(middleString.length()==word.length()){//+++++++++++++++++++++++++++++++++++++
								lastString = "";
							}else
								if(middleString.charAt(word.length())==' '){
								
								middleString = middleString.substring(0, word.length());
								lastString = origText.substring(k-1, (origText.length()));
								//System.out.println("Truncated Word is: "+middleString);
							}else{
								
								lastString = origText.substring(k, (origText.length()));
							}//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							//System.out.println("First....."+firstString+".....");
							//System.out.println("Middle....."+middleString+".....");
							
							//System.out.println("Last....."+lastString+".....");
							
							TagNode ptr = prev.firstChild;
							TagNode ptrEverythingElse = ptr.sibling;
							prev.firstChild.tag = firstString;
							TagNode temp1 = new TagNode(middleString, null, null);
							TagNode temp2 = new TagNode(lastString, null, null);
							ptr.sibling = temp1;
							temp1.sibling = temp2;
							temp2.sibling = ptrEverythingElse;
							if(lastString.equals("")){
								temp1.sibling=temp2.sibling;
							}
							
					}
					
				}else{//from test to make sure word okay
					;
					//System.out.println("Not qualified!");
				}
				
				
			}
			}//end of else word is in middle, split into 3
	
			
		}//end first if statement
		
		
		
		
		//(prev.firstChild!=null &&  !(isWord((prev.firstChild.tag), word)) && (prev.firstChild.tag.toLowerCase()).indexOf(word)!= -1 && prev.firstChild.firstChild==null && !(prev.tag.equals(tag)))
		
		
		//error check && make sure word matches && make sure target text only/. . . . . got rid of else here
		if(prev.sibling!=null&& !(isWord((prev.sibling.tag), word)) && (prev.sibling.tag.toLowerCase()).indexOf(word)!= -1 && prev.sibling.firstChild==null && !(prev.tag.equals(tag))){//PART TWOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
			
			if((prev.sibling.tag.toLowerCase()).indexOf(word)==0 && stenOla((prev.sibling.tag.toLowerCase()), word)>=0 ){
				//System.out.println("yep!");
				//System.out.println((prev.sibling.tag).substring(0, word.length()+1));
				//if no nodes to right of this text
				if(prev.sibling.sibling==null){
					String newWord = (prev.sibling.tag).substring(0, word.length()+1);
					String remainingText;
					//System.out.println("Word is: "+newWord);
					if(newWord.charAt(word.length())==' '){
						newWord = newWord.substring(0, word.length());
						remainingText = (prev.sibling.tag).substring((word.length()), ((prev.sibling.tag).length()));
						//System.out.println("Truncated Word is: "+newWord);
					}else{
						remainingText = (prev.sibling.tag).substring(word.length()+1, ((prev.sibling.tag).length()));
					}
					TagNode ptr = prev.sibling;
					ptr.tag = newWord;
					TagNode temp = new TagNode(remainingText, null, null);
					ptr.sibling = temp;
				}//end if no nodes to right
				else{//if there are nodes to right
					String newWord = (prev.sibling.tag).substring(0, word.length()+1);
					String remainingText;
					//System.out.println("Word is: "+newWord);
					if(newWord.charAt(word.length())==' '){
						newWord = newWord.substring(0, word.length());
						remainingText = (prev.sibling.tag).substring((word.length()), ((prev.sibling.tag).length()));
						//System.out.println("Truncated Word is: "+newWord);
					}else{
						remainingText = (prev.sibling.tag).substring(word.length()+1, ((prev.sibling.tag).length()));
					}
					TagNode ptr = prev.sibling;
					TagNode ptr2= ptr.sibling;
					ptr.tag = newWord;
					TagNode temp = new TagNode(remainingText, null, null);
					temp.sibling = ptr2;
					ptr.sibling = temp;
				}
				
				
				
			}//end if word is first, split in two
			
			else{//word is in middle of text, split in three, or match made to string, but not word;also check for last word;if you get here, the first word is no match, so ignore it;find inxex of first blank space
				//System.out.println("word in middle or match");
				String a = (prev.sibling.tag.toLowerCase());
				//System.out.println("And here is the string going to the stenOla: "+a);
				String origText = prev.sibling.tag;
				int indexEnd = stenOla(a, word);
				//System.out.println("back after stenola at the sibby part, indexEnd is: "+indexEnd);//taking away 1 her . . .. . . . . .  .. .  
			if(indexEnd<0){//++++++++
				;
			}else{
				int i = indexEnd;
				int k = 0;
				if(i+word.length()+1>a.length()){
					k = i+word.length();
				}
				else{
					k = i+word.length()+1;
				}//+++++++++++++++
				//System.out.println("String: "+ a + " and this is the index: "+ (i) +" and end of string to be tested is "+(k)+ ". . . substring to be tested::::::"+ a.substring(i, k)+"::::::");
				
				//test to make sure word is okay. . . 
				if(isWord(a.substring(i, k), word)){
					//System.out.println("qualifies!");
					if(prev.sibling.sibling==null){//if no node to right
						String lastString=null;
						String middleString = origText.substring(i, k);
						String firstString = origText.substring(0, i);
						//no extra statement here
						if(middleString.charAt(word.length())==' '){
							middleString = middleString.substring(0, word.length());
							lastString = origText.substring(k-1, (origText.length()));//changed to k-1 here
							//System.out.println("Truncated Word is: "+middleString);
						}else{
							lastString = origText.substring(k, (origText.length()));//not sure here. . . . if k should be +2 . . . p
						}
						//System.out.println("First::::::"+firstString+":::::::");
						//System.out.println("Middle:::::"+middleString+"::::::");
						
						//System.out.println("Last::::::"+lastString+"::::::");
						TagNode ptr = prev.sibling;
						prev.sibling.tag = firstString;
						TagNode temp1 = new TagNode(middleString, null, null);
						TagNode temp2 = new TagNode(lastString, null, null);
						ptr.sibling = temp1;
						temp1.sibling = temp2;
						if(lastString.equals("")){
							temp1.sibling=null;
						}
						
						
					}//end if no node to right
					else{//if there's a node to the right , , , , 
						
							String lastString=null;
							String middleString = origText.substring(i, k);
							String firstString = origText.substring(0, i);
							if(middleString.length()==word.length()){
								lastString = "";
							}else
							
							if(middleString.charAt(word.length())==' '){
								middleString = middleString.substring(0, word.length());
								lastString = origText.substring(k-1, (origText.length()));
								//System.out.println("Truncated Word is: "+middleString);
							}else{
								lastString = origText.substring(k, (origText.length()));
							}
							//System.out.println("First....."+firstString+".....");
							//System.out.println("Middle....."+middleString+".....");
							
							//System.out.println("Last....."+lastString+".....");
							
							TagNode ptr = prev.sibling;
							TagNode ptrEverythingElse = ptr.sibling;
							prev.sibling.tag = firstString;
							TagNode temp1 = new TagNode(middleString, null, null);
							TagNode temp2 = new TagNode(lastString, null, null);
							ptr.sibling = temp1;
							temp1.sibling = temp2;
							temp2.sibling = ptrEverythingElse;
							if(lastString.equals("")){
								temp1.sibling=temp2.sibling;
							}
							
					}
					
				}else{//from test to make sure word okay
					;
					//System.out.println("Not qualified!");
				}
				
				
			}
			}//end of else word is in middle, split into 3
		
		
		
		
		}//end else if statement
		
		
		//System.out.println("prev= "+ prev.tag);
	
		tagTokenizer(word, tag, prev.firstChild);
		tagTokenizer(word, tag, prev.sibling);
		
		
		//return;
		
		//return;
	}//end tagTokenizer************
	
	private static void addTag(String word, String tag, TagNode prev){
		//System.out.println("tag node recurs called");
		if(prev==null){
			return;
		}
		
		if(prev.firstChild!=null&&prev.firstChild.firstChild==null){
			;
			//System.out.println("prev.firstChild pointing to "+prev.firstChild.tag);
		}
		
		if(prev.sibling!=null&& prev.sibling.firstChild==null){
			;
			//System.out.println("prev.sibling pointing to "+prev.sibling.tag);
		}
	
		//make sure not null so no errors && make sure word matches && make sure target is text only, not tags && make sure not already tagged with b or em
		if(prev.firstChild!=null && isWord((prev.firstChild.tag), word)&&prev.firstChild.firstChild==null && !(prev.tag.equals(tag))){
			//System.out.println("Tag at prev is: " + prev.tag);
			//System.out.println("Found target at prev.firstchild: " + prev.firstChild.tag);
			if(prev.firstChild.sibling==null){
				TagNode ptr = prev.firstChild;
				TagNode temp = new TagNode(tag, null, null);
				prev.firstChild = temp;
				temp.firstChild = ptr;
				//prev = temp;
			}else{
				TagNode ptr = prev.firstChild;//points directly at targeted node
				//System.out.println("Prev: "+prev.tag+" is Poining ptr at: " + ptr.tag);
				TagNode temp = new TagNode(tag, null, null);
				//System.out.println("temp Tag created with this as tag: " + temp.tag);
				prev.firstChild = temp;
				temp.firstChild = ptr;
				temp.sibling = ptr.sibling;
				ptr.sibling = null;
				//prev=temp;
			}
	
			
		}
		//error check && make sure word matches && make sure target text only
		else if(prev.sibling!=null&& isWord((prev.sibling.tag), word)&&prev.sibling.firstChild==null){
			//System.out.println(prev.tag +" found target at prev.sibling: " + prev.sibling.tag);
			//if prev is on an b or em tag
			if(prev.tag.equals(tag)){
				//if nothing is past target text.  .. 
				if(prev.sibling.sibling==null){
					TagNode ptr = prev.sibling;
					TagNode temp = new TagNode(tag, null, null);
					temp.firstChild = ptr;
					prev.sibling = temp;
				}
				else{
					TagNode ptr = prev.sibling;
					TagNode ptr2 = ptr.sibling;
					
					TagNode temp = new TagNode(tag, null, null);
					temp.firstChild = ptr;
					prev.sibling = temp;
					temp.sibling = ptr2;
					ptr.sibling = null;
						//prev = temp;
				}
			
			//if not a tag
			}else if(prev.firstChild==null){
				if(prev.sibling.sibling==null){
					TagNode ptr = prev.sibling;
					TagNode temp = new TagNode(tag, null, null);
					temp.firstChild = ptr;
					prev.sibling = temp;
				}
				else{
					TagNode ptr = prev.sibling;
					TagNode ptr2 = ptr.sibling;
					
					TagNode temp = new TagNode(tag, null, null);
					temp.firstChild = ptr;
					prev.sibling = temp;
					temp.sibling = ptr2;
					ptr.sibling = null;
						//prev = temp;
				}
			}
			
		}
		
		
		//System.out.println("prev= "+ prev.tag);
	
		addTag(word, tag, prev.firstChild);
		addTag(word, tag, prev.sibling);
		
		
		//return;
		
	}//end private addTag
	
	private static boolean isWord(String text, String word){
		text = text.toLowerCase();
		word = word.toLowerCase();
		//System.out.println("Text is "+text+" and word is "+word);
		if(text.equals(word)||text.equalsIgnoreCase(word+" ")||text.equals(word+"!")||text.equals(word+"?")||text.equals(word+".")||text.equals(word+";")||text.equals(word+":")){
			return true;
		}else{
		
		return false;
		}
	}
	
	private static boolean isWord2(String text, String word){
		text = text.toLowerCase();
		word = word.toLowerCase();
		
		if(text.equals(word)||text.equals(word+" ")||text.equals(word+"! ")||text.equals(word+"? ")||text.equals(word+". ")||text.equals(word+"; ")||text.equals(word+": ")||
				text.equals(" " + word +" ")||text.equals(" " +word+"! ")||text.equals(" "+word+"? ")||text.equals(" "+word+". ")||text.equals(" "+word+"; ")||text.equals(" "+word+": ")
				||text.equals(" " + word)
				||text.substring(0,  text.length()-1).equals(word+" ")||text.substring(0,  text.length()-1).equals(" "+word+" ")
				||text.equals(word+"!")||text.equals(word+"?")||text.equals(word+".")||text.equals(word+";")||text.equals(word+":")){
			return true;
		}else{
		
		return false;
		}
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
}
