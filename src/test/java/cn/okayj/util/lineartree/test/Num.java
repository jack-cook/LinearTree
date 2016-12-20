package cn.okayj.util.lineartree.test;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2016/12/20.
 */
public class Num {
    private int n;

    private List<Num> children = new ArrayList<Num>();

    public Num(int n) {
        this.n = n;
    }

    public Num add(Num num){
        children.add(num);
        return this;
    }

    public int getChildSize(){
        return children.size();
    }

    public Num get(int position){
        return children.get(position);
    }

    @Override
    public String toString() {
        return ""+n;
    }
}
