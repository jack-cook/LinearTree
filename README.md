# JavaUtil
This is a util library for java language.
The util DataNode in this project is developed mainly to use in Android projects. 
But it has no dependencies on android framwork. So I extract it to this java project.
This project is useful in android, and can also in any java program.

The DataNode model represent the node in tree. A tree which it built with can easily mapped to a list of nodes. It is very useful to map a tree like data set into ListView or RecyclerView.
The model keep the structure of tree and you can traverse the tree using it's index(implemented by List). You can modify the tree, the index will update automatically so that you can concentrate on your business without worrying about a certain modification to show data in ListView or RecyclerView.

The DataNode has some useful functions. You can hide any node in a tree, it's index will exclude this node and all it children. Similarly you can just hide childern of one node (Fold in ListView's perspective). So you can use it to achieve the behaviour of ExpandableListView, and the feature is not limited to one hierarchy, any node in any hierarchy of a tree can be folded(collapse).
This model can also make your code consistent, keep you away from switch between ListView and ExpandableListView.


这个库主要配合安卓应用中列表(ListView, RecyclerView)的使用. 如果所要开发的列表视图内容比较复杂,包含不同的view type和复杂的层级结构.配合使用该库会让事情变得十分简单.

这个库目前的主要功能是将树形的数据结构映射到列表结构,类似于书的目录,目录是树形结构的,目录包含章节,章节包含子章节.目录中的内容又是从上到下有序排列的列表形式.

使用该库的模型,可以构造一个树,这个树有一个索引(任何节点都可以构造索引),索引类似于目录,可以有序遍历树的节点.因此可以将复杂的树形数据方便地展示到ListView中,无需为如何在ListView中展示而烦恼.

该库还有隐藏节点的功能,达到与ExpandableListView同样的效果(任意层级任意节点可折叠,这是ExpandableLiseView达不到的),并且可以轻松达到header view ,footer view 的效果.一切都由该库的模型掌控, 将数据的操作和视图工具很好地解偶.

## Download
currently available in jcenter.

via Gradle:

    compile 'cn.okayj:java-util:0.1.0'

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