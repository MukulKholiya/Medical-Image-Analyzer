package com.MIA.Service;

import ai.djl.translate.TranslateException;

import java.util.List;

public interface ModelService {
    List<String> predict(String image) throws Exception, TranslateException;
}
