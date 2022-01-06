# Victor Manuel Blanes Castro
import pandas as pd

# Llibreries que necessitarem
import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import train_test_split

random_value = 33
# Carrega de dades i preparació de les dades emprant Pandas
data = pd.read_csv("data/day.csv")
datos = pd.DataFrame(data.iloc[:, 4:13])  # Seleccionam totes les files i les columnes per index
valors = pd.DataFrame(data.iloc[:, -1])  # Seleccionam totes les files i la columna objectiu

X = datos.to_numpy()
y = valors.to_numpy().ravel()
features_names = datos.columns

# Creamos conjunto de entrenamiento y testeo
X_train, X_test, y_train, y_test = \
    train_test_split(X, y, test_size=0.30, random_state=1)

# Aplicamos gridSearch
rfr = RandomForestRegressor()
param_grid = {'n_estimators': np.arange(100, 500, 100).tolist(), 'min_samples_split': np.arange(2, 10, 2).tolist(),
              'max_features': ('auto', 'sqrt', 'log2')}
gsv = GridSearchCV(rfr, param_grid, cv=None, verbose=0)

# Entrenamos el modelo
gsv.fit(X_train, y_train)
print(gsv.best_estimator_)

# Resultados finales (Prediction usa los mejores hyperparametros)
gsv_prediction = gsv.predict(X_test)
