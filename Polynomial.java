package poly;

import java.io.*;
import java.util.StringTokenizer;

/**
 * This class implements a term of a polynomial.Final.
 * 
 * @author runb-cs112
 *
 */
class Term {
	/**
	 * Coefficient of term.
	 */
	public float coeff;
	
	/**
	 * Degree of term.
	 */
	public int degree;
	
	/**
	 * Initializes an instance with given coefficient and degree.
	 * 
	 * @param coeff Coefficient
	 * @param degree Degree
	 */
	public Term(float coeff, int degree) {
		this.coeff = coeff;
		this.degree = degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		return other != null &&
		other instanceof Term &&
		coeff == ((Term)other).coeff &&
		degree == ((Term)other).degree;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (degree == 0) {
			return coeff + "";
		} else if (degree == 1) {
			return coeff + "x";
		} else {
			return coeff + "x^" + degree;
		}
	}
}

/**
 * This class implements a linked list node that contains a Term instance.
 * 
 * @author runb-cs112
 *
 */
class Node {
	
	/**
	 * Term instance. 
	 */
	Term term;
	
	/**
	 * Next node in linked list. 
	 */
	Node next;
	
	/**
	 * Initializes this node with a term with given coefficient and degree,
	 * pointing to the given next node.
	 * 
	 * @param coeff Coefficient of term
	 * @param degree Degree of term
	 * @param next Next node
	 */
	public Node(float coeff, int degree, Node next) {
		term = new Term(coeff, degree);
		this.next = next;
	}
}

/**
 * This class implements a polynomial.
 * 
 * @author runb-cs112
 *
 */
public class Polynomial {
	
	/**
	 * Pointer to the front of the linked list that stores the polynomial. 
	 */ 
	Node poly;
	
	/** 
	 * Initializes this polynomial to empty, i.e. there are no terms.
	 *
	 */
	public Polynomial() {
		poly = null;
	}
	
	/**
	 * Reads a polynomial from an input stream (file or keyboard). The storage format
	 * of the polynomial is:
	 * <pre>
	 *     <coeff> <degree>
	 *     <coeff> <degree>
	 *     ...
	 *     <coeff> <degree>
	 * </pre>
	 * with the guarantee that degrees will be in descending order. For example:
	 * <pre>
	 *      4 5
	 *     -2 3
	 *      2 1
	 *      3 0
	 * </pre>
	 * which represents the polynomial:
	 * <pre>
	 *      4*x^5 - 2*x^3 + 2*x + 3 
	 * </pre>
	 * 
	 * @param br BufferedReader from which a polynomial is to be read
	 * @throws IOException If there is any input error in reading the polynomial
	 */
	public Polynomial(BufferedReader br) throws IOException {
		String line;
		StringTokenizer tokenizer;
		float coeff;
		int degree;
		
		poly = null;
		
		while ((line = br.readLine()) != null) {
			tokenizer = new StringTokenizer(line);
			coeff = Float.parseFloat(tokenizer.nextToken());
			degree = Integer.parseInt(tokenizer.nextToken());
			poly = new Node(coeff, degree, poly);
		}
	}
	
	
	/**
	 * Returns the polynomial obtained by adding the given polynomial p
	 * to this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial to be added
	 * @return A new polynomial which is the sum of this polynomial and p.
	 */
	public Polynomial add(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		Node ptrA = poly;
		Node ptrB = p.poly;
		int highDeg = 0;
		Polynomial polyC = new Polynomial();
		
		while(ptrA!=null){
			polyC.addToFront(ptrA.term.coeff, ptrA.term.degree, poly);
			highDeg = ptrA.term.degree;
			ptrA = ptrA.next;
		}//loads list A into C and keeps highest degree
		
		while(ptrB!=null){
			polyC.addToFront(ptrB.term.coeff, ptrB.term.degree, poly);
			if(ptrB.term.degree>highDeg){
				highDeg = ptrB.term.degree;
			}
			ptrB = ptrB.next;
		}//loads list B into C and keeps highest degree
		
		//this part adds all common terms in list C and puts them into a final list
		Node ptrC = polyC.poly;//new pointer for list "C"
		Polynomial finalList = new Polynomial();//create final list to return
		float totalCoeff = 0;//used to add up all coeff
		
		for(int i = highDeg; i>=0;i--){
			totalCoeff = 0;
			ptrC = polyC.poly;
			while(ptrC!=null){
				if(ptrC.term.degree==i){
					totalCoeff = totalCoeff+ptrC.term.coeff;
				}
				ptrC = ptrC.next;
			}//end while loop
			
			if(totalCoeff == 0){
				continue;
			}
			//System.out.println(totalCoeff + "x^"+i);
			finalList.addToFront(totalCoeff, i, poly);
			
		}//end for loop
		
		
		
		return finalList;
	}
	
	
	//adds new node to front of linked list
	private void addToFront(float coeff, int degree, Node next){
		poly = new Node(coeff, degree, poly);
	}
	
