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

    int getDescendantVisibleSize() {
        return mDescendantVisibleSize;
    }

    /**
     * 设置头部子节点
     *
     * @param headerNode
     */
    public final void addHeaderNode(DataNode headerNode) {
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

        onInternalChildAdd(headerNode, CHILD_POSITION_HEADER);
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

        onInternalChildAdd(childNode, CHILD_POSITION_MIDDLE);
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

        onInternalChildAdd(footerNode, CHILD_POSITION_FOOTER);
    }

    public final int removeHeaderNode(DataNode dataNode) {
        int position = mHeaderChildNodes.indexOf(dataNode);
        if (position >= 0) {
            removeHeaderNode(position);
        }
        return position;
    }

    public final DataNode removeHeaderNode(int position) {
        DataNode dataNode = mHeaderChildNodes.remove(position);

        onInternalChildRemove(dataNode,CHILD_POSITION_MIDDLE);
        return dataNode;
    }

    public final int removeChildNode(DataNode dataNode) {
        int position = mChildNodes.indexOf(dataNode);
        if (position >= 0) {
            removeChildNode(position);
        }
        return position;
    }

    public final DataNode removeChildNode(int position) {
        DataNode dataNode = mChildNodes.remove(position);

        onInternalChildRemove(dataNode,CHILD_POSITION_MIDDLE);
        return dataNode;
    }

    public final int removeFooterNode(DataNode dataNode) {
        int position = mFooterChildNodes.indexOf(dataNode);
        if (position >= 0) {
            removeFooterNode(position);
        }
        return position;
    }

    public final DataNode removeFooterNode(int position) {
        DataNode dataNode = mFooterChildNodes.remove(position);
        onInternalChildRemove(dataNode,CHILD_POSITION_MIDDLE);
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
        if (removeHeaderNode(dataNode) < 0) {
            if (removeChildNode(dataNode) < 0) {
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
        return calculateVisibleFlatSize(mVisibility,mIsFolded,mDescendantVisibleSize);
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


            if (mParentNode != null) {
                int visibleFlatSizeAfter = getVisibleFlatSize();
                int deltaDescendantFlatSize = 0;
                int deltaVisibleDescendantFlatSize = 0;
                deltaVisibleDescendantFlatSize = visibleFlatSizeAfter - visibleFlatSizeBefore;

                mParentNode.notifyDescendantStateChange(deltaDescendantFlatSize, deltaVisibleDescendantFlatSize);

            }

            DataNode preVisibleSibling = getPreVisibleSibling(this);
            notifyVisibilityChangeToFlatIndex(preVisibleSibling, this, visibility);
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


            if (mParentNode != null) {
                int visibleFlatSizeAfter = getVisibleFlatSize();
                int deltaDescendantFlatSize = 0;
                int deltaVisibleDescendantFlatSize = 0;
                deltaVisibleDescendantFlatSize = visibleFlatSizeAfter - visibleFlatSizeBefore;

                mParentNode.notifyDescendantStateChange(deltaDescendantFlatSize, deltaVisibleDescendantFlatSize);
            }

            notifyFoldStateChangeToFlatIndex(this, isFolded);

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

    /**
     * 直接子节点添加
     *
     * @param dataNode
     * @param position
     */
    private void onInternalChildAdd(DataNode dataNode, int position) {
        /*
        更新受影响的先辈节点的状态
         */
        int deltaDescendantSize = dataNode.getFlatSize();
        int deltaVisibleDescendantSize = dataNode.getVisibleFlatSize();
        notifyDescendantStateChange(deltaDescendantSize, deltaVisibleDescendantSize);

        /*
        添加到index
         */
        //TODO 效率优化
        List<DataNode> nodes = new ArrayList<DataNode>();
        nodes.addAll(mHeaderChildNodes);
        nodes.addAll(mChildNodes);
        nodes.addAll(mFooterChildNodes);
        int index = nodes.indexOf(dataNode);

        DataNode preVisibleSibling = getPreVisibleSibling(dataNode);
        DataNode preSibling = null;
        if (index > 0) {
            preSibling = nodes.get(index - 1);
        }
        addSubtreeToFlatIndex(preSibling, preVisibleSibling, dataNode);

        //回掉给子类
        onChildNodeAdded(dataNode, position);
    }

    private void onInternalChildRemove(DataNode dataNode, int position) {
        //更新受影响的先辈节点的状态
        int deltaDescendantSize = -dataNode.getFlatSize();
        int deltaVisibleDescendantSize = -dataNode.getVisibleFlatSize();
        notifyDescendantStateChange(deltaDescendantSize, deltaVisibleDescendantSize);


        //从index中删除
        removeSubtreeFromFlatIndex(dataNode);

        dataNode.mParentNode = null;

        //回掉给子类
        onChildNodeRemoved(dataNode,position);
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
     * 子孙节点改变，引起先辈节点与子孙节点有关的状态改变
     *
     * @param deltaDescendantSize        所导致的 子孙节点数量的增减
     * @param deltaVisibleDescendantSize 所导致的 子孙节点可见数的增减
     */
    private void notifyDescendantStateChange(int deltaDescendantSize, int deltaVisibleDescendantSize) {
        mDescendantSize += deltaDescendantSize;
        mDescendantVisibleSize += deltaVisibleDescendantSize;

        if (mParentNode != null) {

            //判断可见子孙节点数量的改变是否继续传递下去
            if (deltaVisibleDescendantSize != 0 && (!mVisibility || mIsFolded)) {
                deltaVisibleDescendantSize = 0;
            }

            if (deltaDescendantSize != 0 && deltaVisibleDescendantSize != 0)
                mParentNode.notifyDescendantStateChange(deltaDescendantSize, deltaVisibleDescendantSize);
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
     *
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

    private void addSubtreeToFlatIndex(DataNode preSibling, DataNode preVisibleSibling, DataNode subtree) {
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.addSubtree(preSibling, preVisibleSibling, subtree);
        }

        if (mParentNode != null)
            mParentNode.addSubtreeToFlatIndex(preSibling, preVisibleSibling, subtree);
    }

    private void removeSubtreeFromFlatIndex(DataNode subtree) {
        if (mNodeFlatIndex != null) {
            mNodeFlatIndex.removeFlatNodes(subtree);
        }

        if (mParentNode != null)
            mParentNode.removeSubtreeFromFlatIndex(subtree);
    }

    public static int calculateVisibleFlatSize(boolean visibility, boolean isFolded, int descendantVisibleSize){
        if (visibility == false) {
            return 0;
        } else if (isFolded == true) {
            return 1;
        } else {
            return descendantVisibleSize + 1;
        }
    }

}
