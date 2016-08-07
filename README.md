# LinearTree
A java util library mainly for Android application. 
It is used to build a tree. The tree you built is a little special, from which you can get a particular index. 
By the index, you can traverse the tree in such an order: A node is visited first, then it's children, finally the same hierarchal level nodes after it.
It is like table of contents in a book. A chapter's sections are in a deeper hierarchal, but they are listed before the next chapter.

So this util can be used to easily display almost any complex data to ListView or RecyclerView, just like table of contents. What you need to do is just build a tree which has the same structure of the data, and set data to corresponding node, then visit data by the index of that tree. 

Not only that, each node can hide or show all it's child nodes, the index will pass(ignore) a node's child nodes if the node hides it's children. This feature is like ExpandableListView's ability. But ExpandableListView can have two Hierarchies at most. This util is not limited.

To use this util, you can also benefit from decoupling data with ui. Reusing data between ListView, RecyclerView, LinearLayout or other kind Views is easy.


一个工具库，用来构造树，所构造的树的特殊之处在于可以获取树的索引，用这个索引能以子节点先于同级节点的顺序遍历树的所有节点，达到的效果与书的目录的编排顺序一致（章节后面是子章节，子章节排完了才是下一个章节）。

开发这个库的目的是为了简化安卓应用中依赖列表视图（ListView，RecyclerView）的需求的工作，任何复杂的树状数据结构都可轻松以列表形式排列，达到在ListView或RecyclerView中展示的目的。

树的任意节点可以收缩和展开,应用在ListView/RecyclerView中可以达到ExpandableListView的效果,但不限于数据的层级(而ExpandableListView只能展示两层数据).

可以利用树的优势构造任意结构的树,比如靠前的节点作为HeaderView的数据,无需特地为ListView设置HeaderView,也不用为RecyclerView没有HeaderView的功能而烦恼,达到了数据源与视图的解耦.

## Download
currently available in jcenter.

via Gradle:

    compile 'cn.okayj:lineartree:0.2.0'

## Demo
[HierarchicalViewSample][1]

## License
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
[1]: https://github.com/jack-cook/HierarchicalViewSample