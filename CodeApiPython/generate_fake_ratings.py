# generate_data.py
import pandas as pd
import random

n_users = 30
n_games = 40
max_ratings_per_user = 15

data = []

for user_id in range(1, n_users + 1):
    age = random.randint(14, 60)
    rated_games = random.sample(range(1, n_games + 1), random.randint(5, max_ratings_per_user))

    for game_id in rated_games:
        base = random.uniform(2.5, 4.5)

        # Bonus si jeu pour enfants et utilisateur jeune
        if age < 18 and game_id % 5 == 0:
            base += 0.5
        elif age > 50 and game_id % 7 == 0:
            base += 0.3

        rating = round(min(base + random.uniform(-1, 1), 5.0), 1)
        rating = max(1.0, rating)

        data.append({
            "user_id": user_id,
            "game_id": game_id,
            "rating": rating
        })

df = pd.DataFrame(data)
df.to_csv("ratings.csv", index=False)
print(" Fichier ratings.csv généré avec succès.")
