package edu.iastate.cs228.hw3;

import java.awt.List;
import java.util.AbstractSequentialList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;




/**
 * @author Nicholas Kirschbaum
 * Implementation of the list interface based on linked nodes
 * that store multiple items per node.  Rules for adding and removing
 * elements ensure that each node (except possibly the last one)
 * is at least half full.
 */
public class StoutList<E extends Comparable<? super E>> extends AbstractSequentialList<E>
{
  /**
   * Default number of elements that may be stored in each node.
   */
  private static final int DEFAULT_NODESIZE = 4;
  
  /**
   * Number of elements that can be stored in each node.
   */
  private final int nodeSize;
  
  /**
   * Dummy node for head.  It should be private but set to public here only  
   * for grading purpose.  In practice, you should always make the head of a 
   * linked list a private instance variable.  
   */
  public Node head;
  
  /**
   * Dummy node for tail.
   */
  private Node tail;
  
  /**
   * Number of elements in the list.
   */
  private int size;
  
  /**
   * Constructs an empty list with the default node size.
   */
  public StoutList()
  {
    this(DEFAULT_NODESIZE);
  }

  /**
   * Constructs an empty list with the given node size.
   * @param nodeSize number of elements that may be stored in each node, must be 
   *   an even number
   */
  public StoutList(int nodeSize)
  {
    if (nodeSize <= 0 || nodeSize % 2 != 0) throw new IllegalArgumentException();
    
    // dummy nodes
    head = new Node();
    tail = new Node();
    head.next = tail;
    tail.previous = head;
    this.nodeSize = nodeSize;
  }
  
  /**
   * Constructor for grading only.  Fully implemented. 
   * @param head
   * @param tail
   * @param nodeSize
   * @param size
   */
  public StoutList(Node head, Node tail, int nodeSize, int size)
  {
	  this.head = head; 
	  this.tail = tail; 
	  this.nodeSize = nodeSize; 
	  this.size = size; 
  }
  /**
	 * Sets new node to the proper posistion
	 * @param current inputs a node to modify
	 * @param newnode inputs a node to modify
	 */
  private void link(StoutList<E>.Node current, StoutList<E>.Node newnode) {
	  newnode.previous = current;
	  newnode.next = current.next;
	  current.next.previous = newnode;
	  current.next = newnode;
  }
  /**
	 * Detaches node that is no longer being used
	 * * @param current inputs a node to modify
	 */
  private void unlink(StoutList<E>.Node current) {
	  current.previous.next = current.next;
	  current.next.previous = current.previous;
  }
  /**
	 * returns size
	 */

  @Override
  public int size()
  {
    // TODO Auto-generated method stub
    return size;
  }
  /**
	 * adds item to the end 
	 * @param item is the item you want the input
	 */
  
