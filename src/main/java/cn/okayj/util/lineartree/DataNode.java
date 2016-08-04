/*
 * Copyright 2016 Kaijie Huang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.okayj.util.lineartree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 15/11/27.
 * 树型结构数据的节点的包装类,该类有一个父节点和数量不定的子节点.
 * 用该类包装的树型结构可以取得一个平坦结构(列表结构)的索引,索引是树型结构的平坦映射,也就是目录.
 * 可以把节点看成书的章节和文章(叶子节点),平坦索引相当于目录,可以按顺序访问章节和文章.
 * 该类用于将树型结构的数据映射到列表,从而展示在ListView中.
 * 每个子树结构在ListView中会列在一起.
 */
public class DataNode<S> {
    public static final int CHILD_POSITION_HEADER = 1;
    public static final int CHILD_POSITION_MIDDLE = 2;
    public static final int CHILD_POSITION_FOOTER = 3;


    protected S mSource;

    protected boolean mVisibility = true;
    protected boolean mIsFolded = false;

    private DataNode mParentNode;
    private NodeFlatIndex mNodeFlatIndex;
    protected List<DataNode> mHeaderChildNodes = new ArrayList<DataNode>();
    protected List<DataNode> mChildNodes = new ArrayList<DataNode>();
    protected List<DataNode> mFooterChildNodes = new ArrayList<DataNode>();

    private int mDescendantSize = 0;
    private int mDescendantVisibleSize = 0;

    @Deprecated
    /**
     * 用onNodeAdd()和onNodeRemove()实现
     */
    private final void onFlatSizeChange(int minusOrPlus) {
        mDescendantSize += minusOrPlus;
        if (mParentNode != null) {
            mParentNode.onFlatSizeChange(minusOrPlus);
        }
    }

    private void setParentNode(DataNode parentNode) {
        mParentNode = parentNode;
    }

    /**
     * 获取数据源
     *
     * @return
     */
    public S getSource() {
        return mSource;
    }

    /**
     * 设置数据源
     *
     * @param source
     */
    public void setSource(S source) {
        mSource = source;
    }

    public DataNode getParentNode() {
        return mParentNode;
    }

    /**
     * 设置头部子节点
     *
     * @param headerNode
     */
    public final void addHeaderNode(DataNode headerNode) {
        /*headerNode.setParentNode(this);
        mHeaderChildNodes.add(headerNode);
        int nodeFlatSize = headerNode.getFlatSize();
        *//*mDescendantSize += nodeFlatSize;
        if(mParentNode != null){
            mParentNode.onFlatSizeChange(nodeFlatSize);
        }*//*
        onFlatSizeChange(nodeFlatSize);*/
        addHeaderNode(mHeaderChildNodes.size(), headerNode);
    }

    /**
     * 设置头部子节点
     *
     * @param headerNode
     */
    public final void addHeaderNode(int position, DataNode headerNode) {
        headerNode.setParentNode(this);
        mHeaderChildNodes.add(position, headerNode);
        /*int nodeFlatSize = headerNode.getFlatSize();
        onFlatSizeChange(nodeFlatSize);*/

        onInternalNodeAdd(headerNode, CHILD_POSITION_HEADER);
    }

    /**
     * 设置主体子节点(在头部和尾部节点之间)
     *
     * @param childNode
     */
    public final void addChildNode(DataNode childNode) {
        addChildNode(mChildNodes.size(), childNode);
    }

    /**
     * 设置主体子节点(在头部和尾部节点之间)
     *
     * @param childNode
     */
    public final void addChildNode(int position, DataNode childNode) {
        childNode.setParentNode(this);
        mChildNodes.add(position, childNode);
        /*int nodeFlatSize = childNode.getFlatSize();
        onFlatSizeChange(nodeFlatSize);
*/
        onInternalNodeAdd(childNode, CHILD_POSITION_MIDDLE);
    }

    /**
     * 设置尾部子节点
     *
     * @param footerNode
     */
    public final void addFooterNode(DataNode footerNode) {
        addFooterNode(mFooterChildNodes.size(), footerNode);
    }

    /**
     * 设置尾部子节点
     *
     * @param footerNode
     */
    public final void addFooterNode(int position, DataNode footerNode) {
        footerNode.setParentNode(this);
        mFooterChildNodes.add(position, footerNode);
        /*int nodeFlatSize = footerNode.getFlatSize();
        onFlatSizeChange(nodeFlatSize);*/

        onInternalNodeAdd(footerNode, CHILD_POSITION_FOOTER);
    }

    public final boolean removeHeaderNode(DataNode dataNode) {
        int position = mHeaderChildNodes.indexOf(dataNode);
        if (position >= 0) {
            removeHeaderNode(position);
            return true;
        }
        return false;
    }

    public final DataNode removeHeaderNode(int position) {
        DataNode dataNode = mHeaderChildNodes.remove(position);

        /*int nodeFlatSize = dataNode.getFlatSize();
        onFlatSizeChange(-nodeFlatSize);*/
        onNodeRemove(dataNode, true);
        dataNode.mParentNode = null;
        onChildNodeRemoved(dataNode, CHILD_POSITION_HEADER);
        return dataNode;
    }

