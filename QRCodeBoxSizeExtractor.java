import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import py4j.GatewayServer;
import java.net.InetAddress;
import java.util.Collections;

public class QRCodeBoxSizeExtractor {

    // 既存のメソッド（ファイルパスから処理）
    public String extractMatrix(String inputFilePath) {
        try {
            File inputFile = new File(inputFilePath);
            BufferedImage bufferedImage = ImageIO.read(inputFile);
            return processImage(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to read the image file.";
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "Failed to extract a BitMatrix from the image.";
        }
    }

    // Base64データから処理する新しいメソッド
    public String extractMatrixFromBase64(String base64Image) {
        try {
            // Base64デコードしてバイト配列を取得
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);

            // バイト配列からBufferedImageを作成
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (bufferedImage == null) {
                return "Failed to decode Base64 image data.";
            }

            // QRコードを解析
            return processImage(bufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to decode Base64 image data.";
        } catch (NotFoundException e) {
            e.printStackTrace();
            return "Failed to extract a BitMatrix from the image.";
        }
    }

    // 共通処理: BufferedImageからQRコードを解析
    private String processImage(BufferedImage bufferedImage) throws NotFoundException {
        // QRコード画像を解析して二値化
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap bitmap = new BinaryBitmap(binarizer);

        // ビットマトリクスを取得
        BitMatrix bitMatrix = bitmap.getBlackMatrix();

        // マトリックスサイズを表示
        System.out.println("Matrix Size: " + bitMatrix.getWidth() + " x " + bitMatrix.getHeight());

        // ビットマトリクスをフラットな文字列に変換
        StringBuilder matrixData = new StringBuilder();
        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                matrixData.append(bitMatrix.get(x, y) ? "1" : "0");
            }
        }

        // フラット化されたマトリクスデータを返却
        return matrixData.toString();
    }

    public static void main(String[] args) {
        QRCodeBoxSizeExtractor app = new QRCodeBoxSizeExtractor();

        try {
            // すべてのネットワークインターフェースでリッスン
            InetAddress address = InetAddress.getByName("0.0.0.0");

            // GatewayServerのインスタンスを作成
            GatewayServer server = new GatewayServer(
                app,                     // entryPoint
                25333,                   // port
                0,                       // pythonPort
                address,                 // address
                null,                    // pythonAddress
                0,                       // connectTimeout
                0,                       // readTimeout
                Collections.emptyList()  // customCommands
            );

            // サーバーの起動
            server.start();
            System.out.println("Gateway Server Started on 0.0.0.0:25333");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

