https://www.tuicool.com/articles/3ayMbyZ
https://www.tuicool.com/articles/6nYvAj
https://www.tuicool.com/articles/vQVVZ3

#### 录制原理：

1.录制PCM音频，录制格式为WAV格式
2.获取PCM音频buffer，每20ms一个buffer，进行speed音频编码encode
3.将buffer写入文件末尾
4，录制过程中生成的PCM音频buffer只存在于内存中，占用少量内存，每转换一个buffer的speex格式则将其存储到本地文件中
5，在8khz下每秒钟占用1kB的本地存储空间（8khz对于语音说话足够）

#### 播放原理：
1，将speex文件整个读入内存中
2，将speex一段段的读入buffer，通过SpeexCodec进行decode
3，decode的PCM音频buffer输入到RawAudioDataPlayer中
4，RawAudioDataPlayer 将PCM的buffer音频通过AudioQueueEnqueueBuffer进行播放输出