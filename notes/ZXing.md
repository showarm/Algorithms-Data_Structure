
zxing 

http://git.oschina.net/charliec/NSCSupervisor1/blob/master/app/src/main/java/com/nsc/supervisor/sweep/PreviewCallback.java?oid=71f3cb84e607d44c5a01f29ff117e43e40fe9d99 

1 Camera callback来onPreviewFrame(byte[] data, Camera camera)

起点：字节数组data

2 DecodeHandler＃decode(byte[] data, int width, int height)

// 预览界面最终取到的是个bitmap，然后对其进行解码
rawResult = multiFormatReader.decodeWithState(bitmap);



/Users/chenshaosina/dev/android/young/NSCSupervisor1/app/libs/core-2.3.0.jar!/com/google/zxing/qrcode/QRCodeReader.class





