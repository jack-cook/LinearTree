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
        reCalculateStateWhenDescendantRemove(dataNode, true);
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
        reCalculateStateWhenDescendantRemove(dataNode, true);
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
        reCalculateStateWhenDescendantRemove(dataNode, true);
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
            int visibleFlatSizeBefore = getVisibleFlatSize();

            mVisibility = visibility;

            int visibleFlatSizeAfter = getVisibleFlatSize();

            int deltaDescendantFlatSize = 0;
            int deltaVisibleDescendantFlatSize = 0;

            deltaVisibleDescendantFlatSize = visibleFlatSizeAfter - visibleFlatSizeBefore;

            if (mParentNode != null) {
                mParentNode.notifyDescendantStateChange(deltaDescendantFlatSize, deltaVisibleDescendantFlatSize);

                DataNode preVisibleSibling = getPreVisibleSibling(this);
                notifyVisibilityChangeToFlatIndex(preVisibleSibling, this, visibility);
            }

        }
    }

    /**
     * 设置树是否展开,不展开,则其后代节点不可见
     *
     * @param isFolded
     */
    public final void setIsFolded(boolean isFolded) {
        if (mIsFolded != isFolded) {
            int visibleFlatSizeBefore = getVisibleFlatSize();

            mIsFolded = isFolded;

            int visibleFlatSizeAfter = getVisibleFlatSize();

            int deltaDescendantFlatSize = 0;
            int deltaVisibleDescendantFlatSize = 0;

            deltaVisibleDescendantFlatSize = visibleFlatSizeAfter - visibleFlatSizeBefore;

            if (mParentNode != null) {
                mParentNode.notifyDescendantStateChange(deltaDescendantFlatSize, deltaVisibleDescendantFlatSize);

                notifyFoldStateChangeToFlatIndex(this,isFolded);
            }
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


    private void onInternalNodeAdd(DataNode dataNode, int position) {

        //TODO 效率优化
        List<DataNode> nodes = new ArrayList<DataNode>();
        nodes.addAll(mHeaderChildNodes);
        nodes.addAll(mChildNodes);
        nodes.addAll(mFooterChildNodes);
        int index = nodes.indexOf(dataNode);

        DataNode preVisibleSibling = getPreVisibleSibling(dataNode);


        if (index == 0) {
            reCalculateStateWhenDescendantAdd(null, null, dataNode, true);
        } else {
            reCalculateStateWhenDescendantAdd(nodes.get(index - 1), preVisibleSibling, dataNode, true);
        }

        onChildNodeAdded(dataNode, position);
    }

    private DataNode getPreVisibleSibling(DataNode dataNode) {
        //TODO 效率优化
        DataNode parent = dataNode.getParentNode();
        assert parent != null;

        List<DataNode> nodes = new ArrayList<DataNode>();
        nodes.addAll(parent.mHeaderChildNodes);
        nodes.addAll(parent.mChildNodes);
        nodes.addAll(parent.mFooterChildNodes);

        int index = nodes.indexOf(dataNode);

        DataNode preVisibleSibling = null;
        for (int i = index - 1; i >= 0; i--) {
            preVisibleSibling = nodes.get(i);
            if (preVisibleSibling.isVisible()) {
                break;
            }
            preVisibleSibling = null;
        }

        return preVisibleSibling;
    }

    /**
     * 当有节点树被添加到该节点树的某一节点后会被调用,在此方法中
     * 做节点树状相应的状态的更新,并通知父节点(递归).
     *
     * @param preSibling
     * @param descendant
     * @param cascadeVisibleFlatSizeChange 节点到增加是否会引起可见的节点数量的改变
     */
    private void reCalculateStateWhenDescendantAdd(DataNode preSibling, DataNode preVisibleSibling, DataNode descendant, boolean cascadeVisibleFlatSizeChange) {
        mDescendantSize += descendant.getFlatSize();
        if (cascadeVisibleFlatSizeChange == true) {
            mDescendantVisibleSize += descendant.getVisibleFlatSize();
        }

        if (mParentNode != null) {
            boolean cascadeVisibleFlatSizeChangeToParent = false;

            //如果本节点的子节点可见，则继续传播，如果本节点的子节点不可见，则打断了子孙节点可见性的改变的传播路径
            if (cascadeVisibleFlatSizeChange && mVisibility && !mIsFolded) {
                cascadeVisibleFlatSizeChangeToParent = true;
            }

            /*
            *递归调用父节点
            */
            mParentNode.reCalculateStateWhenDescendantAdd(preSibling, preVisibleSibling, descendant, cascadeVisibleFlatSizeChangeToParent);
        }

        /*
        *将新添加进来到节点树添加到本节点树到索引中(如果本节点树创建了索引的话)
         */
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.addSubtree(preSibling, preVisibleSibling, descendant);
        }
    }

    /**
     * 某一个子孙节点被删除时
     * 做节点树相应状态的更新,并通知其父节点(递归)
     *
     * @param descent
     * @param cascadeVisibleFlatSizeChange 子孙的移除是否引起该节点可见节点数的改变
     */
    private void reCalculateStateWhenDescendantRemove(DataNode descent, boolean cascadeVisibleFlatSizeChange) {
        mDescendantSize -= descent.getFlatSize();
        if (cascadeVisibleFlatSizeChange) {
            mDescendantVisibleSize -= descent.getVisibleFlatSize();
        }

        if (mParentNode != null) {
            boolean cascadeVisibleFlatSizeChangeToParent = false;
            if (cascadeVisibleFlatSizeChange && mVisibility && !mIsFolded) {
                cascadeVisibleFlatSizeChangeToParent = true;
            }

            mParentNode.reCalculateStateWhenDescendantRemove(descent, cascadeVisibleFlatSizeChangeToParent);
        }

        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.removeFlatNodes(descent);//todo 节点的状态都更新完毕了，这时flatindex中节点数与状态不一致？？
        }
    }


    /**
     * 子孙节点改变，引起先辈节点与子孙节点有关的状态改变
     *
     * @param deltaDescendantSize        所导致的 子孙节点数量的增减
     * @param deltaVisibleDescendantSize 所导致的 子孙节点可见数的增减
     */
    private void notifyDescendantStateChange(int deltaDescendantSize, int deltaVisibleDescendantSize) {
        mDescendantSize += deltaDescendantSize;
        mDescendantVisibleSize += deltaVisibleDescendantSize;

        DataNode parent = getParentNode();
        if (parent != null) {

            //判断可见子孙节点数量的改变是否继续传递下去
            if (deltaVisibleDescendantSize != 0 && (!mVisibility || mIsFolded)) {
                deltaVisibleDescendantSize = 0;
            }

            if (deltaDescendantSize != 0 && deltaVisibleDescendantSize != 0)
                parent.notifyDescendantStateChange(deltaDescendantSize, deltaVisibleDescendantSize);
        }
    }


    /**
     * 节点可见状态改变，更新visibleFlatIndex
     *
     * @param preVisibleSibling
     * @param node
     * @param currentVisibility
     */
    private void notifyVisibilityChangeToFlatIndex(DataNode preVisibleSibling, DataNode node, boolean currentVisibility) {
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.onNodeVisibilityChange(preVisibleSibling, node, currentVisibility);
        }

        if (mParentNode != null) {
            mParentNode.notifyVisibilityChangeToFlatIndex(preVisibleSibling, node, currentVisibility);
        }
    }

    /**
     * 节点折叠状态改变，更新visibleFlatIndex
     * @param node
     * @param currentFolded
     */
    private void notifyFoldStateChangeToFlatIndex(DataNode node, boolean currentFolded) {
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.onNodeFoldStateChange(node, currentFolded);
        }

        if (mParentNode != null) {
            mParentNode.notifyFoldStateChangeToFlatIndex(node, currentFolded);
        }
    }

}
