package de.vorb.tesseract.gui.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.bridj.Pointer;

import de.vorb.tesseract.bridj.Tesseract;
import de.vorb.tesseract.bridj.Tesseract.Pix;
import de.vorb.tesseract.bridj.Tesseract.TessOcrEngineMode;
import de.vorb.tesseract.tools.recognition.Recognition;
import de.vorb.tesseract.util.PixUtils;

public class PageLoader extends Recognition {
  BufferedImage originalImg = null;

  public PageLoader() throws IOException {
    super();
  }

  @Override
  protected void init() throws IOException {
    this.handle = Tesseract.TessBaseAPICreate();

    // init Tesseract with data path, language and OCR engine mode
    Tesseract.TessBaseAPIInit2(
        getHandle(),
        Pointer.pointerToCString("E:\\Masterarbeit\\Ressourcen\\tessdata"),
        Pointer.pointerToCString("deu-frak"), TessOcrEngineMode.OEM_DEFAULT);

    // set page segmentation mode
    Tesseract.TessBaseAPISetPageSegMode(getHandle(),
        Tesseract.TessPageSegMode.PSM_AUTO);
  }

  public void setOriginalImage(BufferedImage image) {
    this.originalImg = image;

    Tesseract.TessBaseAPISetImage2(getHandle(),
        Pointer.pointerTo(PixUtils.bufferedImageToPix(image)));
  }

  public BufferedImage getOriginalImage() {
    return originalImg;
  }

  public BufferedImage getThresholdedImage() {
    if (originalImg.getType() == BufferedImage.TYPE_BYTE_BINARY) {
      return originalImg;
    }

    final Pointer<Pix> img =
        Tesseract.TessBaseAPIGetThresholdedImage(getHandle());

    return PixUtils.pixToBufferedImage(img.get());
  }
}
