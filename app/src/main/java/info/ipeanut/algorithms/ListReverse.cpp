//
// Created by 陈少 on 17/2/8.
//

#include<iostream>
#include<vector>
#include<deque>
using namespace std;

/* 单链表反转/逆序 */
Status ListReverse(LinkList L)
{
    LinkList current,pnext,prev;
    if(L == NULL || L->next == NULL)
        return L;
    current = L->next;  /* p1指向链表头节点的下一个节点 */
    pnext = current->next;
    current->next = NULL;
    while(pnext) //如果有next
    {
        prev = pnext->next; //prev指针后移一位
        pnext->next = current;  //pnext所在节点 反转
        current = pnext; //current指针后移一位
        pnext = prev; //pnext指针后移一位
        printf("交换后：current = %d,next = %d \n",current->data,current->next->data);
    }
    //printf("current = %d,next = %d \n",current->data,current->next->data);
    L->next = current;  /* 将链表头节点指向p1 */
    return L;
}
