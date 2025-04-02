package com.MIA.Implementation;

import ai.djl.Application;
import ai.djl.Model;
import ai.djl.engine.Engine;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.translate.TranslateException;
import com.MIA.Service.ModelService;
import java.nio.file.Paths;
import java.util.List;

public class ModelServiceImpl implements ModelService {

    private final Model model;
    private final Predictor<Image, Classifications> predictor;

    public ModelServiceImpl(Model model, Predictor<Image, Classifications> predictor) {
        this.model = Model.newInstance(String.valueOf(Paths.get("resnet50")),"OnnxRuntime");
        this.predictor = predictor;
    }

    @Override
    public List<String> predict(String imagePath) throws Exception {
        // Load the image
        Image image = ImageFactory.getInstance().fromFile(Paths.get(imagePath));

        // Perform prediction
        try (Predictor<Image, Classifications> predictor = model.newPredictor(Application.CV.IMAGE_CLASSIFICATION,)) {
            Classifications classifications = predictor.predict(image);

            // Return the top classification results
            return classifications.topK(5).stream()
                    .map(Classifications.Classification::getClassName)
                    .toList();
        }
    }

}
