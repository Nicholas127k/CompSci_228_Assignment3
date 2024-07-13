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
	  	// if size is zero creates new node and places it in there
		if (size == 0) {
			
			Node temp = new Node();
			
			head.next = temp;
			temp.previous = head;
			temp.next = tail;
			tail.previous = temp;
			
			temp.addItem(item);
			
			size++;
			
			return true;
			
			
		} 
		else {
			//if node is not completely full
			if (tail.previous.count <nodeSize) {
				tail.previous.addItem(item);
			}
		
			else {
				//Node is full so has to create a new node
				Node temp = new Node();
				temp.addItem(item);
				
				Node temp2 = tail.previous;
				
				temp2.next = temp;
				temp.previous = temp2;
				
				temp.next = tail;
				tail.previous = temp;
			}
		}
		
		size++;
		return true;
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
	  if (pos < 0 || pos > size) throw new IndexOutOfBoundsException();
	  //if position is empty
	  
	  
	  if (head.next == tail) {
		  
		  add(item);
		  return;
	  }
	//Node current = new Node();	
	  NodeInfo nodel = search(pos);
	  Node temp = nodel.node;
	  int finder = nodel.offset; 
	  
	  if(finder == 0) {
	    
	   	if(temp == tail) {
	   		add(item);
	   		
			
			return;
			
			
	   	}
	   	else if (temp.previous.count < nodeSize && temp.previous != head){
	   		temp.previous.addItem(item);
			
			return;
			
	   	}		
	  }
	  if (temp.count < nodeSize) {
			temp.addItem(finder, item);
	  }
	  else {
		  Node temp2 = new Node();
		  int count = 0;
			while (count < nodeSize/2) {
				temp2.addItem(temp.data[nodeSize/2]);
				temp.removeItem(nodeSize/2);
				count++;
			}
			
			Node temp3 = temp.next;

			temp.next = temp2;
			temp2.previous = temp;
			temp2.next = temp3;
			temp3.previous = temp2;

			if (finder <= nodeSize / 2) {
				temp.addItem(finder, item);
			}
			
			
			if (finder > nodeSize / 2) {
				temp2.addItem((finder - nodeSize / 2), item);
			}
	  }
	  size++;
	  
	 
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
	  //if pos is at the end of the list or less than 0
	  if (pos < 0 || pos > size) {
			throw new IndexOutOfBoundsException();
	  }
	  //searches the node in question
	  NodeInfo nodeInfo = search(pos);
		Node temp = nodeInfo.node;
		//then takes its offset  value
		int finder = nodeInfo.offset;
		
		E nodefinder = temp.data[finder];
		
		if (temp.next == tail && temp.count == 1) {
			
			
			
			Node predecessor = temp.previous;
			predecessor.next = temp.next;
			
			temp.next.previous = predecessor;
			
			temp = null;
		}
		else if (temp.next == tail || temp.count > nodeSize / 2) {
			temp.removeItem(finder);
		}

		else {
			
			temp.removeItem(finder);
				Node succesor = temp.next;
			
			if (succesor.count > nodeSize / 2) {
				
				
				
				temp.addItem(succesor.data[0]);
					succesor.removeItem(0);
			}
			else if (succesor.count <= nodeSize / 2) {
				
				for (int i = 0; i < succesor.count; i++) {
					temp.addItem(succesor.data[i]);
				}
				
				
				temp.next = succesor.next;
					succesor.next.previous = temp;
				succesor = null;
			}
		}
		size--;
		return nodefinder;
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
	  //creates an array to take in list values
	  E[] array = (E[]) new Comparable[size];
	  	int next = 0;
	  	Node shift = head.next;
		for(int i = 0; i < (size / nodeSize) - 1; i++) {
			
			for(int j = 0; j < nodeSize; j++) {
				
				if(shift.data[j] != null) {
					array[next] = shift.data[j];
					next++;
				}
			}
			shift = shift.next;
		}
		//prepares for the new list
		size =0;
		head.next = tail;
		tail.previous = head;
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
	  //creats a array and sets all values inside of it
	  E[] array = (E[]) new Comparable[size];
	  	int next = 0;
	  	Node shift = head.next;
		for(int i = 0; i < (size / nodeSize) ; i++) {
		
			for(int j = 0; j < nodeSize; j++) {
				
				if(shift.data[j] != null) {
					array[next] = shift.data[j];
					next++;
				}
			}
			shift = shift.next;
		}
		//prepares for creating new array
		head.next = tail;
		tail.previous = head;
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
	  if (index < 0 || index > size) {
		  throw new IndexOutOfBoundsException();
	  }
	  
	  StoutListIterator iter = new StoutListIterator(index);
	  return iter;
	  
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


  
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  /**
   * 
   * This node holds info on the Node your looking at
   *
   */
		  
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
    public int count = 0;
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
  
  /**
   * @param pos
   * 
   */
  private NodeInfo search(int pos) {
	  //checks to see if pos is heads or tail
	  if (pos == -1) {
		  return new NodeInfo(head, 0);
	  }
	  
	  if (pos == size) {
		  return new NodeInfo(tail, 0);
	  }
	  //then it runs through the code and searchs for its positions inf
	  Node current = head.next;
	  int index = current.count - 1;
	  
	  while (current != tail && pos > index) {
		  current = current.next;
		  index += current.count;
	  }
	  
	  int offset = current.count + pos - index - 1;
	  return new NodeInfo(current, offset);
	}
 
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  private class StoutListIterator implements ListIterator<E>
  {
	// constants you possibly use ...   
	  
	// instance variables ... 
	  private static final int BEHIND = 0;
	    private static final int AHEAD = 1;
	    //private static final int NONE = 0;
	    
	    //private Node cursor;
	    private int index;
	    //private int direction;
	    public E[] list;
	    int move;
	  
	    
    /**
     * Default constructor 
     */
    public StoutListIterator()
    {
    	// TODO 
    	
    	move = -1;
    	index = 0;
    	list = (E[]) new Comparable[size];
    	Node temp = head.next;
    	int tempIndex = 0;
    	while(temp != tail) {
	    	for(int i = 0; i < temp.count; i++) {
	    		
	    		list[tempIndex] = temp.data[i];
	    		tempIndex++;
	    		
	    	}
	    	temp = temp.next;
    	}
    }

    /**
     * Constructor finds node at a given position.
     * @param pos
     */
    
    public StoutListIterator(int pos)
    {
    	// TODO 
    	if (pos < 0 || pos > size) throw new IndexOutOfBoundsException();
        
        move = -1;
        index = pos;
        list = (E[]) new Comparable[size];
        int tempIndex = 0;
    	Node temp = head.next;
    	
    	while(temp != tail) {
	    	for(int i = 0; i < temp.count; i++) {
	    		
	    		list[tempIndex] = temp.data[i];
	    		tempIndex++;
	    		
	    	}
	    	temp = temp.next;
    	}
        
        
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
    	if(hasNext() == false) {
    		throw new NoSuchElementException();
    		
    	}
    	
        move = AHEAD;
        
        return list[index++];
    }
    /**
	 * removes node
	 */
    @Override
    public void remove()
    {
    	// TODO 
    	if (move == AHEAD)
        {
          StoutList.this.remove(index - 1);
          
          list = (E[]) new Comparable[size];
      		Node temp = head.next;
      	
	      	while(temp !=  tail) {
	  	    	for(int i = 0; i < temp.count; i++) {
	  	    		
	  	    		list[i] = temp.data[i];
	  	    		
	  	    		
	  	    	}
	  	    	temp = temp.next;
	      	}
	      	move = -1;
	      	index--;
	      	if(index < 0) {
	      		index = 0;
	      	}
        }
        
        else
        {
          if (move == BEHIND)
          {
        	  StoutList.this.remove(index);
              list = (E[]) new Comparable[size];
          		Node temp = head.next;
          	
    	      	while(temp != tail) {
    	  	    	for(int i = 0; i < temp.count; i++) {
    	  	    		
    	  	    		list[i] = temp.data[i];
    	  	    		
    	  	    		
    	  	    	}
    	  	    	temp = temp.next;
    	      	}
    	      	move = -1;
    	      	
    	      	
            
          }
          else
          {
        	  throw new IllegalStateException();
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
		if(hasPrevious() == false) {
    		throw new NoSuchElementException();
		}
		index--;
		move = BEHIND;
    	
    	return list[index];
    	
	}
	/**
	 * sets index to the next index
	 */
	@Override
	public int nextIndex() {
		// TODO Auto-generated method stub
		
		return index;
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
		//sets what ever 
		if (move == AHEAD)
	      {
			
	        NodeInfo input = search(index - 1);
	        input.node.data[input.offset] = e;
	        list[index - 1] = e;
	      }
	      
		else if (move == BEHIND)
	      {
			
	    	  NodeInfo input = search(index);
	    	  input.node.data[input.offset] = e;
	    	  
	    	  
		       list[index] = e;
	      }
	      else
	      {
	    	  throw new IllegalStateException();
	      }
		
	}
	/**
	 * It does something
	 */

	@Override
	public void add(E e) {
		// TODO Auto-generated method stub
		if(e == null) {
			//if e is null throws exception
			throw new NullPointerException();
		}
		//adds to stout list 
		
		StoutList.this.add(index, e);
		
		
		//remakes the list
		list = (E[]) new Comparable[size];
  		Node temp = head.next;
  	
      	while(temp != tail) {
  	    	for(int i = 0; i < temp.count; i++) {
  	    		
  	    		list[i] = temp.data[i];
  	    		
  	    		
  	    	}
  	    	temp = temp.next;
      	}
      	move = -1;
      	index++;
		
		
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
		
		for(int first = 1; first < arr.length; first++) {
			E elementoToSort = arr[i];
			
			
			
			i = first - 1;
			while (i > -1 && comp.compare(arr[i], elementoToSort) > 0) {
			
				//swap(i, i + 1);
				arr[i+1] = arr[i];
				i--;
			}
			
			arr[i + 1] = elementoToSort;
		}// adds it back to the list
		for(int j = 0; j < arr.length; j++) {
			this.add(arr[j]);
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
	  boolean swap = false;
	  
	  for (int i = 1; i < n; i++) {
		  if (arr[i - 1].compareTo(arr[i]) < 0) {
			  E temp = arr[i - 1];
			  arr[i - 1] = arr[i];
			  arr[i] = temp;
			  swap = true;
		  }
	  }
	  
	  if (!swap) {
		  return;
	  } else {
		  bubbleSort(arr);
	  }
      
     
  }
 

/**
public static void main(String[] args) {
	StoutList<String> stout = new StoutList<String>();
		ListIterator<String> stoutIter = stout.listIterator();
		stoutIter.add("A");
		System.out.println(stout.toStringInternal());
		stoutIter.add("c");
		stoutIter.add("X");
		stoutIter.remove();
		stoutIter.add("X");
		stoutIter.add("r");
		stoutIter.add("D");
		stoutIter.add("E");
		stoutIter.remove();
		stoutIter.previous();
		stoutIter.previous();
		
		
		stoutIter.previous();
		stoutIter.remove();

	
	
	
	
	System.out.println(stout.toStringInternal());
	

}
***/
	
	
}