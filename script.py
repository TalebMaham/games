import requests
import time
import random

BASE_URL = "http://localhost:8080/api"

# ----------- UTILS -----------

def register_user(name, email, age, password, role):
    response = requests.post(f"{BASE_URL}/users/register", json={
        "name": name,
        "email": email,
        "password": password,
        "age": age,
        "role": role
    })
    if response.status_code == 200:
        print("✅ User created:", response.json())
        return response.json()
    try:
        print("⚠️ User creation skipped:", response.json())
    except Exception:
        print("⚠️ User creation skipped. Response not JSON:", response.text)
    return None

def login_user(email, password):
    response = requests.post(f"{BASE_URL}/users/login", json={
        "email": email,
        "password": password
    })
    print("🔐 Login status:", response.status_code)
    if response.status_code == 200:
        try:
            token = response.json().get("token")
            print("✅ Token retrieved")
            return token
        except Exception:
            print("❌ Failed to parse login response")
    print("❌ Login failed:", response.text)
    return None

def create_author(name, token):
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.post(f"{BASE_URL}/authors", params={"name": name}, headers=headers)
    if response.status_code == 200:
        print("✅ Author created:", response.json())
        return response.json()
    print("❌ Error creating author:", response.status_code, response.text)
    return None

def create_publisher(name, token):
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.post(f"{BASE_URL}/publishers", params={"name": name}, headers=headers)
    if response.status_code == 200:
        print("✅ Publisher created:", response.json())
        return response.json()
    print("❌ Error creating publisher:", response.status_code, response.text)
    return None

def create_game(title, author_id, publisher_id, token, category):
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.post(f"{BASE_URL}/games", json={
        "title": title,
        "author": {"id": author_id},
        "publisher": {"id": publisher_id},
        "category": category
    }, headers=headers)
    if response.status_code == 200:
        print(f"✅ Game '{title}' created:", response.json())
        return response.json()
    print("❌ Error creating game:", response.status_code, response.text)
    return None

def update_stock(game_id, quantity, token):
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.post(f"{BASE_URL}/inventory/update", params={
        "gameId": game_id,
        "quantity": quantity
    }, headers=headers)
    if response.status_code == 200:
        print(f"📦 Stock mis à jour pour jeu {game_id}: {quantity}")
    else:
        print("❌ Échec mise à jour stock:", response.status_code, response.text)

def is_game_available(game_id):
    response = requests.get(f"{BASE_URL}/inventory")
    if response.status_code == 200:
        stock = response.json()
        return stock.get(str(game_id), 0) > 0
    return False

def create_wishlist(user_id, game_ids, token):
    headers = {"Authorization": f"Bearer {token}"}
    data = {"userId": user_id, "gameIds": game_ids}
    response = requests.post(f"{BASE_URL}/wishlists", json=data, headers=headers)
    if response.status_code == 200:
        print("✅ Wishlist created:", response.json())
        return response.json()
    print("❌ Error creating wishlist:", response.status_code, response.text)
    return None

def create_avis(commentaire, note, game_id, token):
    headers = {"Authorization": f"Bearer {token}"}
    data = {
        "commentaire": commentaire,
        "note": note,
        "game": {"id": game_id},
    }
    response = requests.post(f"{BASE_URL}/avis", json=data, headers=headers)
    if response.status_code == 200:
        print("📝 Avis ajouté:", response.json())
        return response.json()
    print("❌ Erreur avis:", response.status_code, response.text)
    return None

def create_purchase(user_id, lines, token):
    headers = {"Authorization": f"Bearer {token}"}
    data = {"userId": user_id, "lines": lines}
    response = requests.post(f"{BASE_URL}/purchases", json=data, headers=headers)
    if response.status_code == 200:
        print("🛒 Achat créé:", response.json())
        return response.json()
    print("❌ Erreur achat:", response.status_code, response.text)
    return None


def generate_fixed_lines(games):
    lines = []
    for i, g in enumerate(games, start=1):
        lines.append({
            "id": i,
            "game": {
                "id": g["id"],
                "title": g["title"],
                "category": g["category"]
            },
            "prix": round(random.uniform(10, 60), 2)
        })
    return lines



# ----------- SCRIPT PRINCIPAL -----------

timestamp = str(int(time.time()))
admins = []
clients = []

# Créer 2 admins
for i in range(2):
    email = f"admin{i}_{timestamp}@example.com"
    age = random.randint(14, 60)
    user = register_user(f"Admin{i}", email, age, "adminpass", "ADMIN")
    if user:
        admins.append(user)

# Créer 18 clients
for i in range(18):
    email = f"client{i}_{timestamp}@example.com"
    age = random.randint(14, 60)
    user = register_user(f"Client{i}", email, age, "clientpass", "CLIENT")
    if user:
        clients.append(user)

# Se connecter avec le premier admin
if not admins:
    print("❌ Aucun admin valide")
    exit()

token = login_user(admins[0]["email"], "adminpass")
if not token:
    print("❌ Token introuvable")
    exit()

# Créer des auteurs et éditeurs
authors = [create_author(f"Author{i}", token) for i in range(5)]
publishers = [create_publisher(f"Publisher{i}", token) for i in range(5)]

# Créer 40 jeux et initialiser le stock
games = []
for i in range(40):
    title = f"Game{i}"
    author = random.choice(authors)
    publisher = random.choice(publishers)
    category = random.choice(["ENFANT", "FAMILLE", "EXPERT"])
    if author and publisher:
        game = create_game(title, author["id"], publisher["id"], token, category)
        if game:
            games.append(game)
            update_stock(game["id"], quantity=5, token=token)

# Créer des wishlists aléatoires pour chaque client
for client in clients:
    selected_games = random.sample(games, k=min(3, len(games)))
    game_ids = [g["id"] for g in selected_games]
    create_wishlist(client["id"], game_ids, token)

# Ajouter des avis et des achats aléatoires pour chaque client
for client in clients:
    token_client = login_user(client["email"], "clientpass")
    if not token_client:
        continue

    reviewed_games = random.sample(games, k=random.randint(1, 3))
    for game in reviewed_games:
        commentaire = f"Avis de {client['name']} sur {game['title']}"
        note = random.randint(1, 5)
        create_avis(commentaire, note, game["id"], token_client)

    # Créer un achat si tous les jeux sont disponibles
    purchased_games = [g for g in random.sample(games, k=3) if is_game_available(g["id"])]
    lines = generate_fixed_lines(purchased_games)
    if lines:
        create_purchase(client["id"], lines, token_client)
