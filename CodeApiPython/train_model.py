# train_model.py
import pandas as pd
from sklearn.neighbors import NearestNeighbors
import pickle

def train_knn_model(csv_path="ratings.csv", model_path="knn_model.pkl"):
    df = pd.read_csv(csv_path)

    # Création de la matrice user-game
    user_game_matrix = df.pivot(index="user_id", columns="game_id", values="rating").fillna(0)

    # Entraînement du modèle KNN
    knn = NearestNeighbors(metric="cosine", algorithm="brute")
    knn.fit(user_game_matrix)

    # Sauvegarde du modèle et de la matrice
    with open(model_path, "wb") as f:
        pickle.dump((knn, user_game_matrix), f)

    print("✅ Modèle KNN entraîné et sauvegardé.")

if __name__ == "__main__":
    train_knn_model()