    public final boolean removeChildNode(DataNode dataNode) {
        int position = mChildNodes.indexOf(dataNode);
        if (position >= 0) {
            removeChildNode(position);
            return true;
        }
        return false;
    }

    public final DataNode removeChildNode(int position) {
        DataNode dataNode = mChildNodes.remove(position);

        /*int nodeFlatSize = dataNode.getFlatSize();
        onFlatSizeChange(-nodeFlatSize);*/
        onNodeRemove(dataNode, true);
        dataNode.mParentNode = null;
        onChildNodeRemoved(dataNode, CHILD_POSITION_MIDDLE);
        return dataNode;
    }

    public final boolean removeFooterNode(DataNode dataNode) {
        int position = mFooterChildNodes.indexOf(dataNode);
        if (position >= 0) {
            removeFooterNode(position);
            return true;
        }
        return false;
    }

    public final DataNode removeFooterNode(int position) {
        DataNode dataNode = mFooterChildNodes.remove(position);
        /*int nodeFlatSize = dataNode.getFlatSize();
        onFlatSizeChange(-nodeFlatSize);*/
        onNodeRemove(dataNode, true);
        dataNode.mParentNode = null;
        onChildNodeRemoved(dataNode, CHILD_POSITION_FOOTER);
        return dataNode;
    }

    /**
     * 将本节点从父节点移除
     */
    public final void removeFromParent() {
        if (mParentNode != null) {
            mParentNode.removeNode(this);
        }
    }

    public final void removeNode(DataNode dataNode) {
        if (!removeHeaderNode(dataNode)) {
            if (!removeChildNode(dataNode)) {
                removeFooterNode(dataNode);
            }
        }
    }

    /**
     * 获取树结构对应的平坦结构(展开成列表)的大小,即该树所有深度的全部节点个数
     *
     * @return
     */
    public final int getFlatSize() {
        return mDescendantSize + 1;
    }

    /**
     * 获取树结构对应的可见节点展开之后的大小
     *
     * @return
     */
    public final int getVisibleFlatSize() {
        if (mVisibility == false) {
            return 0;
        } else if (mIsFolded == true) {
            return 1;
        } else {
            return mDescendantVisibleSize + 1;
        }
    }

    /**
     * 获取树结构的后代对应的可见节点展开之后的大小(即不包括本节点,并且忽略本节点的可见性)
     *
     * @return
     */
    public final int getDescendantVisibleSize() {
        return mDescendantVisibleSize;
    }

    public int getHeaderNodeSize() {
        return mHeaderChildNodes.size();
    }

    public DataNode getHeaderNode(int position) {
        return mHeaderChildNodes.get(position);
    }

    public int getChildNodeSize() {
        return mChildNodes.size();
    }

    public DataNode getChildNode(int position) {
        return mChildNodes.get(position);
    }

    public int getFooterNodeSize() {
        return mFooterChildNodes.size();
    }

    public DataNode getFooterNode(int position) {
        return mFooterChildNodes.get(position);
    }

    public final boolean isVisible() {
        return mVisibility;
    }

    public final boolean isFold() {
        return mIsFolded;
    }

    /**
     * 设置节点树是否可见
     *
     * @param visibility
     */
    public final void setVisibility(boolean visibility) {
        if (mVisibility != visibility) {
            mVisibility = visibility;
            onNodeVisibilityChange(this, visibility);
        }
    }

    /**
     * 设置树是否展开,不展开,则其后代节点不可见
     *
     * @param isFolded
     */
    public final void setIsFolded(boolean isFolded) {
        if (mIsFolded != isFolded) {
            mIsFolded = isFolded;
            onNodeFoldStateChange(this, isFolded);
        }
    }

    /**
     * 获取平坦索引(树结构对应的列表的索引)
     *
     * @return
     */
    public NodeFlatIndex getFlatIndex() {
        if (mNodeFlatIndex == null) {
            mNodeFlatIndex = new NodeFlatIndex(this);
        }

        return mNodeFlatIndex;
    }

    /**
     * @param addedNode
     * @param position  添加的孩子节点所在位置:头部.中部.尾部
     */
    protected void onChildNodeAdded(DataNode addedNode, int position) {

    }

    /**
     * @param removedNode
     * @param position    删除的孩子节点所在位置:头部.中部.尾部
     */
    protected void onChildNodeRemoved(DataNode removedNode, int position) {

    }

    /**
     * 销毁索引.
     * 索引默认不创建,只有获取索引时才被创建,
     * 索引会略微降低性能,所以不用时最好销毁.
     */
    void invalidateFlatIndex() {
    }

