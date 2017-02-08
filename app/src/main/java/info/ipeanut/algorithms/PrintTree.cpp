//
// Created by 陈少 on 17/2/8.
//

#include<iostream>
#include<vector>
#include<deque>
using namespace std;
/**
 * http://blog.csdn.net/zzran/article/details/8778021
 *  层次遍历二叉树
 *  给定一棵二叉树，要求进行分层遍历
 */
typedef struct tree_node_s{
    char data;
    struct tree_node_s *lchild;
    struct tree_node_s *rchild;
}tree_node_t, *Tree;

/**
 * 第一种方法:利用递归
 */
int print_at_level(Tree T,int level){
    if(!T || level<0){
        return 0;
    }
    if(0 == level){
        cout << T->data << " ";
        return 1;
    }

    return print_at_level(T->lchild,level-1) + print_at_level(T->rchild,level-1);

}