import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeDetailedInfoExtractor {
    public static void main(String[] args) {
        try {
            // QRコード画像のパス
            File file = new File("data/wiki.png"); // 必要に応じて画像ファイルパスを修正
            BufferedImage bufferedImage = ImageIO.read(file);

            // QRコード画像を解析
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            System.out.println(bitmap);

            // QRコードのデコード
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            System.out.println("QR Code Content: " + result.getText());

            // 基本的なメタデータを取得
            System.out.println("Barcode Format: " + result.getBarcodeFormat());
            if (result.getResultMetadata() != null) {
                System.out.println("Result Metadata: " + result.getResultMetadata());
            }

            // QRコードの位置情報を取得
            ResultPoint[] points = result.getResultPoints();
            if (points != null) {
                System.out.println("QR Code Position:");
                for (ResultPoint point : points) {
                    System.out.println(" - " + point);
                }
            }

            // QRコードのマトリックスデータを取得
            BitMatrix bitMatrix = bitmap.getBlackMatrix();
            // printBitMatrix(bitMatrix);

        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    // マトリックスデータを表示するヘルパーメソッド
    private static void printBitMatrix(BitMatrix bitMatrix) {
        if (bitMatrix == null) {
            System.out.println("BitMatrix is null!");
            return;
        }

        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                System.out.print(bitMatrix.get(x, y) ? "1" : "0");
            }
            System.out.println();
        }
    }
}
