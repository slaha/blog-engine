package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: slaha
 * Date: 31.12.13
 * Time: 22:17
 */
public class ImageUtils {

	private static final short MAX_SIZE = 400;

	/**
	 * Scale buffered image.
	 *
	 * @param imageFile the image file
	 * @return the buffered image or null if: 1. error occurred; 2. image is smaller than the scaled image
	 */
	public static BufferedImage scale(File imageFile) {

		BufferedImage img;
		try {
			img = ImageIO.read(imageFile);
		} catch (IOException e) {
			return null;
		}

		int iWidth = img.getWidth();
		int iHeight = img.getHeight();

		if (Math.max(iWidth, iHeight) <= MAX_SIZE) {
			return null;
		}

		double scale = (double)MAX_SIZE / Math.max(iWidth, iHeight);

		int scaleX = (int) (scale * iWidth);
		int scaleY = (int) (scale * iHeight);

		Image image = img.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
		BufferedImage buffered = new BufferedImage(scaleX, scaleY, img.getType());
		buffered.getGraphics().drawImage(image, 0, 0 , null);
		buffered.getGraphics().dispose();

		return buffered;
	}

	/**
	 * Write image to file.
	 *
	 * @param zmensenina obrazek k zapsani
	 * @param puvodniSoubor puvodni soubor (z ktereho pochazi zmensenina)
	 * @param slozka slozka, kam ulozit
	 * @param oznaceniZmenseniny nejaky string, ktery se vlozi pred priponu souboru, aby se vedelo, ze se jedna o zmenseninu
	 * @return soubor se zvetseninou, NULL kdyz nastane chyba
	 */
	public static File write(BufferedImage zmensenina,
	                         File puvodniSoubor,
	                         String slozka,
	                         String oznaceniZmenseniny) {

		int lastDot = puvodniSoubor.getName().lastIndexOf('.');

		String jmeno = puvodniSoubor.getName();
		String pripona = jmeno.substring(lastDot);
		jmeno = jmeno.substring(0, lastDot);
		jmeno += oznaceniZmenseniny + pripona;

		jmeno = slozka + jmeno;
		try {
			File soubor = new File(jmeno);
			boolean success = ImageIO.write(zmensenina, determineType(pripona), soubor);
			if (success) {
				return soubor;
			}
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	private static String determineType(String pripona) {
		if ("gif".equalsIgnoreCase(pripona)) {
			return "GIF";
		}
		if ("png".equalsIgnoreCase(pripona)) {
			return "PNG";
		}
		if ("bmp".equalsIgnoreCase(pripona)) {
			return "BMP";
		}
		return "JPEG";
	}
}
