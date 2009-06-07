import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Qi Zheng
 *
 */
public class QZ_PathFinder {
	  private LevelList _levelList;
      private LinkedList<QZ_Node> _closedList;
	  private int[][] _map;
	  private int[] _limit;
	 
	  public QZ_PathFinder(int[][] map, int[] limit) {
	    _map = map;
	    _limit = limit;
	    _levelList = new LevelList();
	    _closedList = new LinkedList<QZ_Node>();
	  }
	  
	  /**
	 * @param Startposition
	 * @param Zielsposition
	 * @return Liefert eine Liste des kurzsten Wegs
	 */
	public List<QZ_Node> searchPath(Point startPos, Point objectPos) {
	    // Anfangsknoten und Endknoten
	    QZ_Node startNode = new QZ_Node(startPos);
	    QZ_Node objectNode = new QZ_Node(objectPos);
	    
	    startNode._costFromStart = 0;
	    startNode._costToObject = startNode.getCost(objectNode);
	    startNode._parentNode = null;
	    
	    _levelList.add(startNode);
	    // durchsuchen bis levelList leer ist
	    while (!_levelList.isEmpty()) {
	      // loeschen den Anfangsknoten
	      QZ_Node firstNode = (QZ_Node) _levelList.removeFirst();
	      // ob gleich ist
	      if (firstNode.equals(objectNode)) {
	        //Menge der Knoten zurueckgeben
	        return makePath(firstNode);
	      } else {
	        
	        // sonst wird es in List eingefuegt
	        _closedList.add(firstNode);
	        
	        LinkedList<QZ_Node> _limit = firstNode.getLimit();
	        // durchensuchen
	        for (int i = 0; i < _limit.size(); i++) {
	          // Nachbarknoten
	          QZ_Node neighborNode = (QZ_Node) _limit.get(i);
	          
	          boolean isOpen = _levelList.contains(neighborNode);
	          
	          boolean isClosed = _closedList.contains(neighborNode);
	          
	          boolean isHit = isHit(neighborNode._pos.x, neighborNode._pos.y);
	          
	          if (!isOpen && !isClosed && !isHit) {
	            
	            neighborNode._costFromStart = firstNode._costFromStart + 1;
	            
	            neighborNode._costToObject = neighborNode.getCost(objectNode);
	            // aendern Eltern Knoten
	            neighborNode._parentNode = firstNode;
	            
	            _levelList.add(neighborNode);
	          }
	        }
	      }
	    }
	    // leeren list
	    _levelList.clear();
	    _closedList.clear();
	    
	    return null;
	  }
	  
	  /* 
	   * Im Rahmen
	   * 
	   * @param x
	   * @param y
	   * @return
	   */
	  private boolean isHit(int x, int y) {
	    for (int i = 0; i < _limit.length; i++) {
	      if (_map[y][x] == _limit[i]) {
	        return true;
	      }
	    }
	    return false;
	  }
	  
	  /*
	   * Knoten List
	   * 
	   * @param node
	   * @return
	   */
	  private LinkedList<QZ_Node> makePath(QZ_Node node) {
	    LinkedList<QZ_Node> path = new LinkedList<QZ_Node>();
	    
	    while (node._parentNode != null) {
	      // der erste Knoten wird hier eingefuegt
	      path.addFirst(node);
	      // node=parent node
	      node = node._parentNode;
	    }
	    // fuege bei ersten Knoten
	    path.addFirst(node);
	    return path;
	  }
	  private class LevelList extends LinkedList {
	    
	    private static final long serialVersionUID = 1L;
	    
	    /*
	     *fuegen einen Knoten ein 
	     * 
	     * @param node
	     */
	    public void add(QZ_Node node) {
	      for (int i = 0; i < size(); i++) {
	        if (node.compareTo(get(i)) <= 0) {
	          add(i, node);
	          return;
	        }
	      }
	      addLast(node);
	    }
	  }
	}