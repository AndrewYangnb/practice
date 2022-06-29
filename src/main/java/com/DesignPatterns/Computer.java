package com.DesignPatterns;

// 一个电脑类
public class Computer {
    public String cpu;//必须
    public String ram;//必须
    public int usbCount;//可选
    public String keyboard;//可选
    public String display;//可选

    // 私有构造器，目的是用Builder完成对象的创建
    private Computer(){
    }

    // 静态内部类，Builder
    public static class Builder{
        Computer c = new Computer();

        // 实例化一个对象必须要传递的参数
        public Builder basicInfo(String cup,String ram){
            c.cpu=cup;
            c.ram=ram;
            //return this 是为了使用链式编程
            return this;
        }

        // 下面是可选参数
        public Builder setUsbCount(int usbCount) {
            c.usbCount = usbCount;
            return this;
        }
        public Builder setKeyboard(String keyboard) {
            c.keyboard = keyboard;
            return this;
        }
        public Builder setDisplay(String display) {
            c.display = display;
            return this;
        }
        public Computer build(){
            return c;
        }
    }
}