	/**
	 * Returns the polynomial obtained by multiplying the given polynomial p
	 * with this polynomial - DOES NOT change this polynomial
	 * 
	 * @param p Polynomial with which this polynomial is to be multiplied
	 * @return A new polynomial which is the product of this polynomial and p.
	 */
	public Polynomial multiply(Polynomial p) {
		/** COMPLETE THIS METHOD **/
		Node ptrA = poly;//pointer to front of "A" list
		Node ptrB = p.poly;//pointer to front of "B" list
		Polynomial polyC = new Polynomial();//stores new results
		int highDeg = 0;
		
		while(ptrA!=null){
			while(ptrB!=null){
				polyC.addToFront(ptrA.term.coeff*ptrB.term.coeff, ptrA.term.degree +  ptrB.term.degree, poly);
				highDeg =  ptrA.term.degree +  ptrB.term.degree;
				ptrB = ptrB.next;

			}//while B
			
			ptrA=ptrA.next;
			ptrB=p.poly;
			
		}//while A
		
		//System.out.println(highDeg);
		
		//this part adds all common terms in list C and puts them into a final list
		Node ptrC = polyC.poly;//new pointer for list "C"
		Polynomial finalList = new Polynomial();//create final list to return
		float totalCoeff = 0;//used to add up all coeff
		
		for(int i = highDeg; i>=0;i--){
			totalCoeff = 0;
			ptrC = polyC.poly;
			while(ptrC!=null){
				if(ptrC.term.degree==i){
					totalCoeff = totalCoeff+ptrC.term.coeff;
				}
				ptrC = ptrC.next;
			}//end while loop
			
			if(totalCoeff == 0){
				continue;
			}
			
			finalList.addToFront(totalCoeff, i, poly);
			
		}//end for loop
		
		
		
		return finalList;
	}
	
	/**
	 * Evaluates this polynomial at the given value of x
	 * 
	 * @param x Value at which this polynomial is to be evaluated
	 * @return Value of this polynomial at x
	 */
	public float evaluate(float x) {
		/** COMPLETE THIS METHOD **/
		if(poly==null){
			return 0;
		}
		
		float c;
		int d;
		float total = 0;
		Node ptr = poly;
	
		while(true){
		c = ptr.term.coeff;
		d = ptr.term.degree;
		total = (float) (total + c*Math.pow(x,(float)d));
		
		
		ptr = ptr.next;
		if(ptr.next==null){
			c = ptr.term.coeff;
			d = ptr.term.degree;
			total = (float) (total + c*Math.pow(x,(float)d));


			break;
			}
		}
		
		return total;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String retval;
		
		if (poly == null) {
			return "0";
		} else {
			retval = poly.term.toString();
			for (Node current = poly.next ;
			current != null ;
			current = current.next) {
				retval = current.term.toString() + " + " + retval;
			}
			return retval;
		}
	}
}
