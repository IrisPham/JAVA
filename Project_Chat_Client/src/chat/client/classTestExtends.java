/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.client;

abstract class aminoA{
    public aminoA(){
    }
    public abstract void getName(); //Đây là một cái phương thức trừ tượng
    //Phương thức trừu tượng chỉ có khai báo và không thể cài đặt
    //Dùng từ khóa abstract để khai báo một phương thức trừu tượng
    //Các lớp con nhất định phải có phương thức trừu tượng của class cha, và cài đặt nó
    //Dùng để miêu tả cho việc chưa có định nghĩa cụ thể ví dụ như chim biet bay : nhung khong biet duoc bay cao hay thấp
    
}
interface aminoB{
    //Đây là một giao diện hoàn toàn ảo
    //các biến được khai báo dưới dạng hằng tĩnh; public static final 
    //Chỉ chứa các khai báo phương thức không dk cài đặt
    
}
class amino{
    public amino(){
        System.out.println("class động vật\n");
    }
}
class dog extends amino{
    
}
class cat extends aminoA{

    @Override
    public void getName() {
        System.out.println("class con");
    }
    
}
public class classTestExtends {
    public static void main(String[] args) {
        dog dog = new dog();
        cat cat = new cat();
        cat.getName();
    }
    
}
