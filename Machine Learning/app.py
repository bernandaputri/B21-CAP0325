from flask import Flask, request
from google.cloud import storage
import os
import tensorflow as tf
from keras.preprocessing import image
import numpy as np
import urllib

os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = "adc.json"
os.environ['CUDA_VISIBLE_DEVICES'] = '-1'
model = tf.keras.models.load_model('model.h5')
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, Docker!'


def returnLabel(testLabel):
  categories = ['building', 'road', 'bridge']
  levels = ['heavy', 'moderate', 'minor']

  category = testLabel[0][0]
  level = testLabel[1][0]
  label = {}

  for val_c, label_c in zip(category, categories):
    for val_l, label_l in zip(level,levels) :
      index = label_l+'_'+label_c
      value = (val_c+val_l)/2
      label[index] = float("{:.2f}".format(value))
      #label[index] = value
  return label

@app.route('/predict', methods=['GET', 'POST'])
def predictPath():
  if request.method == 'POST':
    filename = request.args.get('path')
    storage_client = storage.Client("[arjuna-project]")
    bucket = storage_client.get_bucket('projectlist1')
    blob = bucket.blob(filename)
    path_download = "temp.jpg"
    blob.download_to_filename(path_download)
    result = predict(path_download)
    blob.metadata = result
    blob.patch()
    # print(blob.metadata)
    return blob.metadata
    #return result
  else:
    return 'Use POST to predict'

def predict(path):
    img = image.load_img(path, target_size=(500,500))
    x = image.img_to_array(img)
    x = np.expand_dims(x, axis=0)

    images = np.vstack([x])
    classes = model.predict(images, batch_size=1)
    result = returnLabel(classes)
    return result

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