  @Override
  public boolean add(E item)
  {
	  //as i understand this is adding or removing a node
    // TODO Auto-generated method stub
	 
	  if(item == null) throw new NullPointerException();
	  if(size == 0) {
		  Node temp = new Node();
		    //link(tail.previous, temp);
		  	//temp.previous = tail.previous;
		  link(tail.previous, temp);
		  	temp.addItem(item);
		  	//size++;
		  	size+=nodeSize;
		    return true;
	  }
	  //else if(nodeSize == 4){
	  else {
		  Node temp = head.next;
		    //link(tail.previous, temp);
		  	for(int i = 0; i <(size/nodeSize); i++) {
		  		if(temp.data[i] == item) {
		  			return false;
		  		}
		  		if(temp.count<nodeSize) {
		  			
		  			temp.addItem(item);
		  			
				  	
				    //++size;
				    return true;
		  		}
		  		temp = temp.next;
		  		
		  	}
		  	Node node = new Node();
		    //link(tail.previous, temp);
		  	link(tail.previous, node);
		  	node.addItem(item);
		    //++size;
		  	size+=nodeSize;
		    return true;
		  
	  }
	  //return false;
	  
  }
  /**
	 * adds item to the pos it is reequested to
	 * @param pos
	 * @param item
	 */
  @Override
  public void add(int pos, E item)
  {
    // TODO Auto-generated method stub
	  if (pos < 0 || pos > size) throw new IndexOutOfBoundsException("" + pos);
	  //if position is empty
	  
	  Node current = head.next;
	  //Node current = new Node();
	    int count = 1;
	   
	    while (pos>nodeSize*count)
	    {
	      current = current.next;
	      
	      ++count;
	    }
	    if((pos % nodeSize == 0) &&(pos != 0)) {
	    	current = current.next;
	    }
	    
	    
	    int isnull = (pos - nodeSize*(count - 1));
	    
	    if((pos % nodeSize == 0) &&(pos != 0)) {
	    	isnull = isnull - 4;
	    }
	    
	    
	  if(size == 0) {
		  //size = 0 so create a new node and then plug in new varaible
		  Node temp = new Node();
		    //link(tail.previous, temp);
		  
		  	temp.addItem(pos, item);
		    size+=nodeSize;
		    
	  }
	  else if (current.data[isnull] == null) {
		  //position is empty
		  
		  	current.addItem(isnull, item);
		    
	  }
	  else {
		  int totalempty = 0;
		  for(int i = 0; i <nodeSize; i++) {
			  
			  if(current.data[i] == null){
				  totalempty++;
			  }
		  }
		  if(totalempty > 0) {
			  //is their space left in that node
			  for (int i = (nodeSize - totalempty - 1); (i + 1) < (nodeSize - 1); i++) {
				  current.data[i+1] = current.data[i];
				  }
			  
			  	current.addItem(isnull, item);
			    
		  }
		
		  else {
			  // if no 
			  
			 Node temp = new Node();
			 link(tail.previous, temp);
			 size+=nodeSize;
			
			 int totalneededtoshiftover = nodeSize - isnull;
			 //temp.data[i] = temp.previous.data[i];
			 for(int i = 0; i<(size/nodeSize) - 1; i++) {
				 
				 //this for loop shifts through nodes
				 int notnull = 0;
				 int shiftpre = 0;
				 
				 for(int k = 0; k<nodeSize; k++) {
						//this for loop determines how many data points are not null
					 if(temp.data[k] == null) {
						 notnull++;
					 }
					 if(temp.previous.data[k] != null) {
						 shiftpre++;
					 }
						 
				 }
				 //if(i == 0) {
				 temp.data[0] = temp.previous.data[shiftpre - 1];
				 //}
				 for(int j = 0; j<nodeSize; j++) {
					//this for loop shifts through data
					 if(j < shiftpre - 1) {
						 temp.previous.data[j + 1] = temp.previous.data[j];
					 }
					 
					 
				 }
				 
				 
				 temp = temp.previous;
			 }
			if(pos % nodeSize == 0) {
				 current.count = 0;
				 current.addItem(0, item);
			 }
			else if(pos > nodeSize * count - 1) {
				  current.count = 0;
				  current.next.addItem(totalneededtoshiftover, item);
			  }
			 else if(current.count == nodeSize) {
				 current.count = 0;
				 current.addItem(pos - nodeSize * (count - 1), item);
			 }
			 
			 
			  
		  }
	  }
	  
	 
	   // current.addItem(pos, item);
	    
	 
	  
	
	  	
	    
	  
  }
  /**
	 * Deletes element and shifts thing to proper position
	 * @param pos
	 */
  @Override
  public E remove(int pos)
  {
    // TODO Auto-generated method stub
	  //if pos is at the end of the list
	  
	  Node current = head.next;
	  int count = 1;
	  while (pos>nodeSize*count)
	    {
	      current = current.next;
	      
	      ++count;
	    }
	  int isnull = pos - nodeSize*(count - 1);
	  current.removeItem(isnull);
	  for(int j = 0; j < nodeSize; j++) {
		  if((current.data[j] == null)) {
			  current.data[j] = current.next.data[0];
			  current = current.next;
			  break;
		  }
		 
	  }
	  if(current != tail.previous) {
	  
		  for(int i = 0; i < size/nodeSize - count; i++) {
			  for(int j = 0; j < nodeSize; j++) {
				  if(current.data[j] != null) {
					  if(j == nodeSize - 1) {
						  if(current != tail.previous) {
							  current.data[j] = current.next.data[0];
							 
						  }
						  
						  break;
					  }
					  current.data[j] = current.data[j + 1];
					  
				  }
				 
			  }
			  current = current.next;
			  
		  }
	  }
	  if(current.previous.count == 0) {
	  unlink(current.previous);
	  }
	  /**
	  if(current == tail.previous) {
		  if(current.count == 0) {
			  unlink(current);
		  }
		 
	  }*/
	  if(current.count == 1) {
		  unlink(current);
		  
	  }
	   
	  return null;

  }

