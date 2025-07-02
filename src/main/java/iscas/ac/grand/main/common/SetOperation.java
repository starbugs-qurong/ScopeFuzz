package iscas.ac.grand.main.common;

import java.util.HashSet;
import java.util.Set;

/**
 * Some operations of Set<T>
 */

public class SetOperation {

    //交集
    public <T> Set<T> intersect(Set<T> set1, Set<T> set2){
        if(set1==null||set2==null){
            return new HashSet<>();
        }
        Set<T> set_1=new HashSet<>();
        set_1.addAll(set1);
        set_1.retainAll(set2);
        return set_1;
    }

    //并集
    public <T> Set<T> join(Set<T> set1, Set<T> set2){
        if(set1==null&&set2==null){
            return new HashSet<>();
        }
        else if(set1==null){
            return set2;
        }
        else if(set2==null){
            return set1;
        }
        Set<T> set_1=new HashSet<>();
        set_1.addAll(set1);
        set_1.addAll(set2);
        return set_1;
    }

    //差集
    public <T> Set<T> different(Set<T> set1, Set<T> set2){
        if(set1==null){
            return new HashSet<>();
        }
        if(set2==null){
            return set1;
        }
        Set<T> result=new HashSet<>();
        result.addAll(set1);
        result.removeAll(set2);
        return result;
    }
}