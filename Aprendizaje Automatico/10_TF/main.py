# Llibreries
import numpy as np
import matplotlib.pyplot as plt
from sklearn.datasets import make_moons, make_circles, make_classification

from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

# TensorFlow
import tensorflow as tf
from tensorflow import keras

# Construïm 4 conjunts de dades per classificar:
# En primer lloc un conjunt linealment separable on afegim renou
X, y = make_classification(n_samples=300, n_features=2, n_redundant=0, n_informative=2, random_state=33, n_clusters_per_class=1)
rng = np.random.RandomState(2)
X += 2 * rng.uniform(size=X.shape)
linearly_separable = (X, y)

# En segon lloc un que segueix una distribució xor
X_xor = np.random.randn(300, 2)
y_xor = np.logical_xor(X_xor[:, 0] > 0,
                       X_xor[:, 1] > 0)
y_xor = np.where(y_xor, 1, -1)

# Els afegim a una llista juntament amb els seus noms per tal de poder iterar
# sobre ells
datasets = [
    ("linear", linearly_separable),
    ("moons", make_moons(n_samples=300, noise=0.3, random_state=30)),  # Tercer dataset
    ("circles", make_circles(n_samples=300, noise=0.2, factor=0.5, random_state=30)),  # Darrer dataset
    ("xor", (X_xor, y_xor))]

model = tf.keras.Sequential([
    tf.keras.layers.Input((2)),
    tf.keras.layers.Dense(2, activation='relu'),
    tf.keras.layers.Dense(1, activation='sigmoid')
])



model.compile(optimizer='adam', # també podria ser el descens de gradient tradicional
              loss=tf.keras.losses.BinaryCrossentropy(from_logits=True),
              metrics=['accuracy'])



# Ens proporciona un resum de com és la nostra xarxa, fixau-vos en el nombre de
# paràmetres
model.summary()



# En primer lloc preparam les dades

X, y = datasets[1][1]

X = StandardScaler().fit_transform(X)
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=33)



# En segon lloc entrenam:
# - Proporcionam les dades d'entrenament i de validació
# - Definim el nombre d'iteracions
# - Es pot definir el tamany del batch (batch_size)
history = model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=15)
test_loss, test_acc = model.evaluate(X_test, y_test, verbose=2)

print('\nTest accuracy:', test_acc)

# (history.history.keys())

# Mostram els resultats de l'entrenament de manera gràfica
figure, ax = plt.subplots(nrows=1, ncols=2)

ax[0].plot(history.history['accuracy'])
ax[0].plot(history.history['val_accuracy'])

ax[0].set_title('model accuracy')
ax[0].set_ylabel('accuracy')
ax[0].set_xlabel('epoch')
ax[0].set_ylim(0,1)
ax[0].legend(['train', 'test'], loc='upper left')

# summarize history for loss
ax[1].plot(history.history['loss'])
ax[1].plot(history.history['val_loss'])
ax[1].set_ylim(0,1)
ax[1].set_title('model loss')
ax[1].set_ylabel('loss')
ax[1].set_xlabel('epoch')
ax[1].legend(['train', 'test'], loc='upper right')
figure.tight_layout()
plt.show()

result = model.predict(X)

print(result)

