/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Get.Image;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Visual-Studio
 */
public class chatImage {
    public Image getImage(String addressImage){
        Image image = null;
        try {
            image = ImageIO.read(getClass().getClassLoader().getResource(addressImage));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }        
        return image;
    }
}
