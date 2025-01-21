import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class QRCodeBoxSizeExtractor {
    public static void main(String[] args) {
        try {
            // QRコード画像のパス
            // File inputFile = new File("data/wiki.png"); // 適切な画像パスに変更してください
            File inputFile = new File("data/google.com.png"); // 適切な画像パスに変更してください
            BufferedImage bufferedImage = ImageIO.read(inputFile);

            // QRコード画像を解析して二値化
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap bitmap = new BinaryBitmap(binarizer);

            // ビットマトリクスを取得
            BitMatrix bitMatrix = bitmap.getBlackMatrix();

            // ビットマトリクスサイズの確認
            System.out.println("Matrix Size: " + bitMatrix.getWidth() + " x " + bitMatrix.getHeight());

            // ビットマトリクスをテキストファイルに出力
            File outputFile = new File("output_matrix.txt");
            writeBitMatrixToFile(bitMatrix, outputFile);

            System.out.println("Matrix data has been written to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Failed to read the image file.");
            e.printStackTrace();
        } catch (NotFoundException e) {
            System.err.println("Failed to extract a BitMatrix from the image.");
            e.printStackTrace();
        }
    }

    // ビットマトリクスをテキストファイルに出力するメソッド
    private static void writeBitMatrixToFile(BitMatrix bitMatrix, File outputFile) throws IOException {
        try (FileWriter writer = new FileWriter(outputFile)) {
            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    writer.write(bitMatrix.get(x, y) ? "1" : "0");
                }
                writer.write(System.lineSeparator()); // 改行を追加
            }
        }
    }
}