  /**
   * Sort all elements in the stout list in the NON-DECREASING order. You may do the following. 
   * Traverse the list and copy its elements into an array, deleting every visited node along 
   * the way.  Then, sort the array by calling the insertionSort() method.  (Note that sorting 
   * efficiency is not a concern for this project.)  Finally, copy all elements from the array 
   * back to the stout list, creating new nodes for storage. After sorting, all nodes but 
   * (possibly) the last one must be full of elements.  
   *  
   * Comparator<E> must have been implemented for calling insertionSort().    
   */
  public void sort()
  {
	  // TODO 
	  E array[] = null;
	  	int next = 0;
	  	Node shift = head.next;
		for(int i = 0; i < (size / nodeSize) - 1; i++) {
			
			//declares which node you are searching through
			
			
			for(int j = 0; j < nodeSize; j++) {
				
				if(shift.data[j] != null) {
					array[next] = shift.data[j];
					next++;
				}
			}
			shift = shift.next;
		}
		insertionSort(array, new Comparator<E>() {
			  @Override
			  public int compare(E a, E b) {
				  return a.compareTo(b);
			  }
		  });
  }
  
  /**
   * Sort all elements in the stout list in the NON-INCREASING order. Call the bubbleSort()
   * method.  After sorting, all but (possibly) the last nodes must be filled with elements.  
   *  
   * Comparable<? super E> must be implemented for calling bubbleSort(). 
   */
  public void sortReverse() 
  {
	  // TODO 
	  E array[] = null;
	  	int next = 0;
	  	Node shift = head.next;
		for(int i = 0; i < (size / nodeSize) ; i++) {
			
			//declares which node you are searching through
			
			
			for(int j = 0; j < nodeSize; j++) {
				
				if(shift.data[j] != null) {
					array[next] = shift.data[j];
					next++;
				}
			}
			shift = shift.next;
		}
		bubbleSort(array);
  }
  
