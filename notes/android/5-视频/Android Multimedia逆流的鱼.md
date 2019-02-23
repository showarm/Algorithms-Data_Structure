
Android Multimedia框架总结
https://github.com/hejunlin2013/DriodDeveloper
https://www.jianshu.com/p/56e0eeb93e3b
https://github.com/hejunlin2013/MultiMediaSample

（一）MediaPlayer介绍之状态图及生命周期




FFmpeg 开发（一）常用处理视频命令
https://juejin.im/entry/589bbdbdb123db16a3bd7050

<!-- -i input    -->
<!-- -an  disable audio -->
ffmpeg -i tt.mp4 -b:a 128k tt2.mp3   提取音频
ffmpeg -i tt.mp4 -vcodec h264 -s 352*278 -an -f m4v eeee.264  视频转码
ffmpeg -i test.mp4 rrr.avi  


muxer是指合并文件，即将视频文件、音频文件和字幕文件合并为某一个视频格式。比如把rmvb格式的视频，mp3格式的音频文件以及srt格式的字幕文件，合并成为一个新的mp4或者mkv格式的文件。

demuxer是muxer的逆过程，就是把合成的文件中提取出不同的格式文件。


