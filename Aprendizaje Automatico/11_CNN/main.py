#@title Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#@title MIT License
#
# Copyright (c) 2017 François Chollet
#
# Permission is hereby granted, free of charge, to any person obtaining a
# copy of this software and associated documentation files (the "Software"),
# to deal in the Software without restriction, including without limitation
# the rights to use, copy, modify, merge, publish, distribute, sublicense,
# and/or sell copies of the Software, and to permit persons to whom the
# Software is furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included in
# all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
# IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
# FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
# THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
# LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
# FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
# DEALINGS IN THE SOFTWARE.

# TensorFlow and tf.keras
import tensorflow as tf

# Helper libraries
import numpy as np
import matplotlib.pyplot as plt
from tensorflow.keras.utils import to_categorical

print(tf.__version__)

# Carrega de dades

fashion_mnist = tf.keras.datasets.fashion_mnist

(train_images, train_labels), (test_images, test_labels) = fashion_mnist.load_data()



class_names = ['T-shirt/top', 'Trouser', 'Pullover', 'Dress', 'Coat',
               'Sandal', 'Shirt', 'Sneaker', 'Bag', 'Ankle boot']


plt.figure(figsize=(10,10))
for i in range(25):
    plt.subplot(5, 5, i+1)
    plt.xticks([])
    plt.yticks([])
    plt.grid(False)
    plt.imshow(train_images[i], cmap=plt.cm.binary)
    plt.xlabel(class_names[train_labels[i]])
plt.show()


X_train = train_images.reshape(len(train_images),28,28,1)
X_test = test_images.reshape(len(test_images),28,28,1)

trainY = to_categorical(train_labels)
testY = to_categorical(test_labels)

X_train = X_train.astype('float')
trainY = trainY.astype('float')

X_test = X_train.astype('float')
testY = trainY.astype('float')

# X = np.random.random((10000,29,29,1))
# Y = np.random.randint(0,9,size=60000)
# Y2 = to_categorical(Y)
# print(train_labels.shape)
# print(Y.shape)

print(f"Input: {X_train.shape}, Output: {trainY.shape}")
# print(f"Input: {X.shape}, Output: {Y2.shape}")

model = tf.keras.models.Sequential()
model.add(tf.keras.layers.Conv2D(16,(3,3),activation="relu", padding="same"))
model.add(tf.keras.layers.MaxPool2D((2,2)))
model.add(tf.keras.layers.Conv2D(32,(3,3),activation="relu", padding="same"))
model.add(tf.keras.layers.MaxPool2D((2,2)))
model.add(tf.keras.layers.Conv2D(64,(3,3),activation="relu", padding="same"))
model.add(tf.keras.layers.MaxPool2D((2,2)))
model.add(tf.keras.layers.Conv2D(128,(3,3),activation="relu", padding="same"))
model.add(tf.keras.layers.MaxPool2D((2,2)))
model.add(tf.keras.layers.Flatten())
model.add(tf.keras.layers.Dense(10,'softmax'))

model.compile('adam','categorical_crossentropy',metrics="accuracy")
model.fit(X_train, trainY, validation_data=(X_test, testY), epochs=15)

test_loss, test_acc = model.evaluate(X_test, testY, verbose=2)

print('\nTest accuracy:', test_acc)

# Si el vostre model es diu "model" el dibuixarà
tf.keras.utils.plot_model(
    model,
    to_file="model.png",
    show_shapes=True,
    show_layer_names=True,
    rankdir="TB",
    expand_nested=True,
    dpi=96,
)



# TODO: Aquí heu de fer la compilació del model



# TODO: Entrenar



# TODO: Mostrar les gràfiques d'entrenament



# Avaluar el model i veure exemples de classificació.
# Com és la sortida de la xarxa? Com podem obtenir la classe?