  @Override
  public Iterator<E> iterator()
  {
    // TODO Auto-generated method stub
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator()
  {
    // TODO Auto-generated method stub
    return new StoutListIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index)
  {
    // TODO Auto-generated method stub
    return new StoutListIterator(index);
  }
  @Override
	public E get(int pos){
	  	return listIterator(pos).next();
	}
  
  /**
   * Returns a string representation of this list showing
   * the internal structure of the nodes.
   */
  public String toStringInternal()
  {
    return toStringInternal(null);
  }

  /**
   * Returns a string representation of this list showing the internal
   * structure of the nodes and the position of the iterator.
   *
   * @param iter
   *            an iterator for this list
   */
  public String toStringInternal(ListIterator<E> iter) 
  {
      int count = 0;
      int position = -1;
      if (iter != null) {
          position = iter.nextIndex();
      }

      StringBuilder sb = new StringBuilder();
      sb.append('[');
      Node current = head.next;
      while (current != tail) {
          sb.append('(');
          E data = current.data[0];
          if (data == null) {
              sb.append("-");
          } else {
              if (position == count) {
                  sb.append("| ");
                  position = -1;
              }
              sb.append(data.toString());
              ++count;
          }

          for (int i = 1; i < nodeSize; ++i) {
             sb.append(", ");
              data = current.data[i];
              if (data == null) {
                  sb.append("-");
              } else {
                  if (position == count) {
                      sb.append("| ");
                      position = -1;
                  }
                  sb.append(data.toString());
                  ++count;

                  // iterator at end
                  if (position == size && count == size) {
                      sb.append(" |");
                      position = -1;
                  }
             }
          }
          sb.append(')');
          current = current.next;
          if (current != tail)
              sb.append(", ");
      }
      sb.append("]");
      return sb.toString();
  }


  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private class NodeInfo{
	  public Node node;
	  public int offset;
	  public NodeInfo(Node node, int offset) {
		  this.node = node;
		  this.offset = offset;
	  }
	  
  }
  
  
  /**
   * Node type for this list.  Each node holds a maximum
   * of nodeSize elements in an array.  Empty slots
   * are null.
   */
  private class Node
  {
    /**
     * Array of actual data elements.
     */
    // Unchecked warning unavoidable.
    public E[] data = (E[]) new Comparable[nodeSize];
    
    /**
     * Link to next node.
     */
    public Node next;
    
    /**
     * Link to previous node;
     */
    public Node previous;
    
    /**
     * Index of the next available offset in this node, also 
     * equal to the number of elements in this node.
     */
    public int count;
    public Node() {
		// TODO Auto-generated constructor stub
	}

    

	/**
     * Adds an item to this node at the first available offset.
     * Precondition: count < nodeSize
     * @param item element to be added
     */
    void addItem(E item)
    {
      if (count >= nodeSize)
      {
        return;
      }
      data[count++] = item;
      //useful for debugging
           // System.out.println("Added " + item.toString() + " at index " + count + " to node "  + Arrays.toString(data));
    }
  
    /**
     * Adds an item to this node at the indicated offset, shifting
     * elements to the right as necessary.
     * 
     * Precondition: count < nodeSize
     * @param offset array index at which to put the new element
     * @param item element to be added
     */
    void addItem(int offset, E item)
    {
      if (count >= nodeSize)
      {
    	  return;
    	  
      }
      for (int i = count - 1; i >= offset; --i)
      {
        data[i + 1] = data[i];
      }
      ++count;
      data[offset] = item;
      //useful for debugging 
      //System.out.println("Added " + item.toString() + " at index " + offset + " to node: "  + Arrays.toString(data));
    }

    /**
     * Deletes an element from this node at the indicated offset, 
     * shifting elements left as necessary.
     * Precondition: 0 <= offset < count
     * @param offset
     */
    void removeItem(int offset)
    {
      E item = data[offset];
      for (int i = offset + 1; i < nodeSize; ++i)
      {
        data[i - 1] = data[i];
      }
      data[count - 1] = null;
      --count;
    }    
  }
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private class StoutListIterator implements ListIterator<E>
  {
	// constants you possibly use ...   
	  
	// instance variables ... 
	  private static final int BEHIND = -1;
	    private static final int AHEAD = 1;
	    private static final int NONE = 0;
	    
	    private Node cursor;
	    private int index;
	    private int direction;
	  
    /**
     * Default constructor 
     */
    public StoutListIterator()
    {
    	// TODO 
    	this(0);
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    
    public StoutListIterator(int pos)
    {
    	// TODO 
    	if (pos < 0 || pos > size) throw new IndexOutOfBoundsException("" + pos);
        
        cursor = findNodeByIndex(pos);
        index = pos;
        direction = NONE;
        
    }
    /**
	 * returns a node whose pos it its in
	 */
    private Node findNodeByIndex(int pos)
    {
      if (pos == -1) return head;
      if (pos == size) return tail;
      
      // inv: position of current is count
      Node current = head.next;
      int count = 0;
      while (count < pos)
      {
        current = current.next;
        ++count;
      }
      
      return current;
    }
    /**
	 * checks to see if their is a next and sets true or false
	 */
    @Override
    public boolean hasNext()
    {
    	// TODO 
    	if(index < size) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    /**
	 * gets next E
	 */
    @Override
    public E next()
    {
    	// TODO 
    	if(!hasNext()) {
    		if (findNodeByIndex(index +1) == null) throw new NoSuchElementException();
    		
    	}
    	
        
        E ret = cursor.data[index + 1];
        cursor = cursor.next;
        ++index;
        direction = BEHIND;
        return ret;
    }
    /**
	 * removes node
	 */
    @Override
    public void remove()
    {
    	// TODO 
    	if (direction == NONE)
        {
          throw new IllegalStateException();
        }
        
        else
        {
          if (direction == AHEAD)
          {
            // remove node at cursor and move to next node
            Node n = cursor.next;
            //unlink(cursor);  
            cursor.previous.next = cursor.next;
      	  	cursor.next.previous = cursor.previous;
            cursor = n;
          }
          else
          {
            // remove node behind cursor and adjust index
            //unlink(cursor.previous);
        	  cursor.previous.previous.next = cursor.previous.next;
        	  cursor.previous.next.previous = cursor.previous.previous;
            --index;
          }
        } 
    }

	@Override
	public boolean hasPrevious() {
		// TODO Auto-generated method stub
		if(index > 0) {
    		return true;
    	}
    	else {
    		return false;
    	}
	}
	/**
	 * checks if 
	 */
	@Override
	public E previous() {
		if(!hasNext()) {
    		if (findNodeByIndex(index -1) == null) throw new NoSuchElementException();
    		index = 0;
    	}
    	
        
        E ret = cursor.data[index - 1];
        cursor = cursor.next;
        --index;
        direction = AHEAD;
        return ret;
	}
	/**
	 * sets index to the next index
	 */
	@Override
	public int nextIndex() {
		// TODO Auto-generated method stub
		return index +1;
	}
	/**
	 * sets index to the previous index
	 */
	@Override
	public int previousIndex() {
		// TODO Auto-generated method stub
		return index - 1;
	}
	/**
	 * sets cursor
	 */
	@Override
	public void set(E e) {
		// TODO Auto-generated method stub
		if (direction == NONE)
	      {
	        throw new IllegalStateException();
	      }
	      
	      if (direction == AHEAD)
	      {
	        cursor.data[index] = e;
	      }
	      else
	      {
	        cursor.previous.data[index] = e;
	      }
		
	}
	/**
	 * It does something
	 */

	@Override
	public void add(E e) {
		// TODO Auto-generated method stub
		
		
	}
    
    // Other methods you may want to add or override that could possibly facilitate 
    // other operations, for instance, addition, access to the previous element, etc.
    // 
    // ...
    // 
  }
  

  /**
   * Sort an array arr[] using the insertion sort algorithm in the NON-DECREASING order. 
   * @param arr   array storing elements from the list 
   * @param comp  comparator used in sorting 
   */
  
  
  
  private void insertionSort(E[] arr, Comparator<? super E> comp)
  {
	  // TODO
	  int i = 0;
		// first is first unsorted index
		for(int first = 1; first < arr.length; first++) {
			E elementoToSort = arr[i];
			
			
			i = first - 1;
			while (i > -1 && comp.compare(arr[i], elementoToSort) > 0) {
			
				//swap(i, i + 1);
				arr[i+1] = arr[i];
				i--;
			}
			
			arr[i + 1] = elementoToSort;
		}
  }
  
  /**
   * Sort arr[] using the bubble sort algorithm in the NON-INCREASING order. For a 
   * description of bubble sort please refer to Section 6.1 in the project description. 
   * You must use the compareTo() method from an implementation of the Comparable 
   * interface by the class E or ? super E. 
   * @param arr  array holding elements from the list
   */
  private void bubbleSort(E[] arr)
  {
	  // TODO
	  int n = arr.length;
	  int i, j; 
	  E temp;
      boolean swapped;
      for (i = 0; i < n - 1; i++) {
          swapped = false;
          for (j = 0; j < n - i - 1; j++) {
        	  int first = (int) arr[j];
        	  int second = (int) arr[j + 1];
             if (first < second) {
                   
                  // Swap arr[j] and arr[j+1]
                  temp = arr[j];
                  arr[j] = arr[j + 1];
                  arr[j + 1] = temp;
                  swapped = true;
             }
          }

          // If no two elements were
          // swapped by inner loop, then break
          if (swapped == false)
              break;
      }
     
  }
 

/* This was my tester
public static void main(String[] args) {
	StoutList list = new StoutList();
	
	list.add("w");
	list.add("w");
	list.add("w");
	list.add("w");
	list.add("j");
	list.add("j");
	list.add("j");
	list.add("j");
	list.add("j");
	list.add("j");
	list.add("j");
	list.add("j");
	
	list.add("w");
	//list.add(2, ">");
	//list.add(3, ">");
	//list.add(4, ">");
	list.add("a");
	list.add("b");
	list.add("c");
	list.add("d");
	list.add("e");
	list.add("f");
	list.add("g");
	list.add("h");
	list.add("i");
	
	
	System.out.println(list.toStringInternal());
	//list.add(7, ">");
	list.remove(0);
	System.out.println(list.toStringInternal());
	System.out.println(list.size);

}
*/
	
	
}