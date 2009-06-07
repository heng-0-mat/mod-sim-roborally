import java.awt.Point;
import java.util.LinkedList;

/**
 * @author Qi Zheng
 *
 *Definiert eine Node
 */
public class QZ_Node implements Comparable  {
	  
	  public Point _pos;
	  public int _costFromStart;
	  public int _costToObject;
	  public QZ_Node _parentNode;
	  private QZ_Node()  {
	  }
	  	  
	  public QZ_Node(Point _pos)  {
	    this._pos = _pos;
	  }
	  
	  //knotengewicht
	  public int getCost(QZ_Node node)  {
	    int m = node._pos.x - _pos.x;
	    int n = node._pos.y - _pos.y;
	    
	    return (int) Math.sqrt(m * m + n * n);
	  }
	 
	  //equals Methode
	  public boolean equals(Object node)  {
	    
	    if (_pos.x == ((QZ_Node) node)._pos.x && _pos.y == ((QZ_Node) node)._pos.y)  {
	      return true;
	    }
	    return false;
	  }
	 	 
	  public int compareTo(Object node)  {
	    int a1 = _costFromStart + _costToObject;
	    int a2 = ((QZ_Node) node)._costFromStart + ((QZ_Node) node)._costToObject;
	    if (a1 < a2)  {
	      return -1;
	    } else if (a1 == a2)  {
	      return 0;
	    } else  {
	      return 1;
	    }
	  }
	
	  public LinkedList getLimit()  {
	    LinkedList limit = new LinkedList();
	    int x = _pos.x;
	    int y = _pos.y;
	   
	    limit.add(new QZ_Node(new Point(x, y - 1)));
	    
	    limit.add(new QZ_Node(new Point(x + 1, y)));
	   
	    limit.add(new QZ_Node(new Point(x, y + 1)));
	   
	    limit.add(new QZ_Node(new Point(x - 1, y)));
	   
	    return limit;
	  }
	}