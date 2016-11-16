### 排序
https://www.shiyanlou.com/questions/3931
要两两元素比较，一般需要两层for循环嵌套。
如果每过一轮，序列前面是有序的，则外层循环递增：
```
for (int i = 0; i < arr.length - 1; i++) {}
```

如果每过一轮，序列后面是有序的，则外层循环递减：
```
for (int i = arr.length; i >0 ; i--) {}
```