package org.raisercostin.modules.register.server;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import java.util.UUID;

import javax.imageio.ImageIO;

/**
 * ImageVerification is a simple utility class for 
 * creating an image verification PNG file that will
 * allow you to make sure that only a human can read
 * the alphanumeric values and enter them into a text
 * field during verification. <P>
 * 
 */
public class ImageVerification {
	private String value;

	public ImageVerification(OutputStream out) throws IOException {
		this(50, 120, out);
	}

	public ImageVerification(int height, int width, OutputStream out) throws IOException {
		BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random(System.currentTimeMillis());
		Graphics2D g = bimage.createGraphics();

		// create a random color
		Color color = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));

		// the the background to the random color to fill the
		// background and make it darker
		g.setColor(color.darker());
		g.fillRect(0, 0, width, height);

		// set the font
		g.setFont(new Font("arial", Font.BOLD, 36));

		// generate a random value
		this.value = UUID.randomUUID().toString().replace("-", "").substring(0, 5);

		int w = (g.getFontMetrics()).stringWidth(value);
		int d = (g.getFontMetrics()).getDescent();
		int a = (g.getFontMetrics()).getMaxAscent();

		int x = 0, y = 0;

		// randomly set the color and draw some straight lines through it
		for (int i = 0; i < height; i += 5) {
			g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
			g.drawLine(x, y + i, width, y + i);
		}

		// reset x and y
		x = 0;
		y = 0;

		// randomly set the color of the lines and just draw think at an angle
		for (int i = 0; i < height; i += 5) {
			g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
			g.drawLine(x, y + d - i, width + w, height + d - i);
		}

		// randomly set the color and make it really bright for more readability
		g.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)).brighter().brighter());

		// we need to position the text in the center of the box
		x = width / 2 - w / 2;
		y = height / 2 + a / 2 - 2;

		// affine transform is used to rock the text a bit
		AffineTransform fontAT = new AffineTransform();
		int xp = x - 2;
		// walk through each character and rotate it randomly
		for (int c = 0; c < value.length(); c++) {
			// apply a random radian either left or right (left is half since it's too far back)
			int rotate = rand.nextInt(20);
			fontAT.rotate(rand.nextBoolean() ? Math.toRadians(rotate) : -Math.toRadians(rotate / 2));
			Font fx = new Font("arial", Font.BOLD, 36).deriveFont(fontAT);
			g.setFont(fx);
			String ch = String.valueOf(value.charAt(c));
			int ht = rand.nextInt(3);
			// draw the string and move the y either up or down slightly
			g.drawString(ch, xp, y + (rand.nextBoolean() ? -ht : ht));
			// move our pointer
			xp += g.getFontMetrics().stringWidth(ch) + 2;
		}

		// write out the PNG file
		ImageIO.write(bimage, "png", out);
		// System.out.println("DONE WRITYING NEW IMAGHE");

		// make sure your clean up the graphics object
		g.dispose();
	}

	/**
	 * return the value to check for when the user enters it in. Make sure you
	 * store this off in the session or something like a database and NOT in the 
	 * form of the webpage since the whole point of this exercise is to ensure that
	 * only humans and not machines are entering the data.
	 * 
	 * @return
	 */
	public String getVerificationValue() {
		return this.value;
	}
}
