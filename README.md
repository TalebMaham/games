# 📚 GamesUP - API Documentation

Cette API permet de gérer un système complet autour des jeux : utilisateurs, auteurs, éditeurs, jeux, wishlist, achats, stock, avis, etc. Elle est développée en Spring Boot avec une architecture modulaire.

## 🚀 Démarrage

1. Lancer l’application Spring Boot (`GamesUPApplication.java`)
2. Optionnel : lancer le script Python pour remplir automatiquement la base de données avec des utilisateurs, jeux, auteurs, éditeurs, wishlists, avis et achats.
   ```bash
   pip install requests
   python script.py
   ```

## 🔐 Authentification

### 🔸 Enregistrement

`POST /api/users/register`

```json
{
  "name": "Alice",
  "email": "alice@example.com",
  "password": "alicepass",
  "age": 22,
  "role": "CLIENT"
}
```

### 🔸 Connexion

`POST /api/users/login`

```json
{
  "email": "alice@example.com",
  "password": "alicepass"
}
```

Réponse : `token` JWT à utiliser dans l'en-tête `Authorization: Bearer <token>`

## 👤 Utilisateurs

- `GET /api/users` : liste des utilisateurs (admin uniquement)
- `GET /api/users/me` : infos de l’utilisateur connecté
- `PUT /api/users/{id}/role?role=ADMIN` : modifier le rôle

## ✍️ Auteurs / Éditeurs

> Requiert un token admin

- `POST /api/authors?name=Nom`
- `POST /api/publishers?name=Nom`
- `GET /api/authors`
- `GET /api/publishers`

## 🕹️ Jeux

- `POST /api/games` : création de jeu (token admin requis)
- `GET /api/games`
- `GET /api/games/author/{authorId}`
- `GET /api/games/category/{category}`

```json
{
  "title": "Chess",
  "author": { "id": 1 },
  "publisher": { "id": 1 },
  "category": "FAMILLE"
}
```

## 📦 Inventaire

- `GET /api/inventory` : stock actuel
- `POST /api/inventory/update?gameId=1&quantity=5` : mise à jour (admin)

## 💌 Wishlist

- `POST /api/wishlists`  
```json
{
  "userId": 1,
  "gameIds": [1, 2, 3]
}
```

- `GET /api/wishlists/user/{userId}`

## ⭐ Avis

- `POST /api/avis`
```json
{
  "commentaire": "Super jeu",
  "note": 5,
  "game": { "id": 1 }
}
```

- `GET /api/avis/game/{gameId}`

## 🛒 Achats

- `POST /api/purchases`
```json
{
  "userId": 1,
  "lines": [
    {
      "id": 1,
      "game": { "id": 1, "title": "Jeu A", "category": "EXPERT" },
      "prix": 39.99
    }
  ]
}
```

- `GET /api/purchases`
- `GET /api/purchases/user/{id}`

## 🧠 Recommandation (FastAPI)

### Prérequis
```bash
pip install -r requirements.txt
uvicorn main:app --reload
```

### Endpoint
`GET /api/games/recommend`

## ✅ Script d’initialisation

Un script `script.py` est disponible pour :
- Créer 2 admins et 18 clients
- Ajouter automatiquement des auteurs, éditeurs, 40 jeux
- Remplir les stocks
- Générer des wishlists, avis et achats aléatoires

## 📌 Notes

- Le script utilise la librairie `requests` pour simuler des appels API.
- Les emails générés sont uniques grâce à un horodatage.
- Les tokens JWT sont utilisés pour sécuriser les routes.