package org.jetbrains.research.tasktracker.modelInference

import io.kinference.core.data.tensor.KITensor
import io.kinference.core.data.tensor.asTensor
import io.kinference.ndarray.arrays.FloatNDArray
import org.bytedeco.javacv.Frame

class ImageVectorizer {
    fun vectorize(inputShape: List<Int>, image: Frame): KITensor {
        TODO()
//        return FloatNDArray(inputShape.toIntArray()) { image. }.asTensor()
    }
}