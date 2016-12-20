package cn.okayj.util.lineartree.test;

import cn.okayj.util.lineartree.DataNode;

/**
 * Created by jack on 2016/12/20.
 */
public class TreeBuilder {
    public static DataNode<Num> build(Num num){
        DataNode node = new DataNode();
        node.setSource(num);
        for (int i = 0; i < num.getChildSize(); ++i){
            node.addChildNode(build(num.get(i)));
        }
        return node;
    }
}
