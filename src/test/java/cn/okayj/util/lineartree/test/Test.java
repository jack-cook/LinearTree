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
        Num one = new Num(1);
        Num two = new Num(2);
        Num three = new Num(3);
        Num four = new Num(4);
        Num five = new Num(5);
        Num six = new Num(6);
        Num seven = new Num(7);
        Num eight = new Num(8);
        Num nine = new Num(9);

        one.add(two);
        one.add(three);
            three.add(four);
            three.add(five);
            three.add(six);
        one.add(seven);
            seven.add(eight);
            seven.add(nine);

        number = one;
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
        print("init state");
        outline();

        print("remove 5");
        flatIndex.get(4).removeFromParent();
        outline();

        print("hide 3");
        flatIndex.get(2).setVisibility(false);
        outline();

        print("remove 4");
        flatIndex.get(3).removeFromParent();
        outline();

        print("show 3");
        flatIndex.get(2).setVisibility(true);
        outline();
    }

    public static void print(String msg){
        System.out.println(msg);
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
