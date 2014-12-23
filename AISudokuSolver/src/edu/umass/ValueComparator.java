package edu.umass;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator implements Comparator<String>{
    Map <String, String> baseMap ;
   
     public ValueComparator( Map<String, String> base){
            this.baseMap = base;
    }

     @Override
     public int compare(String o1, String o2) {
            // TODO Auto-generated method stub
            if(baseMap .get(o1).length() >= baseMap.get(o2).length()){ // Longer string means more remaining values
                   return 1;
           } else {
                   return -1;
           }
    }
   
}


