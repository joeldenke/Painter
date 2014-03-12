package paintdrawer;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by joel on 2014-03-12.
 */
public class ResourceManager
{

    public ResourceManager()
    {
    }

    public URL getResource(String path, String suffix)
    {
         URL url = getClass().getResource(String.format("%s.%s", path, suffix));
         System.out.println(String.format("%s.%s", path, suffix));

         return getClass().getResource(String.format("%s.%s", path, suffix));
    }

    public Image getImage(String name, String suffix)
    {
         return null;
    }

    public Image getIconImage(String name, String suffix)
    {
        ImageIcon icon = new ImageIcon(this.getResource("/images/" + name, suffix));
        return icon.getImage();
    }
}