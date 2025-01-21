import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QRCodeMatrixExtractor {
    public static void main(String[] args) {
        try {
            // QRコード画像のパス
            File file = new File("data/wiki.png"); 
            BufferedImage bufferedImage = ImageIO.read(file);

            // QRコード画像を解析
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            // QRコードのデコード
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap);
            System.out.println("QR Code Content: " + result.getText());

            // QRコードのマトリックスデータを取得
            BitMatrix bitMatrix = bitmap.getBlackMatrix();
            
            // マトリックスサイズを表示
            System.out.println("Matrix Size: " + bitMatrix.getWidth() + " x " + bitMatrix.getHeight());

            // マトリックスデータをテキストファイルに出力
            writeBitMatrixToFile(bitMatrix, "output_matrix.txt");
        } catch (IOException | NotFoundException e) {
            e.printStackTrace();
        }
    }

    // マトリックスデータをテキストファイルに出力するヘルパーメソッド
    private static void writeBitMatrixToFile(BitMatrix bitMatrix, String filePath) {
        if (bitMatrix == null) {
            System.out.println("BitMatrix is null!");
            return;
        }

        try (FileWriter writer = new FileWriter(filePath)) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    writer.write(bitMatrix.get(x, y) ? "1" : "0");
                }
                writer.write(System.lineSeparator()); // 改行を追加
            }
            System.out.println("Matrix data has been written to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

