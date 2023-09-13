import numpy as np
from PIL import Image
import onnxruntime as rt
from onnxruntime.datasets import get_example


def preprocess(image_path):
    input_shape = (1, 1, 64, 64)
    img = Image.open(image_path)
    img = img.resize((64, 64), Image.LANCZOS)
    img.save("img_out_py.png")

    img_data = np.array(img)
    img_data = np.resize(img_data, input_shape).astype(np.float32)

    return img_data


def predict(image_path):
    example1 = get_example('/Users/maria.tigina/IdeaProjects/emotional-monitoring/ijPlugin/src/main/resources/model/emotion-ferplus-8.onnx')
    sess = rt.InferenceSession(example1, providers=rt.get_available_providers())

    input_name = sess.get_inputs()[0].name
    print("input name", input_name)

    output_name = sess.get_outputs()[0].name
    print("output name", output_name)

    img = preprocess(image_path)
    res = sess.run([output_name], {input_name: img})

    print(res)

if __name__ == '__main__':
    predict('img.png')
