import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.Scanner;

public class Calc {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String imagePath = "src/main/resources/pic1.jpg";
        BufferedImage myImage = new BufferedImage(200, 200 ,5);

        Graphics2D gr = (Graphics2D) myImage.getGraphics();
        DataBuffer data = myImage.getRaster().getDataBuffer();
        System.out.println(data.getElem(0) + " " + data.getElem(1) + " " + data.getElem(2));
        while (true) {
            String line = scanner.nextLine();
            if (line.equals("break")) {
                break;
            }
            String[] splitLine = line.split(" ");
            float r = Float.parseFloat(splitLine[0]);
            float g = Float.parseFloat(splitLine[1]);
            float b = Float.parseFloat(splitLine[2]);
            float a = Float.parseFloat(splitLine[3]);
            gr.setColor(new Color(r, g, b, a));
            Polygon polygon = new Polygon();
            polygon.addPoint(0, 0);
            polygon.addPoint(0, 200);
            polygon.addPoint(200, 200);
            polygon.addPoint(200, 0);
            gr.fillPolygon(polygon);

            data = myImage.getRaster().getDataBuffer();
            System.out.println(data.getElem(0) + " " + data.getElem(1) + " " + data.getElem(2));
        }

        JLabel picLabel = new JLabel(new ImageIcon(myImage));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(myImage.getWidth(), myImage.getHeight()));
        frame.add(jPanel);
        frame.setVisible(true);
    }

}
