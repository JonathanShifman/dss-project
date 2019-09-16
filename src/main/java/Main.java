import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("A");
        String imagePath = "src/main/resources/image.jpg";
        String outputDir = "src/main/resources/";
        File f = new File(imagePath);
        BufferedImage image = ImageIO.read(f);
        BufferedImage myImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        Graphics2D g = (Graphics2D) myImage.getGraphics();
//        g.setStroke(new BasicStroke(10));

        g.setColor(new Color(0.0f, 0.0f, 1f, 0.5f));
        Polygon polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(0, 100);
        polygon.addPoint(100, 0);
        g.fillPolygon(polygon);
        ImageIO.write(myImage, "jpg", new File(outputDir + "out1.jpg"));

        g.setColor(new Color(0.0f, 1f, 0.0f, 0.5f));
        polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(0, 100);
        polygon.addPoint(100, 0);
        g.fillPolygon(polygon);
        ImageIO.write(myImage, "jpg", new File(outputDir + "out2.jpg"));

        g.setColor(new Color(1f, 0.0f, 0.0f, 0.5f));
        polygon = new Polygon();
        polygon.addPoint(0, 0);
        polygon.addPoint(0, 100);
        polygon.addPoint(100, 0);
        g.fillPolygon(polygon);
        ImageIO.write(myImage, "jpg", new File(outputDir + "out3.jpg"));

        JLabel picLabel = new JLabel(new ImageIcon(myImage));
        JPanel jPanel = new JPanel();
        jPanel.add(picLabel);
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(myImage.getWidth(), myImage.getHeight()));
        frame.add(jPanel);
        frame.setVisible(true);

        int a = 1;
    }

}
