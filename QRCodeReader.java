import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class QRCodeReader {
    public static void main(String[] args) {
        try {
            File file = new File("data/wiki.png"); // 解析するQRコード画像のパス
            BufferedImage bufferedImage = ImageIO.read(file);

            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap);

            System.out.println("QR Code Content: " + result.getText());
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }
}
