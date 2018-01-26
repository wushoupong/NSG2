package nsg.tool;

import nsg.component.Node;

public class Tool {
    public static double distance(Node s, Node d){
    	int x = s.x-d.x;
    	int y = s.y-d.y;
    	double dis = Math.sqrt(x*x + y*y);
    	return dis;
    }
    
   public static int translateX(int shiftX, int x, float scale){
	   return (int)((x/scale)-shiftX);
	   //return x-shiftX;
   }
   
   public static int translateY(int shiftY, int y, float scale){
	   return (int)(10000-(y/scale-shiftY));
   }
   
   public static int translateX(int x){
	   //return (int)(x*scale);
	   return x;
   }
   
   public static int translateY(int y){
	   return 10000-y;
   }
   
//    public static double distance(int x1, int y1, int x2, int y2){
//    	int x = x1-x2;
//    	int y = y1-y2;
//    	double dis = Math.sqrt(x*x + y*y);
//    	return dis;
//    }   
//    
//    public static void sortNodeByX(List list){
//    	Comparator c = new Comparator(){
//    		public int 	compare(Object o1, Object o2) {
//    			if (((Node)o1).x>((Node)o2).x){
//    				return 1;
//    			}else{
//    				return -1;
//    			}
//    		}
//    		public boolean 	equals(Object obj) {
//    			 return true;
//    		}
//    	};
//    	Collections.sort(list, c);
//    }	
}
