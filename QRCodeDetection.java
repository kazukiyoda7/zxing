import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.detector.Detector;
import com.google.zxing.common.DetectorResult;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class QRCodeDetection {
    public static void main(String[] args) {
        try {
            // 入力画像のパス
            // String imagePath = "data/wiki.png"; // 適切な画像パスに変更してください
            // String imagePath = "data/google.com.png"; // 適切な画像パスに変更してください
            String imagePath = "data/images.jpg"; // 適切な画像パスに変更してください
            System.out.println("Reading image from: " + imagePath);

            // ビットマトリクスの取得
            BitMatrix bitMatrix = getBitMatrixFromImage(imagePath);

            // Detector を作成
            Detector detector = new Detector(bitMatrix);

            // QRコードの検出
            DetectorResult result = detector.detect();
            BitMatrix qrMatrix = result.getBits();

            // 検出結果を表示
            System.out.println("QR Code detected!");
            System.out.println("Matrix Size: " + qrMatrix.getWidth() + " x " + qrMatrix.getHeight());
            printBitMatrix(qrMatrix);

        } catch (IOException e) {
            System.err.println("Error reading image: " + e.getMessage());
            e.printStackTrace();
        } catch (NotFoundException e) {
            System.err.println("QR Code not found: " + e.getMessage());
            e.printStackTrace();
        } catch (FormatException e) {
            System.err.println("QR Code format error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static BitMatrix getBitMatrixFromImage(String imagePath) throws IOException, NotFoundException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        if (image == null) {
            throw new IOException("Failed to read image from path: " + imagePath);
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        return new BinaryBitmap(binarizer).getBlackMatrix();
    }

    private static void printBitMatrix(BitMatrix matrix) {
        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                System.out.print(matrix.get(x, y) ? "1" : "0");
            }
            System.out.println();
        }
    }
}
