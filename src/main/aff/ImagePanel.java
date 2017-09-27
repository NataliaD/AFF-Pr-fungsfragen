package main.aff;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	private Image image;
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		
		if (image == null) {
			g.setColor(SystemColor.control /* Color.getColor(nm)*/);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		} else {
			//g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
			g.drawImage(image, 0, 0, null);
		}
	}

	public void loadRessource(String res){
		
		URL imgUrl = getClass().getResource(res);
		if (imgUrl != null) {
			System.out.println(imgUrl.toString());
			try {
				image = ImageIO.read(imgUrl);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				image = null;
			}
		} else {
			image = null;
		}
			
	}
}
