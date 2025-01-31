import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QRCodeDetailedInfoExtractor {
    public static void main(String[] args) {
        try {
            // テキストファイルの準備
            File outputFile = new File("output_details.txt");
            try (FileWriter writer = new FileWriter(outputFile)) {
                // QRコード画像のパス
                File file = new File("data/wiki.png"); // 必要に応じて画像ファイルパスを修正
                BufferedImage bufferedImage = ImageIO.read(file);

                // QRコード画像を解析
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                writer.write("Binary Bitmap Details: " + bitmap + System.lineSeparator());
                System.out.println(bitmap);

                // QRコードのデコード
                MultiFormatReader reader = new MultiFormatReader();
                Result result = reader.decode(bitmap);
                writer.write("QR Code Content: " + result.getText() + System.lineSeparator());
                System.out.println("QR Code Content: " + result.getText());

                // 基本的なメタデータを取得
                writer.write("Barcode Format: " + result.getBarcodeFormat() + System.lineSeparator());
                System.out.println("Barcode Format: " + result.getBarcodeFormat());

                if (result.getResultMetadata() != null) {
                    writer.write("Result Metadata: " + result.getResultMetadata() + System.lineSeparator());
                    System.out.println("Result Metadata: " + result.getResultMetadata());
                }

                // QRコードの位置情報を取得
                ResultPoint[] points = result.getResultPoints();
                if (points != null) {
                    writer.write("QR Code Position:" + System.lineSeparator());
                    System.out.println("QR Code Position:");
                    for (ResultPoint point : points) {
                        writer.write(" - " + point + System.lineSeparator());
                        System.out.println(" - " + point);
                    }
                }

                // QRコードのマトリックスデータを取得
                BitMatrix bitMatrix = bitmap.getBlackMatrix();
                writer.write("Matrix Size: " + bitMatrix.getWidth() + " x " + bitMatrix.getHeight() + System.lineSeparator());
                System.out.println("Matrix Size: " + bitMatrix.getWidth() + " x " + bitMatrix.getHeight());

                // マトリックスデータをファイルに出力
                writeBitMatrixToFile(bitMatrix, writer);
            }

            System.out.println("Details have been written to: output_details.txt");

        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    // マトリックスデータをテキストファイルに出力するヘルパーメソッド
    private static void writeBitMatrixToFile(BitMatrix bitMatrix, FileWriter writer) throws IOException {
        if (bitMatrix == null) {
            writer.write("BitMatrix is null!" + System.lineSeparator());
            return;
        }

        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                writer.write(bitMatrix.get(x, y) ? "1" : "0");
            }
            writer.write(System.lineSeparator());
        }
    }
}
