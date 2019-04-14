# pixScanner


### 原理：借助Android系统提供的ImageReader读取当前屏幕，然后转化为Bitmap对象，Bitmap像素宽高正好和当前屏幕一致，然后调用Bitmap中的getPixel(x, y)方法获取目标点的坐标


### 具体业务中使用
```java
int color = GBData.getColor(x,y)
int r = Color.red(color);
int g = Color.green(color);
int b = Color.blue(color);
LogUtils.d("red:" + r + ",green:" + g + ",blue:" + b);