    {
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex = null;
        }
    }

    private void onNodeVisibilityChange(DataNode dataNode, boolean newVisibility) {
        if(dataNode != this){
            //非自我通知,则为被后代节点通知,因后代节点可见性改变,需更新自己的可见后代数量.
            if(newVisibility == true){
                mDescendantVisibleSize += dataNode.mDescendantVisibleSize + 1;
            }else {
                mDescendantVisibleSize -= dataNode.mDescendantVisibleSize + 1;
            }
        }

        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.onNodeVisibilityChange(getPreVisibleCousinNode(dataNode), dataNode, newVisibility);
        }

        if (mParentNode != null) {
            mParentNode.onNodeVisibilityChange(dataNode, newVisibility);
        }
    }

    private void onNodeFoldStateChange(DataNode dataNode, boolean currentFolded) {
        if(dataNode != this){
            //非自我通知,则为被后代节点通知,因后代节点可见性改变,需更新自己的可见后代数量.
            if(currentFolded == true){
                mDescendantVisibleSize -= dataNode.mDescendantVisibleSize;
            }else {
                mDescendantVisibleSize += dataNode.mDescendantVisibleSize;
            }
        }

        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.onNodeFoldStateChange(dataNode, currentFolded);
        }

        if (mParentNode != null) {
            mParentNode.onNodeFoldStateChange(dataNode, currentFolded);
        }
    }

    private void onInternalNodeAdd(DataNode dataNode, int position) {

        //TODO 效率优化
        List<DataNode> nodes = new ArrayList<DataNode>();
        nodes.addAll(mHeaderChildNodes);
        nodes.addAll(mChildNodes);
        nodes.addAll(mFooterChildNodes);
        int index = nodes.indexOf(dataNode);

        DataNode preVisibleCousinNode = getPreVisibleCousinNode(dataNode);


        if (index == 0) {
            onNodeAdd(null, null, dataNode, true);
        } else {
            onNodeAdd(nodes.get(index - 1), preVisibleCousinNode, dataNode, true);
        }

        onChildNodeAdded(dataNode, position);
    }

    private DataNode getPreVisibleCousinNode(DataNode dataNode) {
        //TODO 效率优化
        DataNode parent = dataNode.getParentNode();
        assert parent != null;

        List<DataNode> nodes = new ArrayList<DataNode>();
        nodes.addAll(parent.mHeaderChildNodes);
        nodes.addAll(parent.mChildNodes);
        nodes.addAll(parent.mFooterChildNodes);

        int index = nodes.indexOf(dataNode);

        DataNode preVisibleCousinNode = null;
        for (int i = index - 1; i >= 0; i--) {
            preVisibleCousinNode = nodes.get(i);
            if (preVisibleCousinNode.isVisible()) {
                break;
            }
            preVisibleCousinNode = null;
        }

        return preVisibleCousinNode;
    }

    /**
     * 当有节点树被添加到该节点树的某一节点后会被调用,在此方法中
     * 做节点树状相应的状态的更新,并通知父节点(递归).
     *
     * @param preCousinNode
     * @param addedNode
     * @param descendantVisibleSizeChange 节点到增加是否会引起可见的节点数量的改变
     */
    private void onNodeAdd(DataNode preCousinNode, DataNode preVisibleCousinNode, DataNode addedNode, boolean descendantVisibleSizeChange) {
        mDescendantSize += addedNode.getFlatSize();
        if (descendantVisibleSizeChange == true) {
            mDescendantVisibleSize += addedNode.getVisibleFlatSize();
        }

        if (mParentNode != null) {
            boolean parentNodeVisibleSizeChange = false;
            /*
            *如果本节点的后代可见节点数改变了,并且本节点的后代节点可见(本节点可见并为展开状态),
            * 可见后代节点数的改变会传递到父节点.
             */
            if (descendantVisibleSizeChange && mVisibility && !mIsFolded) {
                parentNodeVisibleSizeChange = true;
            }

            /*
            *递归调用父节点
            */
            mParentNode.onNodeAdd(preCousinNode, preVisibleCousinNode, addedNode, parentNodeVisibleSizeChange);
        }

        /*
        *将新添加进来到节点树添加到本节点树到索引中(如果本节点树创建了索引的话)
         */
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.addFlatNodes(preCousinNode, preVisibleCousinNode, addedNode);
        }
    }

    /**
     * 当有节点树从该节点树到某一后代节点删除会被调用,在此方法中
     * 做节点树相应状态的更新,并通知其父节点(递归)
     *
     * @param dataNode
     */
    private void onNodeRemove(DataNode dataNode, boolean mDescendantFlatSizeChange) {
        mDescendantSize -= dataNode.getFlatSize();
        if (mDescendantFlatSizeChange) {
            mDescendantVisibleSize -= dataNode.getVisibleFlatSize();
        }

        if (mParentNode != null) {
            boolean parentDescendantFlatSizeChange = false;
            if (mDescendantFlatSizeChange && mVisibility && !mIsFolded) {
                parentDescendantFlatSizeChange = true;
            }

            mParentNode.onNodeRemove(dataNode, parentDescendantFlatSizeChange);
        }

        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.removeFlatNodes(dataNode);
        }
    }
}
