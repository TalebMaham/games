import pickle
from models import UserData

with open("knn_model.pkl", "rb") as f:
    knn_model, user_game_matrix = pickle.load(f)

def generate_recommendations(user_data: UserData):
    user_vector = [0] * len(user_game_matrix.columns)
    game_index_map = {game_id: i for i, game_id in enumerate(user_game_matrix.columns)}

    for purchase in user_data.purchases:
        if purchase.game_id in game_index_map:
            idx = game_index_map[purchase.game_id]
            user_vector[idx] = purchase.rating

    distances, indices = knn_model.kneighbors([user_vector], n_neighbors=3)
    neighbor_ids = user_game_matrix.index[indices[0]].tolist()
    neighbor_ratings = user_game_matrix.loc[neighbor_ids]
    avg_ratings = neighbor_ratings.mean().sort_values(ascending=False)

    owned_ids = {p.game_id for p in user_data.purchases}
    available_ids = {g.game_id for g in user_data.available_games}

    recommendations = []
    for game_id, base_score in avg_ratings.items():
        if game_id in available_ids and game_id not in owned_ids:
            meta = next((g for g in user_data.available_games if g.game_id == game_id), None)
            score = base_score

    
            if user_data.age is not None and meta:
                if user_data.age < 12 and meta.category == "ENFANT":
                    score += 1.0  

            recommendations.append({
                "game_id": int(game_id),
                "score": round(score, 2),
                "author": meta.author if meta else None,
                "publisher": meta.publisher if meta else None,
                "category": meta.category if meta else None
            })

        if len(recommendations) >= 5:
            break

    return recommendations
