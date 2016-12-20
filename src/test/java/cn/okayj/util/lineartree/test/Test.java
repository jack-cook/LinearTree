package cn.okayj.util.lineartree.test;

import cn.okayj.util.lineartree.DataNode;
import cn.okayj.util.lineartree.NodeFlatIndex;

/**
 * Created by jack on 2016/12/20.
 */
public class Test {
    private Num number;
    private DataNode<Num> root;
    private NodeFlatIndex flatIndex;
    private NodeFlatIndex.VisibleFlatIndex visibleFlatIndex;

    public Test(){
        number = new Num(1);
        number.add(new Num(2));
        Num temp = new Num(3);
        temp.add(new Num(4));
        temp.add(new Num(5));
        temp.add(new Num(6));
        number.add(temp);
        temp = new Num(7);
        temp.add(new Num(8));
        temp.add(new Num(9));
        number.add(temp);

        root = TreeBuilder.build(number);
        flatIndex = root.getFlatIndex();
        visibleFlatIndex = flatIndex.getVisibleIndex();
    }

    @org.junit.Test
    public void outline(){
        print(root,0);
        print(flatIndex);
        print(visibleFlatIndex);
    }

    @org.junit.Test
    public void remove5(){
        outline();
        flatIndex.get(4).removeFromParent();
        outline();
        flatIndex.get(2).setVisibility(false);
        outline();
        flatIndex.get(3).removeFromParent();
        flatIndex.get(2).setVisibility(true);
        outline();
    }

    public static void print(DataNode<Num> node,int dep){
        for (int i = 0; i < dep; ++i){
            System.out.print("\t");
        }
        System.out.println(node.getSource().toString());

        for (int i= 0; i < node.getChildNodeSize(); ++i){
            DataNode n = node.getChildNode(i);
            print(n,dep + 1);
        }
    }

    public static void print(NodeFlatIndex nodeFlatIndex){
        for (int i = 0; i < nodeFlatIndex.size(); ++i){
            Num num =(Num)nodeFlatIndex.get(i).getSource();
            System.out.print("-"+num);
        }
        System.out.println();
    }

    public static void print(NodeFlatIndex.VisibleFlatIndex nodeFlatIndex){
        for (int i = 0; i < nodeFlatIndex.size(); ++i){
            Num num =(Num)nodeFlatIndex.get(i).getSource();
            System.out.print("-"+num);
        }
        System.out.println();
    }
}
