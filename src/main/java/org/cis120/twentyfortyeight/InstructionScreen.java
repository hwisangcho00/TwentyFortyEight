package org.cis120.twentyfortyeight;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class InstructionScreen extends JPanel {

    private String filePath;

    public InstructionScreen(String filePath) {
        this.filePath = filePath;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        BufferedImage bi = null;

        try {
            bi = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image i = bi.getScaledInstance(600, 700, Image.SCALE_FAST);
        g.drawImage(i, 0, 0, null);
    }
}
