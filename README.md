# JavaUtil
This is a util library for java language.
The util DataNode in this project is developed mainly to use in Android projects. 
But it has no dependencies on android framwork. So I extract it to this java project.
This project is useful in android, and can also in any java program.

The DataNode model represent the node in tree. A tree which it built with can easily mapped to a list of nodes. It is very useful to map a tree like data set into ListView or RecyclerView.
The model keep the structure of tree and you can traverse the tree using it's index(implemented by List). You can modify the tree, the index will update automatically so that you can concentrate on your business without worrying about a certain modification to show data in ListView or RecyclerView.

The DataNode has some useful functions. You can hide any node in a tree, it's index will exclude this node and all it children. Similarly you can just hide childern of one node (Fold in ListView's perspective). So you can use it to achieve the behaviour of ExpandableListView, and the feature is not limited to one hierarchy, any node in any hierarchy of a tree can be folded(collapse).
This model can also make your code consistent, keep you away from switch between ListView and ExpandableListView.

##License
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.